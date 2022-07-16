package com.yuzhyn.hidoc.app.application.entity.file;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("file")
public class File {

    @ExcelIgnore
    private String id;

    @ExcelProperty("文件名称")
    private String name;

    @ExcelProperty("扩展名")
    private String ext;

    @ExcelProperty("文件大小")
    @NumberFormat("#.##%")
    private Long size;

    @ExcelProperty("路径")
    private String path;

    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private LocalDateTime createTime;

    @ExcelIgnore
    private String userId;

    @ExcelProperty(value = "MD5")
    private String md5;

    @ExcelProperty("SHA1")
    private String sha1;
}
