package com.yuzhyn.hidoc.app.application.entity.serverman;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("server_man_exe_log")
public class ServerManExeLog {
    private String id;
    private String cmdId;
    private String machineId;
    private String runUserId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String dialogId;
    private String contentTa;
    private String contentTb;
    private String contentTc;
    private String resultTa;
    private String resultTb;
    private String resultTc;

}
