package com.yuzhyn.hidoc.app.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.DocHistory;
import com.yuzhyn.hidoc.app.application.entity.DocHistoryLite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocHistoryLiteMapper extends BaseMapper<DocHistoryLite> {

}