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
@TableName(value = "java_doc_class", autoResultMap = true)
public class JavaDocClass {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String projectId;

    private String version;
    private String createUserId;
    private LocalDateTime createTime;

    private String name;
    private String packageInfo;
    private String imports;
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONArray importsJson;
    private String qualifier;

    private String commentInfo;
    private String commentScene;
    private String commentLimit;
    private String commentExample;
    private String commentLog;

}
