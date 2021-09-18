package com.yuzhyn.hidoc.app.application.entity.file;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("file")
public class File {
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
