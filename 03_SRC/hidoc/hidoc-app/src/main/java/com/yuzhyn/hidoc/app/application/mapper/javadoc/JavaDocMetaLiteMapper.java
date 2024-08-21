package com.yuzhyn.hidoc.app.application.mapper.javadoc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursorView;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMetaLite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JavaDocMetaLiteMapper extends BaseMapper<JavaDocMetaLite> {
}
