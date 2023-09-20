package com.yuzhyn.hidoc.app.application.entity.file;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视图查询
 */
@Data
public class ShareFileView {
    private String fileName;
    private String userId;
    private String realName;
    private LocalDateTime createTime;
    private LocalDateTime expiryTime;
    private String uname;
    private LocalDateTime downloadTime;
    private Long downloadCount;
    private String ext;
    private Long size;
}
