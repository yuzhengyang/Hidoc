package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("sys_machine")
public class SysMachine {

    /**
     * 机器ID
     */
    @TableId("machine_id")
    private String machineId;
    /**
     * 机器IP
     */
    private String ip;
    /**
     * 机器MAC
     */
    private String mac;
    /**
     * 机器数据中心ID
     */
    private int dataCenterId;
    /**
     * 机器工作ID
     */
    private int workerId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 密钥
     */
    private String secretKey;
    /**
     * 接口地址
     * 例如：https://www.yuzhyn.top/hidoc_server_api
     * 代码具体使用时，将拼接具体功能接口，如下：
     * https://www.yuzhyn.top/hidoc_server_api/openapi/app/timestamp
     */
    private String apiUrl;

}
