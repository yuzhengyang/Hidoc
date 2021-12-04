package com.yuzhyn.hidoc.app.application.entity.doc;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("doc_access_log")
public class DocAccessLog {
    private String id;
    private String ip;
    private LocalDate createDate;
    private LocalDateTime createTime;
    private String userId;
    private String collectedId;
    private String docId;
    private String createUserId;
    private String ownerUserId;
}