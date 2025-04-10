package com.yuzhyn.hidoc.app.application.model.serverman;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CmdRunLog {
    private String dialogId;
    private byte[] textBytes;
    private LocalDateTime createTime;

    public CmdRunLog(String id, byte[] bt) {
        this.dialogId = id;
        this.textBytes = bt;
        this.createTime = LocalDateTime.now();
    }
}
