package com.yuzhyn.hidoc.app.application.entity;

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

    @TableId("machine_id")
    private String machineId;
    private String ip;
    private String mac;
    private int dataCenterId;
    private int workerId;

}
