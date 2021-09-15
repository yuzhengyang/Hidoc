package com.yuzhyn.hidoc.app.application.customization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.customization.entity.Demo;
import com.yuzhyn.hidoc.app.application.customization.entity.DocHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocHistoryMapper extends BaseMapper<DocHistory> {

}