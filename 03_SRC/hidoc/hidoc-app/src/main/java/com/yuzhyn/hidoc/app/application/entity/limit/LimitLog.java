package com.yuzhyn.hidoc.app.application.entity.limit;

import lombok.Data;

@Data
public class LimitLog {
    /**
     * 限制模型类型
     * access-准许模型，dose-计量模型
     */
    private String limitType;
    /**
     * 限制模型ID
     */
    private String limitId;
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
     * 返回状态
     * u-未启用，e-规则过期，n-禁止通过，y-允许通过
     */
    private String resultStatus;
    /**
     * 剩余剂量
     */
    private Long residueDose;
}
