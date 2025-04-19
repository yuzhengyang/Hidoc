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
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.service.file.FileService;
import com.yuzhyn.hidoc.app.application.service.sys.SysLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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
public class SysUserSleepSchedule {

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    SysLockService sysLockService;

    @Scheduled(cron = "0 0 */1 * * ?")
    public void job() {
        String key = "SysUserSleepSchedule";
        long expireSeconds = (8L * 3600L) - 100L;
        String lockKey = sysLockService.lock(key, expireSeconds, "");
        if (ObjectUtil.isEmpty(lockKey)) return;
        log.info(key + "：锁定成功：" + lockKey);

        // 获取当前时间，并计算180天之前的时间，获取180天之前登录的用户列表
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = now.minusDays(180);
        List<SysUserLite> userLiteList = sysUserLiteMapper.selectList(new LambdaQueryWrapper<SysUserLite>()
                .lt(SysUserLite::getLoginTime, before)
                .and(w -> w.isNull(SysUserLite::getIsSleep).or().eq(SysUserLite::getIsSleep, false)));
        if (ObjectUtil.isEmpty(userLiteList)) return;

        // 循环将用户标记为休眠状态，休眠状态的用户登录需要重新设置密码
        for (SysUserLite userLite : userLiteList) {
            try {
                userLite.setIsSleep(true);
                sysUserLiteMapper.updateById(userLite);
            } catch (Exception ex) {
            }
        }
    }
}
