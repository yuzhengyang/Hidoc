package com.yuzhyn.hidoc.app.application.entity.javadoc;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "java_doc_query_log")
public class JavaDocQueryLog {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String traceId;
    private String userId;
    private LocalDateTime createTime;
    private String queryType;
    private String queryValue;

    public JavaDocQueryLog() {
        super();
    }

    public JavaDocQueryLog(String traceId, String queryType, String queryValue) {
        this.id = R.SnowFlake.nexts();
        this.traceId = traceId;
        this.userId = CurrentUserManager.getUser() != null ? CurrentUserManager.getUser().getId() : "";
        this.createTime = LocalDateTime.now();
        this.queryType = queryType;
        this.queryValue = queryValue;
    }
}
