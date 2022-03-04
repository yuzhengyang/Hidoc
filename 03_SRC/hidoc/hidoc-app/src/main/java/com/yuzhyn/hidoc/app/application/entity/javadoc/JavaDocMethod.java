package com.yuzhyn.hidoc.app.application.entity.javadoc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "java_doc_method", autoResultMap = true)
public class JavaDocMethod {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String classId;
    private String className;
    private String projectId;
    private String projectName;

    private String version;
    private String createUserId;
    private LocalDateTime createTime;

    private String name;
    private String qualifier;
    private String returnType;
    private String params;
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONArray paramsJson;

    private String commentInfo;
    private String commentScene;
    private String commentLimit;
    private String commentExample;
    private String commentLog;
    private String commentKeywords;

    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONArray commentLogJson;

    private String sourceCode;

    @TableField(exist = false)
    private String _class = "JavaDocMethod";

    @TableField(exist = false)
    private JSONObject javaDocClassLite;

    private Boolean isStruct;

    private String commentMenu;

}
