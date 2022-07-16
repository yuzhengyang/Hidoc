package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUserLite {

    @TableId("id")
    private String id;

    @TableField("name")
    private String name;

    private String realName;

    private String avatar;

    private String email;

    private Boolean isFrozen;

    private LocalDateTime onlineTime;

    @TableField(exist = false)
    private String memberDesc;

    private Integer vipLevel;
}
