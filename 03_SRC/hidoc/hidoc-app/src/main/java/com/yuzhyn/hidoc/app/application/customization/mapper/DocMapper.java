package com.yuzhyn.hidoc.app.application.customization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.customization.entity.Doc;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocMapper extends BaseMapper<Doc> {
}
