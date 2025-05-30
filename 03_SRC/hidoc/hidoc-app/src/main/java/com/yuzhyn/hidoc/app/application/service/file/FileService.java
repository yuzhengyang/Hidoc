package com.yuzhyn.hidoc.app.application.service.file;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileBucket;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.file.FileDownloadLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysMachine;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import com.yuzhyn.hidoc.app.application.mapper.file.FileBucketMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileDownloadLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserFileConfMapper;
import com.yuzhyn.hidoc.app.utils.ClientIPTool;
import com.yuzhyn.hidoc.app.utils.http.FileFetchTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileCharCodeTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 件处理
 * 操作文件时，使用文件处理服务，可快速完成响应操作
 */
@Slf4j
@Service
public class FileService {

    @Autowired
    SysUserFileConfMapper sysUserFileConfMapper;

    @Autowired
    FileBucketMapper fileBucketMapper;

    @Autowired
    FileCursorMapper fileCursorMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileDownloadLogMapper fileDownloadLogMapper;


    /**
     * <div javadoc="info" javadoc-cn="一句话精简概括">
     *     检查用户文件空间是否够用
     * </div>
     *
     * @param userId
     * @param fileSize
     * @return
     */
    public boolean checkSpaceLimit(String userId, long fileSize) {
        SysUserFileConf conf = sysUserFileConfMapper.selectById(userId);
        if (null != conf && conf.getUsedSpace() < conf.getSpaceLimit() && fileSize < (conf.getSpaceLimit() - conf.getUsedSpace())) {
            return true;
        }
        return false;
    }

    //region 文件上传功能函数

    /**
     * <div javadoc="info" javadoc-cn="一句话精简概括">
     *     上传文件
     * </div>
     *
     * <div javadoc="limit" javadoc-cn="使用限制说明">
     *     上传文件时，必须为登录状态
     * </div>
     *
     * @param collectedId
     * @param expiryTime
     * @param bucketName
     * @param files
     * @param curUser
     * @return
     */
    public List<FileCursor> uploadFiles(String collectedId, LocalDateTime expiryTime, String bucketName, MultipartFile[] files, SysUser curUser) {
        log.info("上传文件");
        List<FileCursor> sysFileList = new ArrayList<>();
        if (null == bucketName) bucketName = UUIDTool.get();
        if (null == expiryTime) expiryTime = LocalDateTimeTool.max();

        for (MultipartFile file : files) {
            if (StringTool.ok(bucketName) && expiryTime != null) {
                File sysFile = preCreateSysFile(file, curUser.getId());
                if (sysFile != null) {
                    Tuple3<Boolean, Boolean, File> saveToDisk = saveToDisk(file, sysFile);
                    File existFile = null;
                    if (saveToDisk.getT2()) existFile = saveToDisk.getT3();
                    if (saveToDisk.getT1()) {
                        log.info("保存文件到磁盘成功，现在将保存到数据库记录信息");
                        FileCursor cursor = saveDb(sysFile, existFile, bucketName, collectedId, expiryTime);
                        if (cursor != null) sysFileList.add(cursor);
                    }
                }
            }
        }
        return sysFileList;
    }

    /**
     * 预创建文件实体
     *
     * @param file
     * @return
     */
    private File preCreateSysFile(MultipartFile file, String userId) {
        if (!file.isEmpty() && userId != null) {
            if (this.checkSpaceLimit(userId, file.getSize())) {
                String fileId = R.SnowFlake.nexts();
                LocalDateTime createTime = LocalDateTime.now();
                String fileName = file.getOriginalFilename();
                long size = file.getSize();
                String yearMonthDir = DateTimeFormat.toStr(createTime, DateTimeFormatPattern.SHORT_YEAR_MONTH);
                String path = DirTool.combine(yearMonthDir, fileId.toString());

                File sysFile = new File();
                sysFile.setId(fileId);
                sysFile.setName(fileName);
                sysFile.setExt(FileTool.getExt(fileName));
                sysFile.setSize(size);
                sysFile.setPath(path);
                sysFile.setCreateTime(createTime);
                sysFile.setUserId(userId);
                sysFile.setIsDelete(false);
                sysFile.setIsClean(false);
                sysFile.setDownloadCount(0L);
                log.info("创建文件实体，文件名称为：" + fileName);
                return sysFile;
            }
        }
        return null;
    }

