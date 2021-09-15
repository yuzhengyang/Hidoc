package com.yuzhyn.hidoc.app.application.internal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_file")
public class SysFile {
    private String id;
    private String name;
    private String ext;
    private Long size;
    private String path;
    private LocalDateTime createTime;
    private String userId;
    private String md5;
    private String sha1;
}
