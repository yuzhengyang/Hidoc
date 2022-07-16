package com.yuzhyn.hidoc.app.application.entity.doc;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("doc_thumb")
public class DocThumb {

    /**
     * 组装主键，做唯一处理
     * 表名 + 记录ID + 用户ID
     * tableName + dataId + userId
     */
    @TableId("id")
    private String id;

    private String tableName;

    private String dataId;

    private String userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 支持
     */
    private Boolean isSupporter;
    /**
     * 反对
     */
    private Boolean isProtester;

    public static String genId(String _tableName, String _dataId, String _userId) {
        return _tableName + "__" + _dataId + "__" + _userId;
    }
}
