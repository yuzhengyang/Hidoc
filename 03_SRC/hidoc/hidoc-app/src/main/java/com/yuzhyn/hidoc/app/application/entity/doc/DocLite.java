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
public class DocLite {

    @TableId("id")
    private String id;
    private String collectedId;
    private String docType;
    private String title;
    private Long contentLength;
    private String contentType;
    private String tag;
    private Integer serialNumber;
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        this.relativeUpdateTime = RelativeDateFormat.format(this.getUpdateTime());
    }

    private String createUserId;
    private String updateUserId;
    private String lockUserId;
    private String ownerUserId;
    private Boolean isDelete;
    private String parentDocId;

    @TableField(exist = false)
    private SysUserLite ownerUser;

    @TableField(exist = false)
    private SysUserLite lockUser;

    @TableField(exist = false)
    private Boolean isCurrentUserLock;

    @TableField(exist = false)
    private String relativeUpdateTime;

    @TableField(exist = false)
    private String collectedName;

    @TableField(exist = false)
    private String label;

    @TableField(exist = false)
    private List<DocLite> children;
}
