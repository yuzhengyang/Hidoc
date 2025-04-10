package com.yuzhyn.hidoc.app.application.entity.serverman;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServerManOutput {
    private String id;
    private String dialogId;
    private Long serialNumber;
    private LocalDateTime createTime;
    private String output;
}
