package com.yuzhyn.hidoc.app.application.entity.file;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("file_cursor")
public class FileCursor {
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
