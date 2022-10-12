//package com.yuzhyn.hidoc.app.application.controller4openapi;
//
//import com.yuzhyn.azylee.core.threads.sleeps.Sleep;
//import com.yuzhyn.hidoc.app.common.model.ResponseData;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@RestController
//@RequestMapping("openapi/test")
//public class TestApiController {
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
//}
