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

    private Boolean isDelete;
    private String deleteUserId;
    private LocalDateTime deleteTime;
    /**
     * 释放状态
     * 进入释放流程后，不允许从删除状态还原
     * 但是如果上传同一指纹的文件，可以从删除状态撤回（状态1和2可以）
     *
     * 状态改变间隔时间为：10天
     * 1-计划释放（可逆），2-进入释放任务（可逆），3-准备执行释放（不可逆），4、释放完成（不可逆）
     */
    private Long releaseStatus;
    private LocalDateTime releaseTime;
}
