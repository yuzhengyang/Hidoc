package com.yuzhyn.hidoc.app.application.entity.javadoc;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "java_doc_menu")
public class JavaDocMenu {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String menu;
    private String menuPath;
    private String parentId;
    private String projectId;
    private Integer level;


    @TableField(exist = false)
    private String label;

    @TableField(exist = false)
    private List<JavaDocMenu> children;
}
