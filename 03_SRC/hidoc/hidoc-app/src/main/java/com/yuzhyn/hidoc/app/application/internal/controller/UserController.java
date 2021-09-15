package com.yuzhyn.hidoc.app.application.internal.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.customization.entity.DocCollected;
import com.yuzhyn.hidoc.app.application.customization.entity.DocLite;
import com.yuzhyn.hidoc.app.application.customization.mapper.DocCollectedMapper;
import com.yuzhyn.hidoc.app.application.customization.mapper.DocLiteMapper;
import com.yuzhyn.hidoc.app.application.customization.mapper.DocMapper;
import com.yuzhyn.hidoc.app.application.internal.entity.SysFileCursor;
import com.yuzhyn.hidoc.app.application.internal.entity.SysUser;
import com.yuzhyn.hidoc.app.application.internal.entity.SysUserFileConf;
import com.yuzhyn.hidoc.app.application.internal.entity.SysUserLite;
import com.yuzhyn.hidoc.app.application.internal.mapper.SysFileCursorMapper;
import com.yuzhyn.hidoc.app.application.internal.mapper.SysUserFileConfMapper;
import com.yuzhyn.hidoc.app.application.internal.mapper.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.internal.mapper.SysUserMapper;
import com.yuzhyn.hidoc.app.application.internal.model.UserInfo;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.yuzhyn.azylee.core.datas.collections.MapTool;
import pers.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import pers.yuzhyn.azylee.core.datas.encrypts.DesTool;
import pers.yuzhyn.azylee.core.datas.encrypts.Md5Tool;
import pers.yuzhyn.azylee.core.datas.encrypts.MixdeTool;
import pers.yuzhyn.azylee.core.datas.ids.UUIDTool;
import pers.yuzhyn.azylee.core.datas.regexs.RegexPattern;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("i/user")
public class UserController {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysUserFileConfMapper sysUserFileConfMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    DocLiteMapper docLiteMapper;

    @Autowired
    DocCollectedMapper docCollectedMapper;

    @Autowired
    SysFileCursorMapper sysFileCursorMapper;

