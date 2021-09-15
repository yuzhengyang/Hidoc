package com.yuzhyn.hidoc.app.application.internal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("sys_file_download_log")
public class SysFileDownloadLog {
    private String id;
    private String ip;
    private LocalDateTime createTime;
    private String cursorId;
    private String fileName;
}
