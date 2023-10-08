package com.yuzhyn.hidoc.app.application.entity.softsup;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "soft_sup_version")
public class SoftSupVersion extends Model<SoftSupVersion> {

    @TableId("id")
    private String id;
    private String baseId;
    private String version;
    private Boolean isNecessary;
    private String description;

    private String createUserId;
    private String updateUserId;
    private Boolean isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String urlFirst;
    private String urlSecond;
    private String urlThird;
    private String md5;
    private String releasePath;
}
