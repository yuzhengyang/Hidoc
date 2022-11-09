package com.yuzhyn.hidoc.app.application.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.azylee.core.ios.txts.PropertyTool;
import com.yuzhyn.azylee.core.ios.txts.TxtTool;
import com.yuzhyn.azylee.core.systems.bases.SystemStatusTool;
import com.yuzhyn.azylee.core.systems.models.SystemStatusInfo;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManExeLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysStatusLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManExeLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysStatusLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserFileConfMapper;
import com.yuzhyn.hidoc.app.application.model.serverman.CmdRunLog;
import com.yuzhyn.hidoc.app.utils.ssh.SshClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class FileCleanSchedule {

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileCursorMapper fileCursorMapper;

    @Autowired
    SysUserFileConfMapper sysUserFileConfMapper;

    @Scheduled(cron = "0 */30 * * * ?")
    public void job() {

        // 撤销file的删除状态
        // 查询已删除状态尚未清理的文件，校验是否还存有引用关系，如还存在引用关系，则撤销删除状态
        {
            // 条件：is_clean != true AND is_delete = true
            List<File> deleteFiles = fileMapper.selectList(new LambdaQueryWrapper<File>()
                    .ne(File::getIsClean, true).eq(File::getIsDelete, true));
            if (ListTool.ok(deleteFiles)) {
                for (File item : deleteFiles) {
                    // 条件：file_id = file.id AND is_delete != true
                    Long cursorCount = fileCursorMapper.selectCount(new LambdaQueryWrapper<FileCursor>()
                            .eq(FileCursor::getFileId, item.getId()).ne(FileCursor::getIsDelete, true));
                    if (cursorCount > 0) {
                        log.info("文件：" + item.getName() + " （" + item.getId() + "）仍存在未删除的引用，撤销删除状态");
                        item.setIsDelete(false);
                        fileMapper.updateById(item);
                    }
                }
            }
        }

        // 清理物理文件
        // 查询已删除10天的文件，直接清理释放空间
        {
            LocalDateTime daysAgo = LocalDateTime.now().minusDays(10);
            // 条件：is_clean != true AND is_delete = true AND delete_time <= 10天前
            List<File> deleteFiles = fileMapper.selectList(new LambdaQueryWrapper<File>()
                    .ne(File::getIsClean, true).eq(File::getIsDelete, true).le(File::getDeleteTime, daysAgo));
            if (ListTool.ok(deleteFiles)) {
                for (File item : deleteFiles) {
                    String pathName = DirTool.combine(R.Paths.SysFile, item.getRealPath());
                    log.info("即将清理的文件：" + pathName);
                    boolean cleanFlag = FileTool.delete(pathName);
                    if (cleanFlag) {
                        item.setIsClean(true);
                        item.setCleanTime(LocalDateTime.now());
                        fileMapper.updateById(item);
                        log.info("清理文件：" + pathName + " 成功，已保存清理状态和时间信息");
                    }
                }
            }
        }

        // 标记file删除状态
        // 查询超过190天没有引用的文件，并标记为删除状态
        // 标记file为删除状态后，释放用户的使用空间
        {
            LocalDateTime daysAgo = LocalDateTime.now().minusDays(190);
            List<File> fileList = fileMapper.selectNoCursorFileList(daysAgo);
            if (ListTool.ok(fileList)) {
                for (File item : fileList) {
                    item.setIsDelete(true);
                    item.setDeleteTime(LocalDateTime.now());
                    fileMapper.updateById(item);
                }
            }
        }


        // 根据下载时间标记删除状态（cursor）
        // 查询文件后缀，根据超期为下载使用规则，标记未删除状态
        {
            Map<String, Tuple2<Integer, Long>> deleteRuleMap = new HashMap<>();
            deleteRuleMap.put("json", Tuples.of(365, 10 * 1024 * 1024L));
            deleteRuleMap.put("udp", Tuples.of(365, 10 * 1024 * 1024L));
        }
    }
}