    @PostMapping("register")
    public ResponseData register(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "username", "email", "password", "realname")) {
            String avatar = MapTool.get(params, "avatar", "").toString();
            String name = MapTool.get(params, "username", "").toString();
            String email = MapTool.get(params, "email", "").toString();
            String password = MapTool.get(params, "password", "").toString();
            String realname = MapTool.getString(params, "realname", "");

            if (name.length() < R.MinPasswordLength || !RegexPattern.GENERAL.isMatch(name)) {
                return ResponseData.error("账号不符合规则，仅支持6位及以上字母数字下划线组合");
            }
            if (password.length() < R.MinPasswordLength || !RegexPattern.GENERAL.isMatch(password)) {
                return ResponseData.error("密码不符合规则，仅支持6位及以上字母数字下划线组合");
            }

            SysUser user = new SysUser();
            user.setId(R.SnowFlake.nexts());
            user.setName(name);
            user.setRealName(realname);
            user.setAvatar(avatar);
            user.setEmail(email);
            user.setPassword(MixdeTool.md5Mix(user.getName(), password));
            user.setCreateTime(LocalDateTime.now());
            user.setIsFrozen(true); // 注册之后，默认是冻结状态
            int flag = sysUserMapper.insert(user);
            if (flag > 0) {

                SysUserFileConf conf = new SysUserFileConf();
                conf.setUserId(user.getId());
                conf.setCreateTime(LocalDateTime.now());
                conf.setExpiryTime(LocalDateTimeTool.max());
                conf.setSpaceLimit(1024 * 1024 * 1024L);
                conf.setUrlPrefix(name);
                conf.setUsedSpace(0L);
                sysUserFileConfMapper.insert(conf);

                return ResponseData.okData("sysUser", user);
            }
        }
        return ResponseData.error("注册失败，请完善信息");
    }

    @PostMapping("login")
    public ResponseData login(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "username", "password")) {
            String username = MapTool.get(params, "username", "").toString();
            String password = MapTool.get(params, "password", "").toString();

            // password 要设置加密后的字符串，0除外，0为重置密码，允许直接登录
            SysUser currentUser = CurrentUserManager.getUser();
            if (!password.equals("0")) password = MixdeTool.md5Mix(username, password);
            String pwdParam = password;

            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>().eq(SysUser::getName, username);
            wrapper.and(p -> p.eq(SysUser::getPassword, pwdParam).or().eq(SysUser::getPassword, pwdParam));
            SysUser user = sysUserMapper.selectOne(wrapper);

            if (user != null) {

                // 判断账号是否冻结
                if (user.getIsFrozen()) {
                    return ResponseData.error("账号为冻结状态，新注册账号请联系管理员启用。");
                }

                user.setPassword("");
                ResponseData rs = ResponseData.okData("sysUser", user);
                SysUserFileConf conf = sysUserFileConfMapper.selectById(user.getId());
                rs.putDataMap("sysUserFileConf", conf);

                // 登录成功后，缓存登录用户信息数据
                UserInfo userInfo = new UserInfo();
                userInfo.setUser(user);
                userInfo.setUserFileConf(conf);
                userInfo.setToken(UUIDTool.get());
                R.Cache.UserInfo.put(userInfo.getToken(), userInfo);
                UserInfo userinfoCache = R.Cache.UserInfo.get(userInfo.getToken());

                rs.setToken(userInfo.getToken());
                return rs;
            }
        }
        return ResponseData.error("登录失败，账号或密码不正确");
    }


    @PostMapping("changePassword")
    public ResponseData changePassword(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "password", "password2")) {
            String password = MapTool.get(params, "password", "").toString();
            String password2 = MapTool.get(params, "password2", "").toString();

            if (!StringTool.ok(password, password2)) return ResponseData.error("修改失败，请输入密码");

            if (!password.equals(password2)) return ResponseData.error("修改失败，两次密码不一致");

            SysUser user = sysUserMapper.selectById(CurrentUserManager.getUser().getId());
            user.setPassword(MixdeTool.md5Mix(user.getName(), password));
            int flag = sysUserMapper.updateById(user);
            if (flag > 0) {
                return ResponseData.ok("修改成功");
            }
        }
        return ResponseData.error("修改失败，请完善信息");
    }

    @PostMapping("currentUserInfo")
    public ResponseData currentUserInfo() {
        SysUserLite user = sysUserLiteMapper.selectById(CurrentUserManager.getUser().getId());
        SysUserFileConf conf = sysUserFileConfMapper.selectById(user.getId());
        if (conf != null) conf.setSpaceUsage(conf.getUsedSpace() * 100 / conf.getSpaceLimit());

        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("user", user);
        responseData.putDataMap("fileConf", conf);
        return responseData;
    }

    @PostMapping("currentUserBoard")
    public ResponseData currentUserBoard() {
        // 文集数量 文档数量 文件数量 阅读数量
        Integer collectedCount = docCollectedMapper.selectCount(new LambdaQueryWrapper<DocCollected>().eq(DocCollected::getCreateUserId, CurrentUserManager.getUser().getId()));
        Integer docCount = docLiteMapper.selectCount(new LambdaQueryWrapper<DocLite>().eq(DocLite::getCreateUserId, CurrentUserManager.getUser().getId()));
        Integer cursorCount = sysFileCursorMapper.selectCount(new LambdaQueryWrapper<SysFileCursor>().eq(SysFileCursor::getUserId, CurrentUserManager.getUser().getId()));
        Integer readCount = 0;

        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("collectedCount", collectedCount);
        responseData.putDataMap("docCount", docCount);
        responseData.putDataMap("cursorCount", cursorCount);
        responseData.putDataMap("readCount", readCount);
        return responseData;
    }

    @PostMapping("updateCurrentUserInfo")
    public ResponseData updateCurrentUserInfo() {
        ResponseData responseData = ResponseData.ok();
        return responseData;
    }

    @PostMapping("logout")
    public ResponseData logout() {
//        CurrentUserManager.clearCurrentUser();
//        UserInfo userInfo = CurrentUserManager.currentUser.get();
//        return ResponseData.okData("userInfo", userInfo);
        return ResponseData.ok();
    }

    @GetMapping("userList")
    public ResponseData userList() {
        List<SysUser> list = sysUserMapper.selectList(null);
        list = new ArrayList<>();
        return ResponseData.okData(list);
    }
}
