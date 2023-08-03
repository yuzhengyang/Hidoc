package com.yuzhyn.hidoc.app.application.entity.file;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.azylee.core.systems.bases.SystemType;
import com.yuzhyn.azylee.core.systems.bases.SystemTypeTool;
import com.yuzhyn.hidoc.app.aarg.R;
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

    /**
     * 是否删除
     */
    private Boolean isDelete;
    private LocalDateTime deleteTime;
    /**
     * 是否清理（删除n天之后才可以清理，如果这期间存在引用，则撤销删除状态）
     */
    private Boolean isClean;
    private LocalDateTime cleanTime;

    private LocalDateTime downloadTime;
    private Long downloadCount;

    private String checkStatus;
    private LocalDateTime checkTime;

    /**
     * 根据系统类型，返回处理后的路径
     *
     * @return
     */
    public String getRealPath() {
        switch (R.SystemType) {
            case Windows:
                return path.replace('/', '\\');
            case Linux:
            default:
                return path.replace('\\', '/');
        }
    }
}
