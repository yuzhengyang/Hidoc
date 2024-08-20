package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_access_log")
public class SysFlowRule {
    @TableId("id")
    private String id;
    /**
     * 访问链接：/api/xxx
     */
    public String url;
    /**
     * 访问角色：访客、用户、管理员
     */
    public String role;
    /**
     * 访问用户
     */
    public String user;
    /**
     * 访问方法：GET、POST、PUT、DELETE
     */
    public String method;
    /**
     * 访问控制方式：按照角色、按照用户、混合
     */
    public String mode;
    /**
     * 时间期间：秒
     */
    public int duration;
    /**
     * 访问次数
     */
    public int count;

    public SysFlowRule() {
        super();
    }

    public SysFlowRule(String url, String role, String user, String method, String mode, int duration, int count) {
        this.url = url;
        this.role = role;
        this.user = user;
        this.method = method;
        this.mode = mode;
        this.duration = duration;
        this.count = count;
    }
}
