package com.yuzhyn.hidoc.app.application.service.card;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.card.Card;
import com.yuzhyn.hidoc.app.application.entity.card.CardLevel;
import com.yuzhyn.hidoc.app.application.entity.card.CardUser;
import com.yuzhyn.hidoc.app.application.mapper.card.CardLevelMapper;
import com.yuzhyn.hidoc.app.application.mapper.card.CardMapper;
import com.yuzhyn.hidoc.app.application.mapper.card.CardUserLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.card.CardUserMapper;
import com.yuzhyn.hidoc.app.application.service.sys.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CardUserService {

    @Autowired
    CardMapper cardMapper;

    @Autowired
    CardLevelMapper cardLevelMapper;

    @Autowired
    CardUserMapper cardUserMapper;

    @Autowired
    CardUserLogMapper cardUserLogMapper;

    @Autowired
    EmailService emailService;

    /**
     * 申请账号/卡密
     *
     * @param params
     * @return
     */
    public Tuple2<Boolean, String> applyLevel(Map<String, Object> params) {
        String cardId = MapTool.getString(params, "cardId", "");
        String levelId = MapTool.getString(params, "levelId", "");
        String userEmail = MapTool.getString(params, "userEmail", "");
        if (!StringTool.ok(cardId, levelId, userEmail)) return Tuples.of(false, "请求参数不完整");

        Card card = cardMapper.selectById(cardId);
        if (card == null) return Tuples.of(false, "缺少应用定义信息");

        CardLevel cardLevel = cardLevelMapper.selectById(levelId);
        if (cardLevel == null) return Tuples.of(false, "缺少等级信息");

        CardUser cardUser = cardUserMapper.selectOne(new LambdaQueryWrapper<CardUser>()
                .eq(CardUser::getCardId, cardId)
                .eq(CardUser::getUserEmail, userEmail));
        if (cardUser != null) {
            if (cardUser.getIsEnable()) {
                // 已有的账号信息，在等级允许自助申请时，检查并补全新申请的等级信息
                if (cardLevel.getIsAllowApply() && !cardUser.getLevelIds().contains(levelId)) cardUser.setLevelIds(cardUser.getLevelIds() + "," + levelId);
                cardUserMapper.updateById(cardUser);

                if (emailService.sendCardUserPassword(userEmail, cardUser.getUserPassword(), card.getName(), cardLevel.getName()))
                    return Tuples.of(true, "申请成功");
                else
                    return Tuples.of(false, "申请成功，邮件发送失败");

            } else return Tuples.of(false, "账号已禁用");
        }

        if (!cardLevel.getIsAllowApply()) return Tuples.of(false, "当前等级不支持自助申请");

        if (!userEmail.contains(cardLevel.getAllowEmailSuffix())) return Tuples.of(false, "您的邮箱地址不支持申请");

        // 在level允许自助申请时，如果没有匹配记录，则创建记录
        cardUser = new CardUser();
        cardUser.setId(R.SnowFlake.nexts());
        cardUser.setCardId(cardId);
        cardUser.setLevelIds(levelId);
        cardUser.setUserEmail(userEmail);
        cardUser.setUserPassword(UUIDTool.get());
        cardUser.setSignatures("");
        cardUser.setSalt(UUIDTool.getId64());
        cardUser.setDescription("");
        cardUser.setCreateMode("0");
        cardUser.setCreateTime(LocalDateTime.now());
        cardUser.setIsEnable(true);
        if (cardUserMapper.insert(cardUser) > 0) {
            if (emailService.sendCardUserPassword(userEmail, cardUser.getUserPassword(), card.getName(), cardLevel.getName()))
                return Tuples.of(true, "申请成功");
            else
                return Tuples.of(false, "申请成功，邮件发送失败");
        }

        return Tuples.of(false, "申请失败");
    }


    /**
     * 登录账号/卡密
     *
     * @param params
     * @return
     */
    public Tuple3<Boolean, String, List<CardLevel>> loginLevel(Map<String, Object> params) {
        String cardId = MapTool.getString(params, "cardId", "");
        String userEmail = MapTool.getString(params, "userEmail", "");
        String userPassword = MapTool.getString(params, "userPassword", "");
        String signature = MapTool.getString(params, "signature", "");
        CardUser cardUser = cardUserMapper.selectOne(new LambdaQueryWrapper<CardUser>()
                .eq(CardUser::getCardId, cardId)
                .eq(CardUser::getUserEmail, userEmail)
                .eq(CardUser::getUserPassword, userPassword));

        if (cardUser == null) return Tuples.of(false, "账号不存在或密码错误", null);

        if (!cardUser.getIsEnable()) return Tuples.of(false, "账号已禁用", null);

        if (StringTool.ok(cardUser.getSignatures()) && !cardUser.getSignatures().contains(signature))
            return Tuples.of(false, "设备不可用", null);

        List<CardLevel> levels = null;
        String[] idList = StringTool.split(cardUser.getLevelIds(), ",", true, true);
        if (StringTool.ok(cardId) && ListTool.ok(idList)) {
            levels = cardLevelMapper.selectList(new LambdaQueryWrapper<CardLevel>()
                    .eq(CardLevel::getCardId, cardId)
                    .in(CardLevel::getId, idList));
        }

        return Tuples.of(true, "登录成功", levels);
    }

    public Tuple3<Boolean, String, String> lock(Map<String, Object> params) {
        String cardId = MapTool.getString(params, "cardId", "");
        String levelId = MapTool.getString(params, "levelId", "");
        String userEmail = MapTool.getString(params, "userEmail", "");
        String userPassword = MapTool.getString(params, "userPassword", "");
        int duration = MapTool.getInt(params, "duration", 60);
        LocalDateTime now = LocalDateTime.now();
        CardLevel cardLevel = cardLevelMapper.selectById(levelId);
        CardUser cardUser = cardUserMapper.selectOne(new LambdaQueryWrapper<CardUser>()
                .eq(CardUser::getCardId, cardId)
                .eq(CardUser::getUserEmail, userEmail)
                .eq(CardUser::getUserPassword, userPassword));
        if (cardLevel == null || cardUser == null) return Tuples.of(false, "", "账号不存在或密码错误");

        if (!cardUser.getLevelIds().contains(cardLevel.getId())) return Tuples.of(false, "", "账号未开通该等级");

        if (cardLevel.getLockUserId() != null && cardLevel.getLockTime() != null) {
            if (!cardLevel.getLockUserId().equals(cardUser.getId()) && !cardLevel.getLockTime().plusSeconds(cardLevel.getLockDuration()).isBefore(now)) {
                return Tuples.of(false, "", "当前已由其他用户锁定");
            }
        }

        // 记录锁定的相关信息，用来锁定
        String oldLockKey = cardLevel.getLockKey();
        String newLockKey = UUIDTool.get();
        long oldLockVersion = cardLevel.getLockVersion();
        long newLockVersion = cardLevel.getLockVersion() + 1;

        cardLevel.setLockUserId(cardUser.getId());
        cardLevel.setLockTime(now);
        cardLevel.setLockDuration(duration);
        cardLevel.setLockVersion(newLockVersion);
        cardLevel.setLockKey(newLockKey);
        if (cardLevelMapper.update(cardLevel, new LambdaQueryWrapper<CardLevel>()
                .eq(CardLevel::getId, levelId)
                .eq(CardLevel::getLockKey, oldLockKey)
                .eq(CardLevel::getLockVersion, oldLockVersion)) > 0)
            return Tuples.of(true, newLockKey, "锁定成功");

        return Tuples.of(false, "", "锁定失败");
    }

    public Tuple2<Boolean, String> unlock(Map<String, Object> params) {
        String levelId = MapTool.getString(params, "levelId", "");
        String lockKey = MapTool.getString(params, "lockKey", "");
        CardLevel cardLevel = cardLevelMapper.selectOne(new LambdaQueryWrapper<CardLevel>()
                .eq(CardLevel::getId, levelId)
                .eq(CardLevel::getLockKey, lockKey));

        // 进行解锁
        if (cardLevel != null) {
            cardLevel.setLockUserId("");
            cardLevel.setLockTime(LocalDateTime.of(2000, 1, 1, 0, 0));
            cardLevel.setLockDuration(0);
            cardLevel.setLockKey("0");
            if (cardLevelMapper.update(cardLevel, new LambdaQueryWrapper<CardLevel>()
                    .eq(CardLevel::getId, levelId)
                    .eq(CardLevel::getLockKey, lockKey)) > 0)
                return Tuples.of(true, "解锁成功");
        }
        return Tuples.of(false, "解锁失败");
    }
}