    /**
     * <div javadoc="info" javadoc-cn="一句话精简概括">
     *     保存文件到磁盘
     * </div>
     *
     * @param file
     * @param sysFile
     * @return 操作状态（继续执行标志）、是否已有文件、已有文件信息
     */
    private Tuple3<Boolean, Boolean, File> saveToDisk(MultipartFile file, File sysFile) {
        try {
            // 存储文件到临时文件夹
            java.io.File dest = new java.io.File(DirTool.combine(R.Paths.SysFileTemp, sysFile.getId().toString()));
            log.info("存储文件到临时文件夹：" + dest.getAbsolutePath());
            file.transferTo(dest);
            sysFile.setMd5(FileCharCodeTool.md5(dest));
            sysFile.setSha1(FileCharCodeTool.sha1(dest));
            // 查询重复文件（依据文件特征码）
            File record = fileMapper.selectOne(new LambdaQueryWrapper<File>().eq(File::getIsDelete, false).eq(File::getMd5, sysFile.getMd5()).eq(File::getSha1, sysFile.getSha1()).eq(File::getSize, sysFile.getSize()));
            // 查询到已经有相同文件，重置文件实体类
            if (record != null) {
                log.info("查询到已经有相同文件，删除本次上传的文件，减少空间占用");
                FileTool.delete(DirTool.combine(R.Paths.SysFileTemp, sysFile.getId().toString()));
                return Tuples.of(true, true, record);
            }

            // 如果特征码不重复，则将文件移动到标准指定路径中
            String yearMonthDir = DateTimeFormat.toStr(sysFile.getCreateTime(), DateTimeFormatPattern.SHORT_YEAR_MONTH);
            DirTool.create(DirTool.combine(R.Paths.SysFile, yearMonthDir));
            String path = DirTool.combine(yearMonthDir, sysFile.getId().toString());
            String source = DirTool.combine(R.Paths.SysFileTemp, sysFile.getId().toString());
            String target = DirTool.combine(R.Paths.SysFile, path);
            boolean fileMoveFlag = FileTool.move(source, target);
            if (fileMoveFlag) {
                log.info("文件移动成功，文件名称为：" + sysFile.getName());
                return Tuples.of(true, false, new File());
            }
        } catch (Exception ex) {
            log.error("存储失败");
            log.error(ExceptionTool.getStackTrace(ex));
        }
        return Tuples.of(false, false, new File());
    }

    /**
     * 保存数据实体到数据库中
     *
     * @param sysFile
     * @param existFile
     * @param bucketName
     * @param expiryTime
     * @return
     */
    private FileCursor saveDb(File sysFile, File existFile, String bucketName, String collectedId, LocalDateTime expiryTime) {
        // 判断是新增文件还是已经存在文件，如果是新增文件，则保存记录到数据库
        boolean fileSaveFlag = (null != existFile);
        if (null == existFile) {
            int flag = fileMapper.insert(sysFile);
            if (flag > 0) fileSaveFlag = true;
        }
        if (fileSaveFlag) {
            // 验证当前用户的Bucket是否存在
            // 如果不存在，则创建新的Bucket
            FileBucket bucket = fileBucketMapper.selectOne(new LambdaQueryWrapper<FileBucket>().eq(FileBucket::getUserId, sysFile.getUserId()).eq(FileBucket::getName, bucketName));
            boolean bucketSaveFlag = (null != bucket);
            if (null == bucket) {
                bucket = new FileBucket();
                bucket.setId(R.SnowFlake.nexts());
                bucket.setName(bucketName);
                bucket.setUserId(sysFile.getUserId());
                bucket.setIsOpen(true);
                int flag = fileBucketMapper.insert(bucket);
                if (flag > 0) bucketSaveFlag = true;
            }
            if (bucketSaveFlag) {
                // 创建文件Cursor
                FileCursor cursor = new FileCursor();
                cursor.setId(R.SnowFlake.nexts());
                cursor.setBucketId(bucket.getId());
                cursor.setCollectedId(collectedId);
                if (null != existFile) cursor.setFileId(existFile.getId());
                else cursor.setFileId(sysFile.getId());
                cursor.setFileName(sysFile.getName());
                cursor.setUserId(sysFile.getUserId());
                cursor.setCreateTime(sysFile.getCreateTime());
                cursor.setVersion(String.valueOf(System.currentTimeMillis()));
                cursor.setExpiryTime(expiryTime);
                cursor.setUname(cursor.getId() + "." + sysFile.getExt());
                cursor.setIsDelete(false);
                int flag = fileCursorMapper.insert(cursor);
                if (flag > 0) {
                    // 一切保存成功后，刷新用户文件配额信息
                    Long updateFlag = sysUserFileConfMapper.updateUsedSpace(sysFile.getUserId(), sysFile.getSize());
                    return cursor;
                }
            }
        }
        return null;
    }
    //endregion

