package com.yuzhyn.hidoc.app.application.customization.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("doc_collected_member")
public class DocCollectedMember {
    @TableId("id")
    private String id;
    private String collectedId;
    private String userId;
    private String createUserId;
    private Boolean allowEdit;
    private LocalDateTime createTime;
}
