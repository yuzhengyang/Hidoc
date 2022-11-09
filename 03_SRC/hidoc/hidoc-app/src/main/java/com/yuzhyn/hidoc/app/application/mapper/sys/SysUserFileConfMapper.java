package com.yuzhyn.hidoc.app.application.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SysUserFileConfMapper extends BaseMapper<SysUserFileConf> {
    /**
     * 更新空间限额
     * @param userId
     * @param value
     * @return
     */
    @Update("UPDATE sys_user_file_conf SET used_space = used_space + (${value}) WHERE user_id = '${userId}'")
    Long updateUsedSpace(@Param("userId") String userId, @Param("value") Long value);
}
