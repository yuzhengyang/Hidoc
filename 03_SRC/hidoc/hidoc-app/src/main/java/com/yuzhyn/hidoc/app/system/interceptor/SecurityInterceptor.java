package com.yuzhyn.hidoc.app.system.interceptor;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
import com.yuzhyn.hidoc.app.application.service.sys.SysUserLoginService;
import com.yuzhyn.hidoc.app.common.constant.UrlAccess;
import com.yuzhyn.hidoc.app.common.enums.ResponseCode;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.FlowManager;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Enumeration;

/**
 * 用户身份拦截器
 */
@Slf4j
public class SecurityInterceptor implements HandlerInterceptor {

    SysUserLoginService sysUserLoginService;

    public SecurityInterceptor(SysUserLoginService sysUserLoginService) {
        this.sysUserLoginService = sysUserLoginService;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub

        log.info("afterCompletion");
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub

        log.info("postHandle");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        // 检测LocalThread是否泄露
        try {
            boolean isLogin = CurrentUserManager.isLogin();
            if (isLogin) {
                SysUser user = CurrentUserManager.getUser();
                CurrentUserManager.clearCurrentUser();
                log.warn("LocalThread 泄露：" + user.getId() + ", " + user.getName() + "（强制清除泄露的内容）");
            }
        } catch (Exception ex) {
            log.error("LocalThread 泄露检查异常");
        }

        // 登录信息设置和使用处理
        boolean isLogin = false;
        String uri = request.getRequestURI();

        // 获取所有header信息
        log.info("解析并确认当前用户身份");
        try {
            Enumeration<String> headerNames = request.getHeaderNames();
            String accessOrigin = "";
            String accessHost = "";
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
//            log.info("header: " + name + " = " + value);
                if (name.equals("access-token")) {
                    UserInfo userInfo = sysUserLoginService.getUserLoginData(value);
                    if (userInfo != null) {

                        // 判断登录身份有效期
                        if (LocalDateTime.now().isBefore(userInfo.getExpiryTime())) {
                            isLogin = true;
                            CurrentUserManager.set(userInfo);
                            // 给用户续期有效期（登陆一次有效48小时）
                            userInfo.setExpiryTime(LocalDateTime.now().plusHours(48));
                            sysUserLoginService.updateUserLoginData(userInfo);
                        }
                        else {
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json; charset=utf-8");
                            ResponseData rs = new ResponseData(ResponseCode.LOGIN_TIMEOUT);
                            response.setContentLength(rs.toJSONString().getBytes().length);
                            response.getOutputStream().write(rs.toJSONString().getBytes());
                            return false;
                        }
                    }
                }
                if (name.equals("access-origin")) accessOrigin = value;
                if (name.equals("access-host")) accessHost = value;
            }
        } catch (Exception ex) {
            log.info("通过header获取信息失败：{}", ex.getMessage());
            log.error(ExceptionUtil.stacktraceToString(ex, 10));
        }

        if (isLogin) {
            log.info("已核对身份，用户信息已载入缓存");

            log.info("访问控制和流量控制过滤");
            if (!FlowManager.check(uri, CurrentUserManager.getUser())) {
                log.info("访问过于频繁，已拒绝请求");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                ResponseData rs = new ResponseData(ResponseCode.ACCESS_COUNT_MUCH);
                response.setContentLength(rs.toJSONString().getBytes().length);
                response.getOutputStream().write(rs.toJSONString().getBytes());
                return false;
            }

            return true;
        } else {

            // 过滤掉不需要权限验证的请求（OPTIONS）
            String method = CurrentUserManager.getRequestMethod();
            if (method != null && method.equals("OPTIONS")) {
                log.info("特殊请求方法，已过滤");
                return true;
            }

            // 允许已经标志允许的匿名链接
            if (UrlAccess.isAnonymous(uri)) {
                log.info("未登录用户，访问开放内容，已准许");
                return true;
            }

            log.info("未登录用户，访问受限内容，已拒绝");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            ResponseData rs = new ResponseData(ResponseCode.ACCESS_NEED_LOGIN);
            response.setContentLength(rs.toJSONString().getBytes().length);
            response.getOutputStream().write(rs.toJSONString().getBytes());
            return false;
        }
    }
}