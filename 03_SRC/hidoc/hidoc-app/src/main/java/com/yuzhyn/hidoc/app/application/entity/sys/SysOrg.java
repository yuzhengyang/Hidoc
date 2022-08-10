package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户组织的构思存在较多约束和关联，管理方式不明朗
 */
@Data
@TableName("sys_org")
public class SysOrg {

    @TableId("id")
    private String id;
    /**
     * 名称（组织名称）
     */
    private String name;
    /**
     * 描述（描述组织概况）
     */
    private String description;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 冻结时间
     */
    private LocalDateTime frozenTime;
    /**
     * 是否冻结（冻结后，组织不能进入，组织内数据的所有动作均应禁止）
     */
    private boolean isFrozen;
    /**
     * 自动加入邮箱前缀（必须搭配后缀使用）
     */
    private String joinEmailPrefix;
    /**
     * 自动加入邮箱后缀
     */
    private String joinEmailSuffix;
    /**
     * 加入密码
     */
    private String joinPassword;
    /**
     * 自由加入
     */
    private boolean joinFree;
    /**
     * 名额限制
     */
    private String joinQuotaLimit;
    /**
     * 已用名额
     */
    private String joinQuotaUsed;
    /**
     * 创建用户
     */
    private String createUserId;
    /**
     * 更新用户
     */
    private String updateUserId;

}
