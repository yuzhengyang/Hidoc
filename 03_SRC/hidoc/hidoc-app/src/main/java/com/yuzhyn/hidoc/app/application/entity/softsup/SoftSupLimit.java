package com.yuzhyn.hidoc.app.application.entity.softsup;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "soft_sup_limit")
public class SoftSupLimit extends Model<SoftSupLimit> {
    @TableId("id")
    private String id;
    private String baseId;
    private Boolean isEnable;
    private String name;
    private String limitType;
    private String limitContent;
    private String createUserId;
    private String updateUserId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
