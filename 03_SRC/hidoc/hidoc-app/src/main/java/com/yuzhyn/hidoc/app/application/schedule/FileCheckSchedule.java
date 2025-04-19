package com.yuzhyn.hidoc.app.application.schedule;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileCharCodeTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.azylee.core.ios.txts.TxtTool;
import com.yuzhyn.azylee.core.threads.sleeps.Sleep;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.service.file.FileService;
import com.yuzhyn.hidoc.app.application.service.sys.SysLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class FileCheckSchedule {

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileCursorMapper fileCursorMapper;

    @Autowired
    FileService fileService;

    @Autowired
    SysLockService sysLockService;

    // 检查文件，检查一次文件间隔4小时
    @Scheduled(cron = "0 */1 * * * ?")
    public void job() {
        String key = "FileCheckSchedule-" + R.MachineId;
        long expireSeconds = (4L * 3600L) - 100L;
        String lockKey = sysLockService.lock(key, expireSeconds, "");
        if (ObjectUtil.isEmpty(lockKey)) {
            log.info(key + "：锁被占用");
            return;
        }
        log.info(key + "：锁定成功：" + lockKey);

        List<File> fileList = fileMapper.selectHealthyFileList();
        if (ListTool.ok(fileList)) {
            List<String> txts = new ArrayList<>();
            for (File item : fileList) {
                String yearMonthDir = DateTimeFormat.toStr(item.getCreateTime(), DateTimeFormatPattern.SHORT_YEAR_MONTH);
                String path = DirTool.combine(yearMonthDir, item.getId().toString());
                String target = DirTool.combine(R.Paths.SysFile, path);

                // 这里使用标志枚举的方式来校验内容，多位状态位
                // 第1位 - 是否存在
                // 第2位 - 文件大小匹配
                // 第3位 - md5匹配
                // 第4位 - sha1匹配
                char[] flag = new char[4];
                flag[0] = 'N';
                flag[1] = 'N';
                flag[2] = 'N';
                flag[3] = 'N';

                // 如果文件不存在，则去其他服务器下载，这里限制只能去下载1GB以内的文件
                long sizeLimit = 1024L * 1024L * 1024L;
                if (!FileTool.isExist(target) && item.getSize() < sizeLimit) {
                    List<FileCursor> cursorList = fileCursorMapper.selectList(new LambdaQueryWrapper<FileCursor>()
                            .eq(FileCursor::getFileId, item.getId())
                            .eq(FileCursor::getIsDelete, false));
                    if (ObjectUtil.isNotEmpty(cursorList)) {
                        try {
                            fileService.transfer(item, cursorList.get(0), "FileCheckSchedule");
                        } catch (Exception ex) {
                        }
                    }
                }

                // 这里只有存在文件才进行检查，否则不进行检查（没有文件时会有一大堆错误信息）
                if (FileTool.isExist(target)) {
                    java.io.File dest = new java.io.File(target);
                    String md5 = FileCharCodeTool.md5(dest);
                    String sha1 = FileCharCodeTool.sha1(dest);

                    if (dest.exists()) flag[0] = 'Y';
                    if (dest.length() == item.getSize()) flag[1] = 'Y';
                    if (item.getMd5().equals(md5)) flag[2] = 'Y';
                    if (item.getSha1().equals(sha1)) flag[3] = 'Y';
                }

                item.setCheckStatus(String.valueOf(flag));
                item.setCheckTime(LocalDateTime.now());
                fileMapper.updateById(item);

                txts.add(flag[0] + ", "
                        + flag[1] + ", "
                        + flag[2] + ", "
                        + flag[3] + ", "
                        + item.getId()
                        + ", "
                        + item.getPath()
                        + ", "
                        + item.getName());

                // 检查一个文件要等待一下，避免CPU占用高
                try{
                    Thread.sleep(10);
                }catch (Exception ex){}
            }

            // 写入到文件信息文件中（先排一下序）
            txts.sort(String::compareTo);
            FileTool.delete(R.Files.FilesInfo);
            for (String txt : txts) {
                TxtTool.append(R.Files.FilesInfo, txt);
            }
        }
    }
}
