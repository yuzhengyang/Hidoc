package com.yuzhyn.hidoc.app.application.entity.datacoll;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("data_coll_plan")
public class DataCollPlan {
    @TableId("id")
    private String id;
    private String name;
    private String description;
    private LocalDate startTime;
    private LocalDate stopTime;
    private Boolean isEnable;
    private Long dataCount;
    private String token;
    private String createUserId;
    private String ownerUserId;
    private Boolean isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
