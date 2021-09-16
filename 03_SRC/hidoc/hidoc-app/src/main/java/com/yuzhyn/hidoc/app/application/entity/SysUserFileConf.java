package com.yuzhyn.hidoc.app.application.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("sys_user_file_conf")
public class SysUserFileConf {

    @TableId("user_id")
    private String userId;
    private LocalDateTime createTime;
    private LocalDateTime expiryTime;
    private Long spaceLimit;
    private Long usedSpace;
    private String urlPrefix;

    @TableField(exist = false)
    private Long spaceUsage;
}
