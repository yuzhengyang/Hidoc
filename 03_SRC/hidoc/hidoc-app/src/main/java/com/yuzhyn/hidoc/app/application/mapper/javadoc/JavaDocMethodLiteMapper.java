package com.yuzhyn.hidoc.app.application.mapper.javadoc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMethod;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMethodLite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JavaDocMethodLiteMapper extends BaseMapper<JavaDocMethodLite> {
}
