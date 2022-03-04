package com.yuzhyn.hidoc.app.application.model.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.objects.Obj;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class SystemStatus {

    //2021-01-28 23:59:03|58|58|14|4024815|43740103|0|62943

    private LocalDateTime startTime;
    private LocalDateTime stopTime;
    private Long time;
    private Long afk;
    private Integer cpu;
    private Long ram;
    private Long disk;
    private Integer appCpu;
    private Long appRam;

    public SystemStatus() {
        this.startTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        this.startTime = Obj.op(this.startTime, LocalDateTime.now());
        this.stopTime = Obj.op(this.stopTime, LocalDateTime.now());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateTimeFormat.toStr(startTime));
        stringBuilder.append("|");
        stringBuilder.append(Duration.between(startTime, stopTime).getSeconds());
        stringBuilder.append("|");
        stringBuilder.append(Duration.between(startTime, stopTime).getSeconds());
        stringBuilder.append("|");
        stringBuilder.append(Obj.op(cpu, 0));
        stringBuilder.append("|");
        stringBuilder.append(Obj.op(ram, 0));
        stringBuilder.append("|");
        stringBuilder.append(Obj.op(disk, 0));
        stringBuilder.append("|");
        stringBuilder.append(Obj.op(appCpu, 0));
        stringBuilder.append("|");
        stringBuilder.append(Obj.op(appRam, 0));
        return stringBuilder.toString();
    }
}
