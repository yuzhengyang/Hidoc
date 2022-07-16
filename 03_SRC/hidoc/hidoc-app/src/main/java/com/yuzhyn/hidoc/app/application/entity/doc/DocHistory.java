package com.yuzhyn.hidoc.app.application.entity.doc;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("doc_history")
public class DocHistory {

    @TableId("id")
    private String id;
    private String docId;
    private String collectedId;
    private String docType;
    private String title;
    private String content;
    private Integer contentLength;
    private String contentType;
    private String tag;
    private Integer serialNumber;
    private LocalDateTime createTime;
    private String createUserId;
    private String updateUserId;
}
