package com.yuzhyn.hidoc.app.application.entity.softsup;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "soft_sup_client")
public class SoftSupClient extends Model<SoftSupClient> {

    @TableId("id")
    private String id;
    private String baseId;
    private String clientType;
    private String name;
    private String email;
    private String ip;
    private String ipLocation;
    private String mapLocation;
    private String version;
    private Boolean isLimitAccess;
    private String forbidLimitId;

    private Long freeTime;
    private Long workTime;
    private Long freeTimePer;
    private Long workTimePer;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String lockVersion;
    private String remark;
}
