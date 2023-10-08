package com.yuzhyn.hidoc.app.application.entity.softsup;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "soft_sup_base")
public class SoftSupBase extends Model<SoftSupBase> {

    @TableId("id")
    private String id;
    private String name;
    private String description;
    private String token;

    private String createUserId;
    private String updateUserId;
    private String ownerUserId;
    private Boolean isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
