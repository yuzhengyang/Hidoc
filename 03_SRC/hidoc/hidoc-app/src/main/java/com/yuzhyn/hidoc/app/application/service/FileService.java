package com.yuzhyn.hidoc.app.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileBucket;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.file.FileDownloadLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import com.yuzhyn.hidoc.app.application.mapper.file.FileBucketMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileDownloadLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserFileConfMapper;
import com.yuzhyn.hidoc.app.utils.ClientIPTool;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
     * 检查用户文件空间是否够用
     *
     * @param userId
     * @param fileSize
     * @return
     */
    public boolean checkSpaceLimit(String userId, long fileSize) {
        SysUserFileConf conf = sysUserFileConfMapper.selectById(userId);
        if (null != conf && conf.getUsedSpace() < conf.getSpaceLimit() &&
                fileSize < (conf.getSpaceLimit() - conf.getUsedSpace())) {
            return true;
        }
        return false;
    }

    //region 文件上传功能函数

    /**
     * 上传文件
     *
     * @param collectedId
     * @param expiryTime
     * @param bucketName
     * @param files
     * @param curUser
     * @return
     */
    public List<FileCursor> uploadFiles(String collectedId, LocalDateTime expiryTime, String bucketName, MultipartFile[] files, SysUser curUser) {
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
                return sysFile;
            }
        }
        return null;
    }

    /**
     * 保存文件到磁盘
     *
     * @param file
     * @param sysFile
     * @return 操作状态（继续执行标志）、是否已有文件、已有文件信息
     */
    private Tuple3<Boolean, Boolean, File> saveToDisk(MultipartFile file, File sysFile) {
        try {
            // 存储文件到临时文件夹
            java.io.File dest = new java.io.File(DirTool.combine(R.Paths.SysFileTemp, sysFile.getId().toString()));
            file.transferTo(dest);
            sysFile.setMd5(FileCharCodeTool.md5(dest));
            sysFile.setSha1(FileCharCodeTool.sha1(dest));
            // 查询重复文件（依据文件特征码）
            File record = fileMapper.selectOne(new LambdaQueryWrapper<File>()
                    .eq(File::getMd5, sysFile.getMd5())
                    .eq(File::getSha1, sysFile.getSha1())
                    .eq(File::getSize, sysFile.getSize()));
            // 查询到已经有相同文件，重置文件实体类
            if (record != null) {
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
            FileBucket bucket = fileBucketMapper.selectOne(new LambdaQueryWrapper<FileBucket>()
                    .eq(FileBucket::getUserId, sysFile.getUserId())
                    .eq(FileBucket::getName, bucketName));
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
                int flag = fileCursorMapper.insert(cursor);
                if (flag > 0) {
                    // 一切保存成功后，刷新用户文件配额信息
                    SysUserFileConf conf = sysUserFileConfMapper.selectById(sysFile.getUserId());
                    if (conf != null) {
                        conf.setUsedSpace(conf.getUsedSpace() + sysFile.getSize());
                        sysUserFileConfMapper.updateById(conf);
                    }
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
            SysUserFileConf conf = sysUserFileConfMapper.selectOne(new LambdaQueryWrapper<SysUserFileConf>()
                    .eq(SysUserFileConf::getUrlPrefix, userPrefix));

            if (conf != null) {
                FileBucket bucket = fileBucketMapper.selectOne(new LambdaQueryWrapper<FileBucket>()
                        .eq(FileBucket::getUserId, conf.getUserId()).eq(FileBucket::getName, bucketName));

                if (bucket != null) {
                    List<FileCursor> cursorList = fileCursorMapper.selectList(new LambdaQueryWrapper<FileCursor>()
                            .eq(FileCursor::getUserId, conf.getUserId())
                            .eq(FileCursor::getBucketId, bucket.getId())
                            .eq(FileCursor::getFileName, fileName)
                            .orderByDesc(FileCursor::getVersion));
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
            FileCursor cursor = fileCursorMapper.selectOne(new LambdaQueryWrapper<FileCursor>()
                    .eq(FileCursor::getId, cursorId));
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
            FileCursor cursor = fileCursorMapper.selectOne(new LambdaQueryWrapper<FileCursor>()
                    .eq(FileCursor::getUname, uname));
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
     * 保存文件下载日志信息
     *
     * @param cursor
     * @param file
     * @param ip
     */
    public void saveDownloadLog(FileCursor cursor, File file, String ip) {
        if (cursor != null && file != null) {
            try {
                FileDownloadLog log = new FileDownloadLog();
                log.setId(R.SnowFlake.nexts());
                log.setIp(ip);
                log.setCreateTime(LocalDateTime.now());
                log.setCursorId(cursor.getId());
                log.setFileName(file.getName());
                fileDownloadLogMapper.insert(log);
            } catch (Exception ex) {
            }
        }
    }

    /**
     * 根据 文件指针信息和文件信息 下载文件
     *
     * @param sysFileCursor
     * @param sysFile
     * @param request
     * @param response
     */
    public void download(FileCursor sysFileCursor, File sysFile, HttpServletRequest request, HttpServletResponse response) {
        if (sysFileCursor != null && sysFile != null) {
            try {
                log.info("download log: " + ClientIPTool.getIp(request) + ", file: " + sysFile.getName());
//                saveDownloadLog(sysFileCursor, sysFile, ClientIPTool.getIp(request));

                String pathName = DirTool.combine(R.Paths.SysFile, sysFile.getPath());

                log.error("下载文件路径检查：" + pathName);
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
                }
            } catch (Exception ex) {
                log.error(ExceptionTool.getStackTrace(ex));
            }
        }
    }
    //endregion
}
