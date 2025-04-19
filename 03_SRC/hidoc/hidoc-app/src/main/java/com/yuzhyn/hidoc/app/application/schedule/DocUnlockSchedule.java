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
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
import com.yuzhyn.hidoc.app.application.service.file.FileService;
import com.yuzhyn.hidoc.app.application.service.sys.SysLockService;
import com.yuzhyn.hidoc.app.application.service.sys.SysUserLoginService;
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
public class DocUnlockSchedule {

    @Autowired
    private SysLockService sysLockService;

    @Autowired
    private DocLiteMapper docLiteMapper;

    @Autowired
    private SysUserLoginService sysUserLoginService;

    /**
     * 解锁锁定时间超过一天的文档
     * 处理一次等待8小时，多服务部署时，只允许一个服务执行
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void job() {
        String key = "DocUnlockSchedule";
        long expireSeconds = (8L * 3600L) - 100L;
        String lockKey = sysLockService.lock(key, expireSeconds, "");
        if (ObjectUtil.isEmpty(lockKey)) return;
        log.info(key + "：锁定成功：" + lockKey);
        try {
            List<DocLite> docList = docLiteMapper.selectList(new LambdaQueryWrapper<DocLite>()
                    .isNotNull(DocLite::getLockUserId)
                    .ne(DocLite::getLockUserId, ""));
            if (ObjectUtil.isEmpty(docList)) return;

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime unlockTime = now.minusDays(1);
            for (DocLite doc : docList) {
                List<UserInfo> userList = sysUserLoginService.getUserInfoList(doc.getLockUserId());
                // 如果有用户当前有效的登录信息，则不进行解锁
                if(!ObjectUtil.isEmpty(userList)) continue;

                // 如果用户已经不在线了，则开始判断时间，超过一天则解锁
                if (doc.getLockTime() == null || doc.getLockTime().isBefore(unlockTime)) {
                    doc.setLockUserId("");
                    doc.setLockTime(null);
                    docLiteMapper.updateById(doc);
                }
            }
        } catch (Exception e) {
        }
    }
}
