package com.yuzhyn.hidoc.app.application.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_file_bucket")
public class SysFileBucket {
    private String id;
    private String userId;
    private String name;
    private Boolean isOpen;
}
