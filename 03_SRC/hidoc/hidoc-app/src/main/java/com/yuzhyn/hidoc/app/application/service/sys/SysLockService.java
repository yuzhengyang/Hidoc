package com.yuzhyn.hidoc.app.application.service.sys;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.hidoc.app.application.entity.sys.SysLock;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysLockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SysLockService {

    @Autowired
    private SysLockMapper sysLockMapper;

    /**
     * 使用key创建锁，val会自动创建并在成功锁定后返回
     *
     * @param key
     * @param expireSeconds
     * @param userId
     * @return
     */
    public String lock(String key, Long expireSeconds, String userId) {
        String val = UUIDTool.getId64();
        if (lock(key, val, expireSeconds, userId)) {
            return val;
        }
        return "";
    }

    /**
     * 使用key和val来创建锁
     *
     * @param key
     * @param val
     * @param expireSeconds
     * @param userId
     * @return
     */
    public boolean lock(String key, String val, Long expireSeconds, String userId) {
        clean();
        try {
            SysLock sysLock = new SysLock();
            sysLock.setKey(key);
            sysLock.setVal(val);
            sysLock.setUserId(userId);
            sysLock.setCreateTime(LocalDateTime.now());
            sysLock.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
            sysLock.setLockCount(1L);
            int flag = sysLockMapper.insert(sysLock);
            if (flag > 0) {
                log.info(key + "：锁定成功：" + val);
                return true;
            } else {
                log.info(key + "：锁被占用");
                return false;
            }
        } catch (Exception ex) {
            log.info(key + "：锁定失败");
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 续期锁
     *
     * @param key
     * @param val
     * @param expireSeconds
     * @return
     */
    public boolean renew(String key, String val, Long expireSeconds) {
        clean();
        try {
            SysLock sysLock = sysLockMapper.selectOne(new LambdaQueryWrapper<SysLock>().eq(SysLock::getKey, key).eq(SysLock::getVal, val));
            if (sysLock != null) {
                sysLock.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
                sysLock.setLockCount(sysLock.getLockCount() + 1);
                int flag = sysLockMapper.updateById(sysLock);
                return flag > 0;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * 检查是否有锁
     *
     * @param key
     * @return
     */
    public boolean check(String key) {
        clean();
        try {
            boolean has = sysLockMapper.exists(new LambdaQueryWrapper<SysLock>().eq(SysLock::getKey, key));
            return has;
        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * 检查是否有锁
     *
     * @param key
     * @param val
     * @return
     */
    public boolean check(String key, String val) {
        clean();
        try {
            boolean has = sysLockMapper.exists(new LambdaQueryWrapper<SysLock>().eq(SysLock::getKey, key).eq(SysLock::getVal, val));
            return has;
        } catch (Exception ex) {
        }
        return false;
    }

    public boolean unlock(String key, String val) {
        clean();
        try {
            int flag = sysLockMapper.delete(new LambdaQueryWrapper<SysLock>().eq(SysLock::getKey, key).eq(SysLock::getVal, val));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void clean() {
        try {
            var now = LocalDateTime.now();
            List<SysLock> list = sysLockMapper.selectList(new LambdaQueryWrapper<SysLock>().select(SysLock::getKey).lt(SysLock::getExpireTime, now));
            List<String> keys = list.stream().map(SysLock::getKey).toList();
            sysLockMapper.deleteByIds(keys);
        } catch (Exception ex) {
        }
    }
}
