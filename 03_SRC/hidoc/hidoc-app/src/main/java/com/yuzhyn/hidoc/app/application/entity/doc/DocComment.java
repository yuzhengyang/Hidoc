package com.yuzhyn.hidoc.app.application.entity.doc;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("doc_comment")
public class DocComment {
    /**
     * 评论唯一ID
     */
    @TableId("id")
    private String id;
    /**
     * 文档ID
     */
    private String docId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 内容
     */
    private String content;
    /**
     * 评论用户ID
     */
    private String createUserId;
    /**
     * 回复评论ID
     */
    private String replyCommentId;
    /**
     * 回复用户ID
     */
    private String replyUserId;
    /**
     * 文档拥有者已读
     */
    private Boolean isDocOwnerRead;
    private LocalDateTime docOwnerReadTime;
    /**
     * 回复用户已读
     */
    private Boolean isReplyUserRead;
    private LocalDateTime replyUserReadTime;

    private Boolean isDelete;

    @TableField(exist = false)
    private JSONObject createUser;

    @TableField(exist = false)
    private JSONObject  replyUser;
}
