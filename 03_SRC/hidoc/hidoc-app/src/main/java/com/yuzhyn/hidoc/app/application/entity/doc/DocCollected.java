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

    /**
     * 需登录准入
     */
    private Boolean isLoginAccess;
    /**
     * 需密码准入
     */
    private String passwordAccess;

    /**
     * 是否模板文集（可作为模板引入内容）
     */
    private Boolean isTemplet;

    @TableField(exist = false)
    private List<DocLite> docLites;

    @TableField(exist = false)
    private List<SysUserLite> sysUserLites;

    @TableField(exist = false)
    private Long docTotal;

    @TableField(exist = false)
    private Boolean isCoop;

    /**
     * 格式化后的名称，用来做排序（格式掉Emoji等字符）
     */
    @TableField(exist = false)
    private String formatName;

    /**
     * 最新的文档更新时间
     */
    @TableField(exist = false)
    private LocalDateTime docLastUpdateTime;
}
