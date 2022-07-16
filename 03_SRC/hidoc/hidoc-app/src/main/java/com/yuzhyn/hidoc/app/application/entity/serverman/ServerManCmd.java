package com.yuzhyn.hidoc.app.application.entity.serverman;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("server_man_cmd")
public class ServerManCmd {
    private String id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUserId;
    private String updateUserId;
    private String ownerUserId;
    private String type;
    private Boolean isDelete;
    private String machineId;
    private String contentTa;
    private String contentTb;
    private String contentTc;


}
