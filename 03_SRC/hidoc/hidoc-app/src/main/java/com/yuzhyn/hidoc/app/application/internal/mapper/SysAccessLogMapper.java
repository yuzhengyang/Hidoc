package com.yuzhyn.hidoc.app.application.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.internal.entity.SysAccessLog;
import com.yuzhyn.hidoc.app.application.internal.entity.SysFileBucket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysAccessLogMapper extends BaseMapper<SysAccessLog> {
}
