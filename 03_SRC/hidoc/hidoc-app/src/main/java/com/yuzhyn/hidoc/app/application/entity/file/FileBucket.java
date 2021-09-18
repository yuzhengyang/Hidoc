package com.yuzhyn.hidoc.app.application.entity.file;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("file_bucket")
public class FileBucket {
    private String id;
    private String userId;
    private String name;
    private Boolean isOpen;
}
