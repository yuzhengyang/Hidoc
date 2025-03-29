package com.yuzhyn.hidoc.app.application.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.hidoc.app.application.entity.sys.SysLock;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysLockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysLockService {
    @Autowired
    private SysLockMapper sysLockMapper;

    public String lock(String key, Long expireSeconds, String userId) {
        clean();
        String val = "";
        try {
            SysLock sysLock = new SysLock();
            sysLock.setKey(key);
            sysLock.setVal(UUIDTool.getId64());
            sysLock.setUserId(userId);
            sysLock.setCreateTime(LocalDateTime.now());
            sysLock.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
            sysLock.setLockCount(1L);
            int flag = sysLockMapper.insert(sysLock);
            if (flag > 0) val = sysLock.getVal();
        } catch (Exception ex) {
        }
        return val;
    }

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
