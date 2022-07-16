package com.yuzhyn.hidoc.app.application.entity.doc;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.azylee.core.datas.datetimes.RelativeDateFormat;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author inc
 */
@Data
@TableName("doc")
public class DocLiteOrder {

    @TableId("id")
    private String id;
    private String collectedId;
    private Integer serialNumber;

    private String createUserId;
    private Boolean isDelete;
    private String parentDocId;

    private LocalDateTime createTime;
}
