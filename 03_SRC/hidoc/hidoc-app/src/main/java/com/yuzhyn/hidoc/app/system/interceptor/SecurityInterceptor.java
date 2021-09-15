package com.yuzhyn.hidoc.app.system.interceptor;

import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.internal.entity.SysUser;
import com.yuzhyn.hidoc.app.application.internal.model.UserInfo;
import com.yuzhyn.hidoc.app.common.constant.UrlAccess;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 用户身份拦截器
 */
@Slf4j
public class SecurityInterceptor implements HandlerInterceptor {

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
                log.warn("LocalThread 泄露：" + user.getId() + ", " + user.getName());
            }
        } catch (Exception ex) {
            log.error("LocalThread 泄露检查异常");
        }

        // 登录信息设置和使用处理
        boolean isLogin = false;
        String uri = request.getRequestURI();
        // 获取所有header信息
        log.info("解析并确认当前用户身份");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
//            log.info("header: " + name + " = " + value);
            if (name.equals("access-token")) {
                UserInfo userInfo = R.Cache.UserInfo.get(value);
                if (userInfo != null) {
                    isLogin = true;
                    CurrentUserManager.set(userInfo);
                }
            }
        }

        if (isLogin) {
            log.info("已核对身份，用户信息已载入缓存");
            return true;
        } else {
            if (UrlAccess.isAnonymous(uri)) {
                log.info("未登录用户，访问开放内容，已准许");
                return true;
            }

            log.info("未登录用户，访问受限内容，已拒绝");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            ResponseData rs = new ResponseData(50014, "拒绝访问受限内容，请登录访问！");
            response.setContentLength(rs.toJSONString().getBytes().length);
            response.getOutputStream().write(rs.toJSONString().getBytes());
            return false;
        }
    }
}