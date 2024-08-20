package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_flow_log")
public class SysFlowLog {
    @TableId("id")
    private String id;
    /**
     * 时间
     */
    public Long time;
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

}
