package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.encrypts.MixdeTool;
import com.yuzhyn.azylee.core.datas.regexs.RegexPattern;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.datacoll.DataCollPlan;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollected;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.entity.file.FileBucket;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLogin;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCollectedMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.file.FileBucketMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserFileConfMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteOnlineMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLoginMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserMapper;
import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
import com.yuzhyn.hidoc.app.application.service.file.FileBucketService;
import com.yuzhyn.hidoc.app.application.service.sys.AuthCodeService;
import com.yuzhyn.hidoc.app.application.service.sys.SysUserLoginService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import com.yuzhyn.hidoc.app.utils.TOTPGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping({"user", "u"})
public class UserController {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysUserFileConfMapper sysUserFileConfMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    SysUserLiteOnlineMapper sysUserLiteOnlineMapper;

    @Autowired
    DocLiteMapper docLiteMapper;

    @Autowired
    DocCollectedMapper docCollectedMapper;

    @Autowired
    FileCursorMapper fileCursorMapper;

    @Autowired
    FileBucketMapper fileBucketMapper;

    @Autowired
    DocAccessLogMapper docAccessLogMapper;

    @Autowired
    AuthCodeService authCodeService;

    @Autowired
    FileBucketService fileBucketService;

    @Autowired
    SysUserLoginService sysUserLoginService;

