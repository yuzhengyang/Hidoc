package com.yuzhyn.hidoc.app.application.mapper.javadoc;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMetaSearchLite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JavaDocMetaSearchLiteMapper extends BaseMapper<JavaDocMetaSearchLite> {

    @Select("""
                SELECT java_doc_meta.*, COALESCE(java_doc_helpful.helpful_rate, 0) helpful_rate
                FROM java_doc_meta java_doc_meta 
                LEFT JOIN (SELECT meta_id, AVG(helpful_rate) helpful_rate FROM java_doc_helpful GROUP BY meta_id) java_doc_helpful ON java_doc_meta.id = java_doc_helpful.meta_id
                ${ew.customSqlSegment}
                ORDER BY helpful_rate DESC
                LIMIT #{limit}
                OFFSET #{offset}
                """)
    List<JavaDocMetaSearchLite> selectList(@Param(Constants.WRAPPER) Wrapper wrapper,
                                           @Param("limit") Long limit, @Param("offset") Long offset);
}
