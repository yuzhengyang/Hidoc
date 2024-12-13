package com.yuzhyn.hidoc.app.application.entity.card;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "card_user")
public class CardUser {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String cardId;

    private String levelIds;

    /**
     * 用户邮箱/账号
     */
    private String userEmail;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 特征码：用于区分设备，锁定使用设备数，为空是则不限制使用设备
     * 限制示例：[win11-zhangsan][win7-zhangsan]
     */
    private String signatures;
    /**
     * 盐值
     */
    private String salt;

    /**
     * 描述
     */
    private String description;
    /**
     * 创建模式：0-自助申请，1-管理员添加
     */
    private String createMode;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUserId;
    private String updateUserId;
    private Boolean isEnable;

}