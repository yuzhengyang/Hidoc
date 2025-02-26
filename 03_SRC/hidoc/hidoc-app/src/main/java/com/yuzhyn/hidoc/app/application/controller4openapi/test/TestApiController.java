package com.yuzhyn.hidoc.app.application.controller4openapi.test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.azylee.core.threads.sleeps.Sleep;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import com.yuzhyn.hidoc.app.application.service.test.TestService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("x-openapi/test")
public class TestApiController {


//    @Autowired
//    SysAccessLogMapper sysAccessLogMapper;
//
//    @Autowired
//    TestService testService;
//
//    @Autowired
//    private DataSourceTransactionManager transactionManager;
//
//    @GetMapping("stream")
//    public ResponseData streamQueryByCust() {
//        // 记录开始时间
//        LocalDateTime startTime = LocalDateTime.now();
//        System.out.println("开始时间：" + startTime);
//
//        sysAccessLogMapper.selectAllLog(new HashMap<>(), resultContext -> {
//            SysAccessLog h2User = resultContext.getResultObject();
//            System.out.println("当前处理第" + resultContext.getResultCount() + "条记录");
//            System.out.println("当前处理数据，主键：" + h2User.getId());
//        });
//
//        // 记录结束时间和持续时间
//        LocalDateTime endTime = LocalDateTime.now();
//        System.out.println("结束时间：" + endTime);
//        System.out.println("持续时间：" + Duration.between(startTime, endTime).toMillis() + "毫秒");
//
//        ResponseData responseData = ResponseData.ok();
//        return responseData;
//    }
//
//    @GetMapping("cursor")
//    public ResponseData cursorQueryByCust() {
//        // 记录开始时间
//        LocalDateTime startTime = LocalDateTime.now();
//        System.out.println("开始时间：" + startTime);
//
//        // 创建事务定义
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setName("ManualTransaction");
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//
//        // 获取事务状态
//        TransactionStatus status = transactionManager.getTransaction(def);
//        try {
//            int count = 0;
//            Cursor<SysAccessLog> cursor = sysAccessLogMapper.selectAllLog(new HashMap<>());
//            if (cursor != null) {
//                for (SysAccessLog user : cursor) {
//                    System.out.println("当前处理第 " + (++count) + " 条记录，主键为：" + user.getId());
//                }
//            }
//            // 提交事务
//            transactionManager.commit(status);
//            System.out.println("事务提交成功");
//        } catch (Exception e) {
//            // 发生异常，回滚事务
//            transactionManager.rollback(status);
//            System.out.println("事务回滚成功");
//            e.printStackTrace();
//        }
//        // 记录结束时间和持续时间
//        LocalDateTime endTime = LocalDateTime.now();
//        System.out.println("结束时间：" + endTime);
//        System.out.println("持续时间：" + Duration.between(startTime, endTime).toMillis() + "毫秒");
//
//        ResponseData responseData = ResponseData.ok();
//        return responseData;
//    }
//
//    @GetMapping("select1")
//    public ResponseData select1() {
//        testService.streamQueryByPage();
//        ResponseData responseData = ResponseData.ok();
//        return responseData;
//    }
//
//    @GetMapping("select2")
//    public ResponseData select2() {
//        sysAccessLogMapper.selectList(Wrappers.emptyWrapper(), new ResultHandler<SysAccessLog>() {
//            int count = 0;
//
//            @Override
//            public void handleResult(ResultContext<? extends SysAccessLog> resultContext) {
//                SysAccessLog h2User = resultContext.getResultObject();
//                System.out.println("当前处理第" + (++count) + "条记录");
//            }
//        });
//
//        ResponseData responseData = ResponseData.ok();
//        return responseData;
//    }


//
//    @GetMapping("getUser")
//    public ResponseData getUser() {
//        String name = getName("key");
//        int age = getAge("key");
//        ResponseData responseData = ResponseData.ok();
//        responseData.putDataMap("name", name);
//        responseData.putDataMap("age", age);
//        return responseData;
//    }
//
//    @GetMapping("getUser2")
//    public ResponseData getUser2() {
////        System.out.println("main start ...");
////        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
////            System.out.println("开启异步任务1...");
////            int i = 10 / 2;
////            return i;
////        });
////        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
////            System.out.println("开启异步任务2...");
////            return "hello";
////        });
////        future1.thenAcceptBothAsync(future2, (res1, res2) -> {
////            System.out.println("任务3 启动了.... 任务1的返回值：" + res1 + " 任务2的返回值：" + res2);
////        });
////        //        System.out.println("获取异步任务最终返回值：" + future.get());
////        System.out.println("main end ...");
//
//
//        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> getName("123"));
//        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> getAge("aaa"));
//        CompletableFuture<ResponseData> future3 = CompletableFuture.supplyAsync(() -> ResponseData.ok());
//
//        ResponseData responseData = ResponseData.ok();
//        future1.thenAcceptBothAsync(future2, (name, age) -> {
//            responseData.putDataMap("name", name);
//            responseData.putDataMap("age", age);
//        });
//        return responseData;
//    }
//
//    @GetMapping(path = "gettasks")
//    private CompletableFuture<ResponseData> getNewTasks() {
//        CompletableFuture<ResponseData> future1 = CompletableFuture.supplyAsync(() -> getName("123"));
//        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> getAge("aaa"));
//        future1.thenAcceptBothAsync(future2, (name, age) -> {
//            responseData.putDataMap("name", name);
//            responseData.putDataMap("age", age);
//        });
//
//        future.complete(ResponseData.ok());
//        return future.thenApply(result -> result);
//    }
//
//    private String getName(String key) {
//        Sleep.s(2);
//        return "zhangsan";
//    }
//
//    private int getAge(String key) {
//        Sleep.s(2);
//        return 18;
//    }
//
//    @GetMapping(value = "flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<String> flux() {
//        Flux<String> flux = Flux.fromArray(new String[]{"小黑", "小胖", "小六", "一鑫"}).map(s -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return "二班：" + s;
//        });
//        return flux;
//    }
//
//    @GetMapping("hello")
//    public Mono<ResponseData> hello() {
//        long start = System.currentTimeMillis();
//        Mono<ResponseData> hello = Mono.fromSupplier(() -> getHelloStr());
//        log.info("openapi/test/hello: 接口耗时：" + (System.currentTimeMillis() - start));
//        return hello;
//    }
//
//    private ResponseData getHelloStr() {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return ResponseData.ok("hi hi");
//    }
}