    /**
     * 用户注册
     *
     * @param params
     * @return
     */
    @PostMapping("register")
    public ResponseData register(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "username", "email", "password", "realname", "authCode", "uid")) {
            String avatar = MapTool.get(params, "avatar", "").toString();
            String name = MapTool.get(params, "username", "").toString();
            String email = MapTool.get(params, "email", "").toString();
            String password = MapTool.get(params, "password", "").toString();
            String realname = MapTool.getString(params, "realname", "");
            String authCode = MapTool.getString(params, "authCode", "");
            String uid = MapTool.getString(params, "uid", "");

            String limitEmail = MapTool.getString(R.Maps.registerConfig, "limit-email", "");
            if (!email.endsWith(limitEmail)) {
                return ResponseData.error("系统限制仅支持：" + limitEmail + "的邮箱注册账号");
            }

            long emailIsUse = sysUserLiteMapper.selectCount(new LambdaQueryWrapper<SysUserLite>().eq(SysUserLite::getEmail, email));
            if (emailIsUse > 0) {
                return ResponseData.error("邮箱已经注册了账号，一个邮箱只能注册一个账号");
            }

            if (name.length() < R.MinPasswordLength || !RegexPattern.GENERAL.isMatch(name)) {
                return ResponseData.error("账号不符合规则，请录入" + R.MinPasswordLength + "位及以上账号，可使用字母数字下划线");
            }
            if (password.length() < R.MinPasswordLength || !RegexPattern.GENERAL.isMatch(password)) {
                return ResponseData.error("密码不符合规则，请录入" + R.MinPasswordLength + "位及以上密码，可使用字母数字下划线");
            }
            if (!StringTool.ok(uid)) {
                return ResponseData.error("请获取验证码，验证邮箱可用");
            }
            if (!StringTool.ok(authCode)) {
                return ResponseData.error("请输入验证码");
            }
            Tuple2<Boolean, String> checkFlag = authCodeService.checkCode(email, uid, authCode);
            if (!checkFlag.getT1()) {
                return ResponseData.error(checkFlag.getT2());
            }

            SysUser user = new SysUser();
            user.setId(R.SnowFlake.nexts());
            user.setName(name);
            user.setRealName(realname);
            user.setAvatar(avatar);
            user.setEmail(email.trim());
            user.setPassword(MixdeTool.md5Mix(user.getName(), password));
            user.setCreateTime(LocalDateTime.now());
            user.setLoginTime(LocalDateTime.now());
            user.setIsSleep(false);
            user.setIsFrozen(false);
            int flag = sysUserMapper.insert(user);
            if (flag > 0) {

                SysUserFileConf conf = new SysUserFileConf();
                conf.setUserId(user.getId());
                conf.setCreateTime(LocalDateTime.now());
                conf.setExpiryTime(LocalDateTimeTool.max());
                conf.setSpaceLimit(1024 * 1024 * 1024L); // 1G
                conf.setUrlPrefix(name);
                conf.setUsedSpace(0L);
                sysUserFileConfMapper.insert(conf);

                fileBucketService.createSystemDefaultBucket(R.Buckets.HidocFile, user.getId());
                fileBucketService.createSystemDefaultBucket(R.Buckets.Avatar, user.getId());

                return ResponseData.okData("sysUser", user);
            }
        }
        return ResponseData.error("注册失败，请完善信息");
    }

    /**
     * 重置密码
     *
     * @param params
     * @return
     */
    @PostMapping("resetPassword")
    public ResponseData resetPassword(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "email", "authCode", "uid", "password", "passwordConfirm")) {
            String email = MapTool.getString(params, "email", "");
            String authCode = MapTool.getString(params, "authCode", "");
            String uid = MapTool.getString(params, "uid", "");
            String password = MapTool.getString(params, "password", "");
            String passwordConfirm = MapTool.getString(params, "passwordConfirm", "");

            if (!StringTool.ok(email, authCode, uid, password, passwordConfirm)) {
                return ResponseData.error("重置失败，请输入完整内容");
            }
            if (!password.equals(passwordConfirm)) {
                return ResponseData.error("重置失败，两次密码不一致");
            }

            Tuple2<Boolean, String> checkFlag = authCodeService.checkCode(email, uid, authCode);
            if (!checkFlag.getT1()) {
                return ResponseData.error(checkFlag.getT2());
            }

            SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, email));
            if (user != null) {
                user.setPassword(MixdeTool.md5Mix(user.getName(), password));
                user.setLoginTime(LocalDateTime.now());
                user.setIsSleep(false);
                int flag = sysUserMapper.updateById(user);
                if (flag > 0) {
                    return ResponseData.ok("重置成功");
                }
            } else {
                return ResponseData.error("没有找到该邮箱对应的账号");
            }
        }
        return ResponseData.error("重置失败，请输入完整内容");
    }

    /**
     * 用户登录
     *
     * @param params
     * @return
     */
    @PostMapping("login")
    public ResponseData login(@RequestBody Map<String, Object> params) {
        String username = MapTool.get(params, "username", "").toString();
        String password = MapTool.get(params, "password", "").toString();
        String totpcode = MapTool.get(params, "totpcode", "").toString();

        if (!StringTool.ok(username)) return ResponseData.error("登录失败，账号不正确");
        if (!StringTool.ok(password) && !StringTool.ok(totpcode))
            return ResponseData.error("登录失败，密码或验证码没有输入");

        // 先根据邮箱查询用户信息，如果没有再根据用户名查询
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, username));
        if (user == null)
            user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getName, username));
        if (user == null) return ResponseData.error("邮箱不存在");
        if (user.getIsSleep() != null && user.getIsSleep())
            return ResponseData.error("账号长时间未使用，请重置密码后进行登录");

        if (StringTool.ok(totpcode) && totpcode.length() == 6) {

            // 使用动态口令进行登录
            if (!StringTool.ok(user.getTotpSeed())) return ResponseData.error("账号未设置动态口令");
            if (user.getTotpCreateTime() == null) return ResponseData.error("账号动态口令未生效");
            String userId = user.getId();
            String seed = user.getTotpSeed();
            LocalDateTime totpCreateTime = user.getTotpCreateTime();
            String secret = TOTPGenerator.generateSecret(userId, seed, DateTimeFormat.toStr(totpCreateTime));
            if (!TOTPGenerator.generateNumber(secret, 30, 6).equals(totpcode)) return ResponseData.error("验证码错误");
            // 验证成功，设置用户为登录状态
            return loginAfterCheck(user);

        } else if (StringTool.ok(password)) {

            // 使用密码进行登录
            // password 要设置加密后的字符串，0除外，0为重置密码，允许直接登录
            if (!password.equals("0")) password = MixdeTool.md5Mix(user.getName(), password);
            if (!user.getPassword().equals(password)) return ResponseData.error("登录失败，账号或密码不正确");
            // 验证成功，设置用户为登录状态
            return loginAfterCheck(user);

        }
        return ResponseData.error("登录失败，账号或密码不正确");
    }

    private ResponseData loginAfterCheck(SysUser user) {
        user.setIsSleep(false);
        user.setLoginTime(LocalDateTime.now());
        sysUserMapper.updateById(user);

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
        userInfo.setIp(CurrentUserManager.ip.get());
        userInfo.setLoginTime(LocalDateTime.now());
        userInfo.setExpiryTime(LocalDateTime.now().plusHours(8));

        // 这里记录登录信息缓存到数据库
        sysUserLoginService.updateUserLoginData(userInfo);

        // 登录成功根据个人通知配置，发送登录通知信息到邮箱
        // 暂定
        rs.setToken(userInfo.getToken());
        return rs;
    }

    /**
     * 冻结账号
     *
     * @param params
     * @return
     */
    @PostMapping("setFrozen")
    public ResponseData setFrozen(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "userId")) {
            String userId = MapTool.get(params, "userId", "").toString();
            boolean op = MapTool.getBoolean(params, "op", "");

            SysUserLite userLite = sysUserLiteMapper.selectById(userId);
            if (userLite != null) {
                userLite.setIsFrozen(op);
                sysUserLiteMapper.updateById(userLite);
                if (op) return ResponseData.ok(userLite.getRealName() + " 账号已冻结");
                else return ResponseData.ok(userLite.getRealName() + " 账号已恢复");
            }
        }
        return ResponseData.error("操作失败");
    }

    /**
     * 修改密码
     *
     * @param params
     * @return
     */
    @PostMapping("changePassword")
    public ResponseData changePassword(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "password", "password2")) {
            String password = MapTool.get(params, "password", "").toString();
            String password2 = MapTool.get(params, "password2", "").toString();

            if (!StringTool.ok(password, password2)) return ResponseData.error("修改失败，请输入密码");

            if (!password.equals(password2)) return ResponseData.error("修改失败，两次密码不一致");

            SysUser user = sysUserMapper.selectById(CurrentUserManager.getUser().getId());
            user.setPassword(MixdeTool.md5Mix(user.getName(), password));
            user.setLoginTime(LocalDateTime.now());
            user.setIsSleep(false);
            int flag = sysUserMapper.updateById(user);
            if (flag > 0) {
                return ResponseData.ok("修改成功");
            }
        }
        return ResponseData.error("修改失败，请完善信息");
    }

    /**
     * 当前登录用户信息
     *
     * @return
     */
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

    /**
     * 当前登录用户看板数据
     *
     * @return
     */
    @PostMapping("currentUserBoard")
    public ResponseData currentUserBoard() {
        // 文集数量 文档数量 文件数量 阅读数量 我阅读数量
        long collectedCount = docCollectedMapper.selectCount(new LambdaQueryWrapper<DocCollected>().eq(DocCollected::getCreateUserId, CurrentUserManager.getUser().getId()));
        long docCount = docLiteMapper.selectCount(new LambdaQueryWrapper<DocLite>().eq(DocLite::getCreateUserId, CurrentUserManager.getUser().getId()));
        long cursorCount = fileCursorMapper.selectCount(new LambdaQueryWrapper<FileCursor>().eq(FileCursor::getUserId, CurrentUserManager.getUser().getId()));
        long readCount = docAccessLogMapper.selectCount(new LambdaQueryWrapper<DocAccessLog>().eq(DocAccessLog::getOwnerUserId, CurrentUserManager.getUser().getId()).ne(DocAccessLog::getUserId, CurrentUserManager.getUser().getId()));
//        Integer iReadCount = docAccessLogMapper.selectCount(new LambdaQueryWrapper<DocAccessLog>().eq(DocAccessLog::getCreateUserId, CurrentUserManager.getUser().getId()));

        // 近14天 文集数量 文档数量 文件数量 阅读数量
        List<Map> statisData = new ArrayList<>();

        List<String> dateList = new ArrayList<>();
        List<Long> collectedCountList = new ArrayList<>();
        List<Long> docCountList = new ArrayList<>();
        List<Long> docUpdateCountList = new ArrayList<>();
        List<Long> cursorCountList = new ArrayList<>();
        List<Long> readCountList = new ArrayList<>();
        for (int i = 30; i >= 0; i--) {
            LocalDate localDate = LocalDate.now();
            LocalDateTime beginTime = localDate.plusDays(i * -1).atStartOfDay();
            LocalDateTime endTime = localDate.plusDays((i - 1) * -1).atStartOfDay();

            Map map = new HashMap<>();
            long _collectedCount = docCollectedMapper.selectCount(new LambdaQueryWrapper<DocCollected>().eq(DocCollected::getCreateUserId, CurrentUserManager.getUser().getId()).ge(DocCollected::getCreateTime, beginTime).lt(DocCollected::getCreateTime, endTime).eq(DocCollected::getIsDelete, false));
            long _docCount = docLiteMapper.selectCount(new LambdaQueryWrapper<DocLite>().eq(DocLite::getCreateUserId, CurrentUserManager.getUser().getId()).ge(DocLite::getCreateTime, beginTime).lt(DocLite::getCreateTime, endTime).eq(DocLite::getIsDelete, false));
            long _docUpdateCount = docLiteMapper.selectCount(new LambdaQueryWrapper<DocLite>().eq(DocLite::getCreateUserId, CurrentUserManager.getUser().getId()).ge(DocLite::getUpdateTime, beginTime).lt(DocLite::getUpdateTime, endTime).eq(DocLite::getIsDelete, false));
            long _cursorCount = fileCursorMapper.selectCount(new LambdaQueryWrapper<FileCursor>().eq(FileCursor::getUserId, CurrentUserManager.getUser().getId()).ge(FileCursor::getCreateTime, beginTime).lt(FileCursor::getCreateTime, endTime).eq(FileCursor::getIsDelete, false));
            long _readCount = docAccessLogMapper.selectCount(new LambdaQueryWrapper<DocAccessLog>().eq(DocAccessLog::getOwnerUserId, CurrentUserManager.getUser().getId()).ne(DocAccessLog::getUserId, CurrentUserManager.getUser().getId()).ge(DocAccessLog::getCreateTime, beginTime).lt(DocAccessLog::getCreateTime, endTime));
            map.put("date", DateTimeFormat.toStr(beginTime, DateTimeFormatPattern.NORMAL_DATE));
            map.put("collectedCount", _collectedCount);
            map.put("docCount", _docCount);
            map.put("docUpdateCount", _docUpdateCount);
            map.put("cursorCount", _cursorCount);
            map.put("readCount", _readCount);
            statisData.add(map);

            dateList.add(DateTimeFormat.toStr(beginTime, DateTimeFormatPattern.NORMAL_DATE));
            collectedCountList.add(_collectedCount);
            docCountList.add(_docCount);
            docUpdateCountList.add(_docUpdateCount);
            cursorCountList.add(_cursorCount);
            readCountList.add(_readCount);
        }

        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("collectedCount", collectedCount);
        responseData.putDataMap("docCount", docCount);
        responseData.putDataMap("cursorCount", cursorCount);
        responseData.putDataMap("readCount", readCount);
//        responseData.putDataMap("iReadCount", iReadCount);
        responseData.putDataMap("statisData", statisData);

        Map<String, Object> trendChartData = new HashMap<>();
        trendChartData.put("dateList", dateList);
        trendChartData.put("collectedCountList", collectedCountList);
        trendChartData.put("docCountList", docCountList);
        trendChartData.put("cursorCountList", cursorCountList);
        trendChartData.put("readCountList", readCountList);
        trendChartData.put("docUpdateCountList", docUpdateCountList);
        responseData.putDataMap("trendChartData", trendChartData);

        return responseData;
    }

    /**
     * 更新当前用户信息
     *
     * @return
     */
    @PostMapping("updateCurrentUserInfo")
    public ResponseData updateCurrentUserInfo(@RequestBody Map<String, Object> params) {
        String type = MapTool.get(params, "type", "").toString();
        String value = MapTool.get(params, "value", "").toString();
        if (StringTool.ok(type, value)) {
            SysUser sysUser = sysUserMapper.selectById(CurrentUserManager.getUserId());
            if (sysUser == null) return ResponseData.error();

            if ("realName".equals(type)) sysUser.setRealName(value);
            if ("avatar".equals(type)) sysUser.setAvatar(value);

            if (sysUserMapper.updateById(sysUser) > 0) return ResponseData.ok();
        }
        return ResponseData.error();
    }

    /**
     * 退出登录
     *
     * @param params
     * @return
     */
    @PostMapping("logout")
    public ResponseData logout(@RequestBody Map<String, Object> params) {

        String ip = MapTool.get(params, "ip", "").toString();
        String token = MapTool.get(params, "token", "").toString();

        if (StringTool.ok(ip, token)) {
            // 注销指定token的用户，从数据库删除
            sysUserLoginService.deleteUserLoginData(token);

        } else {
            // 注销当前登录用户

        }

//        CurrentUserManager.clearCurrentUser();
//        UserInfo userInfo = CurrentUserManager.currentUser.get();
//        return ResponseData.okData("userInfo", userInfo);
        return ResponseData.ok();
    }


    /**
     * 当前用户的多端登录状态信息
     *
     * @return
     */
    @PostMapping("getLoginUserInfo")
    public ResponseData getLoginUserInfo() {
        List<UserInfo> userInfoList = sysUserLoginService.getUserInfoList(CurrentUserManager.getUser().getId());
        return ResponseData.okData(userInfoList);
    }


    /**
     * 获取用户列表
     *
     * @param params
     * @return
     */
    @PostMapping("getUsers")
    public ResponseData getUsers(@RequestBody Map<String, Object> params) {
        List<SysUserLite> list = sysUserLiteMapper.selectList(new LambdaQueryWrapper<SysUserLite>().orderByAsc(SysUserLite::getIsFrozen).orderByDesc(SysUserLite::getLoginTime));
        if (ListTool.ok(list)) {
            // 查询用户的空间用量信息
            List<String> userIds = list.stream().map(SysUserLite::getId).collect(Collectors.toList());
            List<SysUserFileConf> confList = sysUserFileConfMapper.selectList(new LambdaQueryWrapper<SysUserFileConf>().in(SysUserFileConf::getUserId, userIds));
            List<UserInfo> userInfoList = sysUserLoginService.getUserInfoList(CurrentUserManager.getUser().getId());

            for (SysUserLite liteItem : list) {
                // 补充用户的空间用量信息
                for (SysUserFileConf conf : confList) {
                    if (liteItem.getId().equals(conf.getUserId())) {

                        if (conf.getSpaceLimit() > 0)
                            conf.setUsedSpacePercent(conf.getUsedSpace() * 100 / conf.getSpaceLimit());
                        else conf.setUsedSpacePercent(100L);

                        liteItem.setSysUserFileConf(conf);
                        break;
                    }
                }

                // 补充用户的在线状态
                for (UserInfo item : userInfoList) {
                    if (liteItem.getId().equals(item.getUser().getId())) {
                        liteItem.setIsOnline(true);
                        break;
                    }
                }
            }
        }
        return ResponseData.okData(list);
    }

    /**
     * 创建用户的TOTP的URI
     * 重复创建将替换掉之前的TOTP信息
     *
     * @param params
     * @return
     */
    @PostMapping("createTotpUri")
    public ResponseData createTotpUri(@RequestBody Map<String, Object> params) {
        String userId = CurrentUserManager.getUser().getId();
        String seed = UUIDTool.get();
        LocalDateTime now = LocalDateTime.now();
        String secret = TOTPGenerator.generateSecret(userId, seed, DateTimeFormat.toStr(now));

        SysUser user = sysUserMapper.selectById(userId);
        if (user != null) {
            user.setTotpSeed(seed);
            user.setTotpCreateTime(now);
            int flag = sysUserMapper.updateById(user);
            if (flag > 0) {
                String uri = TOTPGenerator.generateUri(R.AppNameCn, user.getRealName(), secret, 6, 30);
                return ResponseData.okData("uri", uri);
            }
        }
        return ResponseData.ok();
    }


    @PostMapping("setAdmin")
    public ResponseData setAdmin(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "userId", "op")) {
            // 验证登录用户的身份，必须为超级管理员（sa）
            if (!CurrentUserManager.getUser().getRoles().contains("sa"))
                return ResponseData.error("您没有权限进行该项操作");

            String userId = MapTool.get(params, "userId", "").toString();
            boolean op = MapTool.getBoolean(params, "op", "");

            SysUser user = sysUserMapper.selectById(userId);
            if (user.getRoles() == null) user.setRoles(new JSONArray());
            if (op) {
                if (!user.getRoles().contains("admin")) user.getRoles().add("admin");
            } else {
                user.getRoles().remove("admin");
            }

            int flag = sysUserMapper.updateById(user);
            if (flag > 0) {
                return ResponseData.ok("操作成功");
            }
        }
        return ResponseData.error("操作失败，请完善信息");
    }


    @PostMapping("extendFileSpace")
    public ResponseData extendFileSpace(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "userId", "op")) {
            // 验证登录用户的身份，必须为超级管理员或管理员
            if (!CurrentUserManager.getUser().getRoles().contains("sa") && !CurrentUserManager.getUser().getRoles().contains("admin"))
                return ResponseData.error("您没有权限进行该项操作");

            String userId = MapTool.get(params, "userId", "").toString();
            boolean op = MapTool.getBoolean(params, "op", "");

            SysUserFileConf sysUserFileConf = sysUserFileConfMapper.selectById(userId);
            if (sysUserFileConf != null) {
                if (op) {
                    sysUserFileConf.setSpaceLimit(sysUserFileConf.getSpaceLimit() + 1073741824);
                } else {
                    long spaceLimit = sysUserFileConf.getSpaceLimit() - 1073741824;
                    if (spaceLimit > 0) sysUserFileConf.setSpaceLimit(spaceLimit);
                }
                int flag = sysUserFileConfMapper.updateById(sysUserFileConf);
                if (flag > 0) {
                    return ResponseData.ok("操作成功");
                }
            }
        }
        return ResponseData.error("操作失败，请完善信息");
    }

//    @PostMapping("createAnalysisIndex")
//    public ResponseData createAnalysisIndex() {
//
//        List<UserInfo> userInfoList = new ArrayList<>();
//
//        for (Iterator<Cache.Entry<String, UserInfo>> i = R.Cache.UserInfo.iterator(); i.hasNext(); ) {
//            Cache.Entry<String, UserInfo> item = i.next();
//            if (CurrentUserManager.getUser().getId().equals(item.getValue().getUser().getId())) {
//                userInfoList.add(item.getValue());
//            }
//        }
//        return ResponseData.okData(userInfoList);
//    }
}
