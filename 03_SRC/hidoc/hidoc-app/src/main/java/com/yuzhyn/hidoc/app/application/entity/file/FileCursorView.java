package com.yuzhyn.hidoc.app.application.entity.file;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视图查询
 */
@Data
@TableName("file_cursor")
public class FileCursorView {
    private String id;
    private String bucketId;
    private String fileId;
    private String fileName;
    private String userId;
    private LocalDateTime createTime;
    private String version;
    private LocalDateTime expiryTime;
    private String collectedId;
    private String uname;
    private String historyCount;
    private String urlPrefix;
    private String bucketName;
}
