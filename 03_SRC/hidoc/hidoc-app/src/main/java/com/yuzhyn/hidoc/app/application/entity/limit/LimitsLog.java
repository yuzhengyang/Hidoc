package com.yuzhyn.hidoc.app.application.entity.limit;

import lombok.Data;

@Data
public class LimitsLog {
    /**
     * 限制模型ID
     */
    private String limitsId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户信息
     * 其他多用信息
     */
    private String userInfo;
    /**
     * 是否允许
     */
    private Boolean isAllow;
    /**
     * 剩余剂量
     */
    private Long residueDose;
}
