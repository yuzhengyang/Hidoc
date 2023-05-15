package com.yuzhyn.hidoc.app.application.entity.file;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视图查询
 */
@Data
public class HidocFileView {
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
    private Boolean isDelete;
    private String deleteUserId;
    private LocalDateTime deleteTime;
    private LocalDateTime downloadTime;
    private Long downloadCount;
    private String collectedName;
    private String collectedOwnerName;
    private Long size;
}
