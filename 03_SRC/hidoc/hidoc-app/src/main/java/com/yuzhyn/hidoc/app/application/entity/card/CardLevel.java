package com.yuzhyn.hidoc.app.application.entity.card;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "card_level")
public class CardLevel {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String cardId;

    private String name;
    private String description;
    private String dataPackage;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUserId;
    private String updateUserId;
    private Integer memberCount;
    private Boolean isEnable;

    private Boolean isAllowApply;
    private String allowEmail;
    private String allowEmailSuffix;

    /**
     * 锁定用户ID，用来锁定对资源包的修改或类似功能的应用
     * 如：锁定后只能由当前用户操作资源修改
     */
    private String lockUserId;
    /**
     * 锁定时间，可自行计算做不同逻辑处理
     */
    private LocalDateTime lockTime;
    private int lockDuration;
    private long lockVersion;
    private String lockKey;
}