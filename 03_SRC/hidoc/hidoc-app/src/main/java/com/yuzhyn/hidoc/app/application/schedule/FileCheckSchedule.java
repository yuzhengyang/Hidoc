package com.yuzhyn.hidoc.app.application.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileCharCodeTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.datacoll.DataColl;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserFileConfMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class FileCheckSchedule {

    @Autowired
    FileMapper fileMapper;

    @Scheduled(cron = "0 */1 * * * ?")
    public void job() {
        // 查询100个没有删除的并且没有进行检查或者最近1天没进行检查的文件
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
        List<File> fileList = fileMapper.selectNeedCheckFileList(localDateTime, 1000);
        if (ListTool.ok(fileList)) {
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

                java.io.File dest = new java.io.File(target);
                String md5 = FileCharCodeTool.md5(dest);
                String sha1 = FileCharCodeTool.sha1(dest);

                if (dest.exists()) flag[0] = 'Y';
                if (dest.length() == item.getSize()) flag[1] = 'Y';
                if (item.getMd5().equals(md5)) flag[2] = 'Y';
                if (item.getSha1().equals(sha1)) flag[3] = 'Y';

                item.setCheckStatus(String.valueOf(flag));
                item.setCheckTime(LocalDateTime.now());
                fileMapper.updateById(item);
            }
        }
    }
}
