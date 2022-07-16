package com.yuzhyn.hidoc.app.application.model.serverman;

import lombok.Data;

@Data
public class CmdRunLog {
    private String dialogId;
    private byte[] textBytes;

    public CmdRunLog(String id, byte[] bt) {
        this.dialogId = id;
        this.textBytes = bt;
    }
}
