package com.yuzhyn.hidoc.app.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.Doc;
import com.yuzhyn.hidoc.app.application.entity.DocLite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocLiteMapper extends BaseMapper<DocLite> {
}
