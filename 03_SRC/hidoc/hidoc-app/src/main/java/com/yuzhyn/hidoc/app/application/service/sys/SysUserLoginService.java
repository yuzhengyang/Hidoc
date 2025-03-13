package com.yuzhyn.hidoc.app.application.service.sys;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLogin;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserFileConfMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLoginMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserMapper;
import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SysUserLoginService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserFileConfMapper sysUserFileConfMapper;

    @Autowired
    private SysUserLoginMapper sysUserLoginMapper;

    /**
     * 将用户的登录状态保存到数据库中，在启动时加载到缓存
     * 后续考虑：定时将登录信息同步到服务上来，以支持多服务部署的情景
     * 注意：暂不考虑引入redis
     */
//    public void loadUserLoginData() {
//        try {
//            // 查询没有过期的登录信息（过期时间在2分钟内），在启动时加入缓存
//            LocalDateTime signTime = LocalDateTime.now().minusMinutes(2);
//            List<SysUserLogin> userLoginList = sysUserLoginMapper.selectList(new LambdaQueryWrapper<SysUserLogin>().ge(SysUserLogin::getExpiryTime, signTime));
//            // 如果列表中有数据，则开始循环查询用户详细信息，组装详情
//            if (ObjectUtil.isNotEmpty(userLoginList)) {
//                for (SysUserLogin userLogin : userLoginList) {
//                    SysUser user = sysUserMapper.selectById(userLogin.getUserId());
//                    SysUserFileConf userFileConf = sysUserFileConfMapper.selectById(userLogin.getUserId());
//                    if (ObjectUtil.isNotEmpty(user) && ObjectUtil.isNotEmpty(userFileConf)) {
//                        UserInfo userInfo = new UserInfo();
//                        userInfo.setUser(user);
//                        userInfo.setUserFileConf(userFileConf);
//                        userInfo.setToken(userLogin.getToken());
//                        userInfo.setIp(userLogin.getIp());
//                        userInfo.setLoginTime(userLogin.getLoginTime());
//                        userInfo.setExpiryTime(userLogin.getExpiryTime());
//                        if (R.Caches.UserInfo.getIfPresent(userLogin.getToken()) == null) {
//                            R.Caches.UserInfo.put(userInfo.getToken(), userInfo);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.info("初始化历史登录用户信息异常");
//            log.info(ExceptionUtil.stacktraceToString(e, 10));
//        }
//    }

//    public void deleteUserLoginData(String token) {
//        R.Caches.UserInfo.invalidate(token);
//        sysUserLoginMapper.deleteById(token);
//    }
    public UserInfo getUserLoginData(String token) {
        SysUserLogin userLogin = sysUserLoginMapper.selectById(token);
        if (ObjectUtil.isNotEmpty(userLogin) && ObjectUtil.isNotEmpty(userLogin.getUserInfo())) {
            return userLogin.getUserInfo();
        }
        return null;
    }

    public void updateUserLoginData(UserInfo userInfo) {
        SysUserLogin userLogin = new SysUserLogin();
        userLogin.setToken(userInfo.getToken());
        userLogin.setUserId(userInfo.getUser().getId());
        userLogin.setIp(userInfo.getIp());
        userLogin.setLoginTime(userInfo.getLoginTime());
        userLogin.setExpiryTime(userInfo.getExpiryTime());
        userLogin.setUserInfo(userInfo);
        sysUserLoginMapper.insertOrUpdate(userLogin);
    }

    public void deleteUserLoginData(String token) {
        sysUserLoginMapper.deleteById(token);
    }

    public List<UserInfo> getUserInfoList(String userId) {
        List<UserInfo> userInfoList = new ArrayList<>();
        List<SysUserLogin> userLoginList = sysUserLoginMapper.selectList(new LambdaQueryWrapper<SysUserLogin>()
                .eq(SysUserLogin::getUserId, userId)
                .ge(SysUserLogin::getExpiryTime, LocalDateTime.now()));
        if (ObjectUtil.isNotEmpty(userLoginList)) {
            for (SysUserLogin userLogin : userLoginList) {
                UserInfo userInfo = userLogin.getUserInfo();
                if (ObjectUtil.isNotEmpty(userInfo)) userInfoList.add(userInfo);
            }
        }
        return userInfoList;
    }
}
