package com.yuzhyn.hidoc.app.application.controller4openapi.test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import com.yuzhyn.hidoc.app.application.service.test.TestService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * 这个是测试的查询数据使用流式查询和游标查询
 */
@Slf4j
@RestController
@RequestMapping("x-openapi/testquery")
public class TestQueryApiController {


    @Autowired
    SysAccessLogMapper sysAccessLogMapper;

    @Autowired
    TestService testService;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @GetMapping("stream")
    public ResponseData streamQueryByCust() {
        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("开始时间：" + startTime);

        sysAccessLogMapper.selectAllLog(new HashMap<>(), resultContext -> {
            SysAccessLog h2User = resultContext.getResultObject();
            System.out.println("当前处理第" + resultContext.getResultCount() + "条记录");
            System.out.println("当前处理数据，主键：" + h2User.getId());
        });

        // 记录结束时间和持续时间
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("结束时间：" + endTime);
        System.out.println("持续时间：" + Duration.between(startTime, endTime).toMillis() + "毫秒");

        ResponseData responseData = ResponseData.ok();
        return responseData;
    }

    @GetMapping("cursor")
    public ResponseData cursorQueryByCust() {
        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("开始时间：" + startTime);

        // 创建事务定义
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("ManualTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // 获取事务状态
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int count = 0;
            Cursor<SysAccessLog> cursor = sysAccessLogMapper.selectAllLog(new HashMap<>());
            if (cursor != null) {
                for (SysAccessLog user : cursor) {
                    System.out.println("当前处理第 " + (++count) + " 条记录，主键为：" + user.getId());
                }
            }
            // 提交事务
            transactionManager.commit(status);
            System.out.println("事务提交成功");
        } catch (Exception e) {
            // 发生异常，回滚事务
            transactionManager.rollback(status);
            System.out.println("事务回滚成功");
            e.printStackTrace();
        }
        // 记录结束时间和持续时间
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("结束时间：" + endTime);
        System.out.println("持续时间：" + Duration.between(startTime, endTime).toMillis() + "毫秒");

        ResponseData responseData = ResponseData.ok();
        return responseData;
    }
}
