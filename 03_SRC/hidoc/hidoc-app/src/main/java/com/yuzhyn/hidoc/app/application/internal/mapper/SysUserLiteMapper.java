package com.yuzhyn.hidoc.app.application.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.internal.entity.SysUser;
import com.yuzhyn.hidoc.app.application.internal.entity.SysUserLite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserLiteMapper extends BaseMapper<SysUserLite> {
}