    //region 下载文件功能函数

    /**
     * 根据桶名称和文件名称获取 文件指针信息和文件信息
     *
     * @param userPrefix
     * @param bucketName
     * @param fileName
     * @return
     */
    public Tuple2<FileCursor, File> getDownloadFile(String userPrefix, String bucketName, String fileName) {
        if (StringTool.ok(userPrefix, bucketName, fileName)) {
            SysUserFileConf conf = sysUserFileConfMapper.selectOne(new LambdaQueryWrapper<SysUserFileConf>().eq(SysUserFileConf::getUrlPrefix, userPrefix));

            if (conf != null) {
                FileBucket bucket = fileBucketMapper.selectOne(new LambdaQueryWrapper<FileBucket>().eq(FileBucket::getUserId, conf.getUserId()).eq(FileBucket::getName, bucketName));

                if (bucket != null) {
                    List<FileCursor> cursorList = fileCursorMapper.selectList(new LambdaQueryWrapper<FileCursor>().eq(FileCursor::getUserId, conf.getUserId()).eq(FileCursor::getBucketId, bucket.getId()).eq(FileCursor::getFileName, fileName).orderByDesc(FileCursor::getVersion));
                    FileCursor cursor = null;
                    if (ListTool.ok(cursorList)) cursor = cursorList.get(0);
                    if (null != cursor && null != cursor.getExpiryTime() && LocalDateTime.now().isBefore(cursor.getExpiryTime())) {
                        File sysFile = fileMapper.selectById(cursor.getFileId());
                        return Tuples.of(cursor, sysFile);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根据文件指针ID获取 文件指针信息和文件信息
     *
     * @param cursorId
     * @return
     */
    public Tuple2<FileCursor, File> getDownloadFileByCursor(String cursorId) {
        if (null != cursorId) {
            FileCursor cursor = fileCursorMapper.selectOne(new LambdaQueryWrapper<FileCursor>().eq(FileCursor::getId, cursorId));
            if (null != cursor && null != cursor.getExpiryTime() && LocalDateTime.now().isBefore(cursor.getExpiryTime())) {
                File sysFile = fileMapper.selectById(cursor.getFileId());
                return Tuples.of(cursor, sysFile);
            }
        }
        return null;
    }

    /**
     * 根据全局唯一名称获取 文件指针信息和文件信息
     *
     * @param uname
     * @return
     */
    public Tuple2<FileCursor, File> getDownloadFileByUname(String uname) {
        if (null != uname) {
//            try{
            FileCursor cursor = fileCursorMapper.selectOne(new LambdaQueryWrapper<FileCursor>().eq(FileCursor::getUname, uname));
            if (null != cursor && null != cursor.getExpiryTime() && LocalDateTime.now().isBefore(cursor.getExpiryTime())) {
                File sysFile = fileMapper.selectById(cursor.getFileId());
                return Tuples.of(cursor, sysFile);
            }
//            }catch (Exception ex){
//                log.debug("报错啦：" + uname);
//            }
        }
        return null;
    }

    /**
     * 根据 文件指针信息和文件信息 下载文件
     *
     * @param sysFileCursor
     * @param sysFile
     * @param request
     * @param response
     */
    public void download(String sourceName, FileCursor sysFileCursor, File sysFile, HttpServletRequest request, HttpServletResponse response) {
        if (sysFileCursor != null && sysFile != null) {
            try {
                log.info("download log: " + ClientIPTool.getIp(request) + ", file: " + sysFile.getName());
                transfer(sysFile, sysFileCursor, sourceName);

                String pathName = DirTool.combine(R.Paths.SysFile, sysFile.getRealPath());
                log.info("下载文件路径检查：" + pathName);

                if (FileTool.isExist(pathName)) {
                    java.io.File file = new java.io.File(pathName);
                    // 配置文件下载
                    response.setHeader("content-type", "application/octet-stream");
                    response.setContentType("application/octet-stream");
                    // 下载文件能正常显示中文
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(sysFile.getName(), "UTF-8"));
                    response.setContentLengthLong(sysFile.getSize());
                    // 实现文件下载
                    byte[] buffer = new byte[1024];
                    try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis)) {
                        OutputStream os = response.getOutputStream();
                        int i = bis.read(buffer);
                        while (i != -1) {
                            os.write(buffer, 0, i);
                            i = bis.read(buffer);
                        }
                        log.info("Download  successfully!");
                    } catch (IOException e) {
                        log.error(ExceptionTool.getStackTrace(e));
                        log.error("Download  failed!");
                    }

                    try {
                        FileDownloadLog log = new FileDownloadLog();
                        log.setId(R.SnowFlake.nexts());
                        log.setIp(ClientIPTool.getIp(request));
                        log.setCreateTime(LocalDateTime.now());
                        log.setCursorId(sysFileCursor.getId());
                        log.setFileName(sysFile.getName());
                        log.setFileId(sysFile.getId());
                        R.Queues.FileDownloadLogQueue.add(log);
                    } catch (Exception ex) {
                    }
                }
            } catch (Exception ex) {
                log.error(ExceptionTool.getStackTrace(ex));
            }
        }
    }

    /**
     * 文件不存在的补偿机制，去其他服务器下载文件
     *
     * @param sysFile
     * @param sysFileCursor
     * @throws IOException
     */
    public void transfer(File sysFile, FileCursor sysFileCursor, String controllerName) {
        // 判断内部处理逻辑的来源，能进入transfer的方法来源有：定时任务和下载文件
        // 定时任务：FileCheckSchedule
        // 下载文件：FileController 或 FileApiController
        // 如果是FileApiController（直接进入transfer接口的，则不能循环再进行transfer调用）
        if (controllerName.equals("FileApiController")) return;

        // 如果文件已存在，则不需要补偿，则跳过transfer逻辑
        String pathName = DirTool.combine(R.Paths.SysFile, sysFile.getRealPath());
        if (FileTool.isExist(pathName)) return;

        // 如果没有其他服务器，则无法补偿，则跳过transfer逻辑
        if (R.Caches.sysMachines.size() == 0) return;

        for (SysMachine machine : R.Caches.sysMachines.asMap().values()) {
            if (machine == null) continue;

            try {
                String url = machine.getApiUrl() + "/openapi/file/transfer/{serverSecretKey}/{cursorId}";
                url = url.replace("{serverSecretKey}", machine.getSecretKey());
                url = url.replace("{cursorId}", sysFileCursor.getId());
                // 先下载到临时目录
                String uuid = UUIDTool.get();
                String tempPathName = DirTool.combine(R.Paths.SysFileTemp, uuid);

                FileFetchTool.download(url, tempPathName, (downloadInfo) -> null);
                DirTool.create(FileTool.getPath(pathName));

                // 检查文件大小和MD5，匹配时才移动到正确位置
                java.io.File dest = new java.io.File(tempPathName);
                long size = dest.length();
                String md5 = FileCharCodeTool.md5(dest);
                if (size == sysFile.getSize() && sysFile.getMd5().equals(md5)) {
                    boolean fileMoveFlag = FileTool.move(tempPathName, pathName);
                    if (!fileMoveFlag) log.error("文件已下载，但是移动失败");
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    public java.io.File getIoFile(FileCursor sysFileCursor, File sysFile) {
        java.io.File file = null;
        if (sysFileCursor == null || sysFile == null) return file;
        try {
            try {
                FileDownloadLog log = new FileDownloadLog();
                log.setId(R.SnowFlake.nexts());
                log.setIp("");
                log.setCreateTime(LocalDateTime.now());
                log.setCursorId(sysFileCursor.getId());
                log.setFileName(sysFile.getName());
                log.setFileId(sysFile.getId());
                R.Queues.FileDownloadLogQueue.add(log);
            } catch (Exception ex) {
            }

            String pathName = DirTool.combine(R.Paths.SysFile, sysFile.getRealPath());

            log.info("下载文件路径检查：" + pathName);
            if (FileTool.isExist(pathName)) {
                file = new java.io.File(pathName);
            }
        } catch (Exception ex) {
            log.error(ExceptionTool.getStackTrace(ex));
        }
        return file;
    }
    //endregion
}
