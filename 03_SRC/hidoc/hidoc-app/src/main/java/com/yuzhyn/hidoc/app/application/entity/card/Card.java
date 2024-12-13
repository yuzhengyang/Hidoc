package com.yuzhyn.hidoc.app.application.entity.card;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "card")
public class Card {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;

    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
    private String createUserId;
    private String updateUserId;
    private String deleteUserId;
    private String ownerUserId;
    private Integer memberCount;
    private Boolean isDelete;

}