package com.yuzhyn.hidoc.app.application.service;

import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.Doc;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
public class DocAccessLogService {

    public void createLog(Doc doc) {
        DocAccessLog log = new DocAccessLog();
        log.setId(R.SnowFlake.nexts());
        log.setCreateDate(LocalDate.now());
        log.setCreateTime(LocalDateTime.now());
        log.setIp(CurrentUserManager.ip.get());
        if (CurrentUserManager.isLogin()) {
            log.setUserId(CurrentUserManager.getUser().getId());
        } else {
            log.setUserId("");
        }
        log.setCollectedId(doc.getCollectedId());
        log.setDocId(doc.getId());
        log.setCreateUserId(doc.getCreateUserId());
        log.setOwnerUserId(doc.getOwnerUserId());
        R.Queue.DocAccessLogQuene.add(log);
    }
}
