package com.yuzhyn.hidoc.app.application.entity.doc;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("doc_access_log")
public class DocAccessLog {
    /**
     * 主键
     */
    private String id;
    /**
     * 访问ip
     */
    private String ip;
    /**
     * 创建日期（方便日汇总统计）
     */
    private LocalDate createDate;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 访问用户ID
     */
    private String userId;
    /**
     * 访问文集ID
     */
    private String collectedId;
    /**
     * 访问文档ID
     */
    private String docId;
    /**
     * 文档创建者ID
     */
    private String createUserId;
    /**
     * 文档所属者ID
     */
    private String ownerUserId;
}