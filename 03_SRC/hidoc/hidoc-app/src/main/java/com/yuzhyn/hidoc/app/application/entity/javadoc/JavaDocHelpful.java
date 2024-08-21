package com.yuzhyn.hidoc.app.application.entity.javadoc;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "java_doc_helpful")
public class JavaDocHelpful {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String userId;
    private LocalDateTime createTime;

    private String metaId;
    private String classId;
    private String projectId;
    private Double helpfulRate;

    public JavaDocHelpful() {
        super();
    }

    public JavaDocHelpful(String metaId, String classId, String projectId, Double helpfulRate) {
        this.userId = CurrentUserManager.getUser() != null ? CurrentUserManager.getUser().getId() : "*";
        this.createTime = LocalDateTime.now();
        this.metaId = metaId;
        this.classId = classId;
        this.projectId = projectId;
        this.helpfulRate = helpfulRate;
        this.id = this.userId + "-" + this.metaId;
    }
}
