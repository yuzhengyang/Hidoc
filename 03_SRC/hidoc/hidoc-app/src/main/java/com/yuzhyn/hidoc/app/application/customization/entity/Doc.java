package com.yuzhyn.hidoc.app.application.customization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("doc")
public class Doc {

    @TableId("id")
    private String id;
    private String collectedId;
    private String docType;
    private String title;
    private String content;
    private Integer contentLength;
    private String contentType;
    private String tag;
    private Integer serialNumber;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUserId;
    private String updateUserId;
    private String lockUserId;
    private String ownerUserId;
    private Boolean isDelete;

    @TableField(exist = false)
    private Boolean isCurrentUserLock;

}
