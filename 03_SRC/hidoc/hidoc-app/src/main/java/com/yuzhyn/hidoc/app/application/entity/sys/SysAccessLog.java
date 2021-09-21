package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("sys_access_log")
public class SysAccessLog {
    private String id;
    private String ip;
    private LocalDateTime createTime;
    private String uri;
    private String method;
    private Long elapsedTime;
    private String userId;

    @TableField(exist = false)
    private String s1;
    @TableField(exist = false)
    private String s2;
    @TableField(exist = false)
    private String s3;
    @TableField(exist = false)
    private String s4;
    @TableField(exist = false)
    private String s5;
    @TableField(exist = false)
    private String s6;
    @TableField(exist = false)
    private String s7;
    @TableField(exist = false)
    private String s8;
    @TableField(exist = false)
    private String s9;
    @TableField(exist = false)
    private String s10;
    @TableField(exist = false)
    private String s11;
    @TableField(exist = false)
    private String s12;
    @TableField(exist = false)
    private String s13;
    @TableField(exist = false)
    private String s14;
    @TableField(exist = false)
    private String s15;
    @TableField(exist = false)
    private String s16;
    @TableField(exist = false)
    private String s17;
    @TableField(exist = false)
    private String s18;
    @TableField(exist = false)
    private String s19;
    @TableField(exist = false)
    private String s20;
}
