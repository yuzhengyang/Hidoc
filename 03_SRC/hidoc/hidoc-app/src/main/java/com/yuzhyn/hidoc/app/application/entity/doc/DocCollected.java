package com.yuzhyn.hidoc.app.application.entity.doc;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author inc
 */
@Data
@TableName("doc_collected")
public class DocCollected {
    @TableId("id")
    private String id;
    private String name;
    private String description;
    private Boolean isOpen;
    private String createUserId;
    private String ownerUserId;
    private LocalDateTime createTime;
    private Boolean isDelete;

    @TableField(exist = false)
    private List<DocLite> docLites;

    @TableField(exist = false)
    private List<SysUserLite> sysUserLites;

    @TableField(exist = false)
    private Long docTotal;

    @TableField(exist = false)
    private Boolean isCoop;
}
