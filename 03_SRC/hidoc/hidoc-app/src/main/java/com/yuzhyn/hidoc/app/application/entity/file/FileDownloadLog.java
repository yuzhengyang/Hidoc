package com.yuzhyn.hidoc.app.application.entity.file;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("file_download_log")
public class FileDownloadLog {
    private String id;
    private String ip;
    private LocalDateTime createTime;
    private String cursorId;
    private String fileName;
    private String fileId;
}
