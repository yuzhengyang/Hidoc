//package com.yuzhyn.hidoc.app.application.schedule;
//
//import cn.hutool.core.exceptions.ExceptionUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.yuzhyn.hidoc.app.aarg.R;
//import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
//import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
//import com.yuzhyn.hidoc.app.application.mapper.doc.DocMapper;
//import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLoginMapper;
//import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
//import com.yuzhyn.hidoc.app.application.service.sys.SysUserLoginService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Slf4j
//@Component
//@EnableScheduling
//@EnableAsync
//public class UserLoginInfoSchedule {
//
//    @Autowired
//    SysUserLoginMapper sysUserLoginMapper;
//
//    @Autowired
//    DocLiteMapper docLiteMapper;
//
//    @Autowired
//    SysUserLoginService sysUserLoginService;
//
//    /**
//     * 用户登录信息持久化处理
//     * 1.定期将过期1小时的用户信息从缓存删除
//     * 2.定期将用户登录信息写入数据库
//     */
//    @Async
//    @Scheduled(cron = "0 */1 * * * ?")
//    public void job() {
//        try {
//
//            // 定义检查过期时间的点为一小时之前
//            final LocalDateTime checkTime = LocalDateTime.now().minusHours(1);
//
//            // 清理已经过期的登录用户信息
//            for (String key : R.Caches.UserInfo.asMap().keySet()) {
//                UserInfo userInfo = R.Caches.UserInfo.getIfPresent(key);
//                if (userInfo != null) {
//                    final LocalDateTime expiryTime = userInfo.getExpiryTime();
//                    if (expiryTime != null && expiryTime.isBefore(checkTime)) {
//                        // 过期从缓存中失效，并且从数据库删除
//                        sysUserLoginService.deleteUserLoginData(key);
//                    }
//                }
//            }
//
//            // 解锁已经下线的用户锁定的文档
////        List<DocLite> docLiteList = docLiteMapper.selectList(new LambdaQueryWrapper<DocLite>().isNotNull(DocLite::getLockUserId));
//
//            // 保存登录信息到数据库中
//            for (String key : R.Caches.UserInfo.asMap().keySet()) {
//                UserInfo userInfo = R.Caches.UserInfo.getIfPresent(key);
//                if (userInfo != null) {
//                    // 保存登录信息到数据库中，先删除后插入
//                    sysUserLoginMapper.deleteById(userInfo.getToken());
//                    sysUserLoginMapper.insert(userInfo.getSysUserLogin());
//                }
//            }
//
//        } catch (Exception e) {
//            log.info("定时任务处理用户登录信息异常");
//            log.info(ExceptionUtil.stacktraceToString(e, 10));
//        }
//    }
//}
