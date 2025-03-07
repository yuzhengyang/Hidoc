package com.yuzhyn.hidoc.app.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 性能追踪工具类
 * 作者：一只叫煤球的猫
 * 链接：https://juejin.cn/post/7436567167728812044
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 * 使用方式
 *  try (PerfTracker.TimerContext ignored = PerfTracker.start()) {
 *         return userMapper.selectList(wrapper);
 *     }
 *
 * 📝 原因:
 * 业务代码和性能监控代码完全分离
 * try-with-resources 即使发生异常，close() 方法也会被调用，确保一定会记录耗时
 * 不需要手动管理计时的开始和结束
 * 更优雅
 */
@Slf4j
public class PerfTracker {
    private final long startTime;
    private final String methodName;

    private PerfTracker(String methodName) {
        this.startTime = System.currentTimeMillis();
        this.methodName = methodName;
    }

    public static TimerContext start() {
        return new TimerContext(Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    public static class TimerContext implements AutoCloseable {
        private final PerfTracker tracker;

        private TimerContext(String methodName) {
            this.tracker = new PerfTracker(methodName);
        }

        @Override
        public void close() {
            long executeTime = System.currentTimeMillis() - tracker.startTime;
            if (executeTime > 500) {
                log.warn("慢查询告警：方法 {} 耗时 {}ms", tracker.methodName, executeTime);
            }
        }
    }
}