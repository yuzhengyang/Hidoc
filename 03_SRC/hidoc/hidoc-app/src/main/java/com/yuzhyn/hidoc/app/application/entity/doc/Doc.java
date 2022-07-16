package com.yuzhyn.hidoc.app.application.entity.doc;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime lockTime;

    private String createUserId;
    private String updateUserId;
    private String lockUserId;
    private String ownerUserId;
    private Boolean isDelete;
    private String parentDocId;
    /**
     * 支持数
     */
    private Long supporterCount;
    /**
     * 反对数
     */
    private Long protesterCount;

    @TableField(exist = false)
    private Boolean isCurrentUserLock;


    @TableField(exist = false)
    private String label;

    @TableField(exist = false)
    private List<Doc> children;
}
