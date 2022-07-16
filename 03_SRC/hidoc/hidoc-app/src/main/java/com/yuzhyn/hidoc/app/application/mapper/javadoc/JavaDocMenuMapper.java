package com.yuzhyn.hidoc.app.application.mapper.javadoc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMenu;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocProject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JavaDocMenuMapper extends BaseMapper<JavaDocMenu> {
}