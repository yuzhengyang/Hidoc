package com.yuzhyn.hidoc.app.application.controller4openapi;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("openapi/async")
public class AsyncApiController {
//
//    @GetMapping(value = "t1")
//    @ResponseBody
//    public WebAsyncTask<String> webAsyncReq() {
//        System.out.println("外部线程：" + Thread.currentThread().getName());
//        Callable<String> result = () -> {
//            System.out.println("内部线程开始：" + Thread.currentThread().getName());
//            try {
//                TimeUnit.SECONDS.sleep(4);
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//            log.info("副线程返回");
//            System.out.println("内部线程返回：" + Thread.currentThread().getName());
//            return "success";
//        };
//        WebAsyncTask<String> wat = new WebAsyncTask<String>(3000L, result);
//        wat.onTimeout(new Callable<String>() {
//
//            @Override
//            public String call() throws Exception {
//                // TODO Auto-generated method stub
//                return "超时";
//            }
//        });
//        return wat;
//    }

    public static void main(String[] args) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) {
                emitter.onNext("Hello Uncle Xing");
                emitter.onComplete();
            }
        }).subscribe(new Observer<String>() {

            @Override
            public void onSubscribe(@NotNull Disposable disposable) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(@NotNull String s) {
                System.out.println("onNext");
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
