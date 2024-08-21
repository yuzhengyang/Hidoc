package com.yuzhyn.hidoc.app.application.entity.javadoc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "java_doc_meta")
public class JavaDocMetaLite {

    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String createUserId;
    private LocalDateTime createTime;
    private Boolean isStruct;
    private String metaType;

    private String classId;
    private String className;
    private String projectId;
    private String projectName;

    private String packageInfo;

    private String name;
    private String qualifier;
    private String returnType;
    private String returnDesc;
    private String params;
    private String throwses;

    private String commentInfo;
    private String commentScene;
    private String commentLimit;
    private String commentExample;
    private String commentLog;
    private String commentKeywords;
    private String commentMenu;

    // 以下是运行时扩展字段，不存储

    @TableField(exist = false)
    private String[] _highlightKeys = null;

    @TableField(exist = false)
    private List<JavaDocMeta> javaDocMethodList;

    @TableField(exist = false)
    private JSONObject javaDocClassLite;


}
