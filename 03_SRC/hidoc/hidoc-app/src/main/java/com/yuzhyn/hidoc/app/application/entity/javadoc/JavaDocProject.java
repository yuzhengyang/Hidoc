package com.yuzhyn.hidoc.app.application.entity.javadoc;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "java_doc_project")
public class JavaDocProject {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String name;
    private String description;
    private String teamsRead;
    private String teamsCode;

    private String token;

    private String createUserId;
    private String updateUserId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
