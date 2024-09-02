package com.yuzhyn.hidoc.app.application.entity.javadoc;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.application.entity.team.TeamLite;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    // 以下是运行时扩展字段，不存储

    @TableField(exist = false)
    private List<TeamLite> teamsReadList;

    @TableField(exist = false)
    private List<TeamLite> teamsCodeList;
}
