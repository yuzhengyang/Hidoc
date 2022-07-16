package com.yuzhyn.hidoc.app.application.entity.javadoc;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "java_doc_class", autoResultMap = true)
public class JavaDocClassLite {

    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String projectId;
    private String projectName;

    private String createUserId;
    private LocalDateTime createTime;

    private String name;
    private String packageInfo;

    private String qualifier;

    private String commentInfo;
    private String commentScene;
    private String commentLimit;
    private String commentExample;
    private String commentLog;
    private String commentKeywords;

    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONArray commentLogJson;

    @TableField(exist = false)
    private String _class = "JavaDocClass";

    @TableField(exist = false)
    private String[] _highlightKeys = null;

    @TableField(exist = false)
    private List<JavaDocMethod> javaDocMethodList;

    private Boolean isStruct;

    private String commentMenu;
}
