package com.yuzhyn.hidoc.app.application.internal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_file_cursor")
public class SysFileCursor {
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
}
