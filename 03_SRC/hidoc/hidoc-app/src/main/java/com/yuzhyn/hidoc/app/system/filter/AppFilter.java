package com.yuzhyn.hidoc.app.system.filter;

import com.alibaba.fastjson.JSONObject;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import com.yuzhyn.hidoc.app.system.wrapper.ResponseWrapper;
import com.yuzhyn.hidoc.app.utils.ClientIPTool;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.yuzhyn.azylee.core.datas.objects.Obj;
import com.yuzhyn.azylee.core.datas.objects.ObjectTool;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Component
public class AppFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        log.info("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Long beginTime = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CurrentUserManager.ip.set(ClientIPTool.getIp(request));
        String uri = URLDecoder.decode(request.getRequestURI(), "utf-8");
        String method = request.getMethod();
        CurrentUserManager.setRequestMethod(method);
        log.info("AppFilter 过滤器处理: " + uri + " ,method: " + method);

        // *************************************************************************************
        // *************************************************************************************
        ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, wrapper);

        // *************************************************************************************
        // *************************************************************************************


        // *************************************************************************************
        // *************************************************************************************

        Long runTime = System.currentTimeMillis() - beginTime;
        log.info("请求执行时长：" + runTime + " ms");

        // 处理header数据
        Collection<String> headers = ((HttpServletResponse) servletResponse).getHeaderNames();
        wrapper.setHeader("biz-op-time", runTime.toString());

        // 处理response数据（依据返回数据类型）
        String contentType = Obj.op(servletResponse.getContentType(), "");
        byte[] responseData = wrapper.getResponseData();
        switch (contentType) {
            case "application/json;charset=UTF-8":
                // 对json数据进行处理，解析-处理-转换
                String result = new String(responseData, servletResponse.getCharacterEncoding());
                JSONObject jsonObject = JSONObject.parseObject(result);
                jsonObject.put("biz-op-time", runTime);
                result = jsonObject.toString();
                responseData = result.getBytes();
                break;
            case "application/octet-stream":
            default:
                break;
        }

        // 写出最终response数据
        servletResponse.setContentLength(responseData.length);
        servletResponse.getOutputStream().write(responseData);

//        // 记录请求日志
//        if (method.toUpperCase().equals("POST") || method.toUpperCase().equals("GET")) {
//            SysAccessLog sysAccessLog = new SysAccessLog();
//            sysAccessLog.setId(R.SnowFlake.nexts());
//            sysAccessLog.setIp(CurrentUserManager.ip.get());
//            sysAccessLog.setCreateTime(LocalDateTime.now());
//            sysAccessLog.setUri(uri);
//            sysAccessLog.setMethod(method);
//            sysAccessLog.setElapsedTime(runTime);
//            if (CurrentUserManager.isLogin()) {
//                sysAccessLog.setUserId(CurrentUserManager.getUser().getId());
//            }
//            R.Queue.SysAccessLogQuene.add(sysAccessLog);
//            log.info("* 记录访问日志：" + uri + ", " + method + ", |   " + ObjectTool.optional(sysAccessLog.getUserId(), ""));
//        }

        // 清空LocalThread，防止内存泄露
        CurrentUserManager.clearCurrentUser();
    }

    @Override
    public void destroy() {
        log.info("过滤器销毁");
    }
}