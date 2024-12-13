package com.yuzhyn.hidoc.app.application.entity.card;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "card_user_log")
public class CardUserLog {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String cardId;
    private String levelId;
    private String userId;

    /**
     * 特征码
     */
    private String signature;

    private LocalDateTime createTime;
    private String createUserId;

}