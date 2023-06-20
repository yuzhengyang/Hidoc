package com.yuzhyn.hidoc.app.application.mapper.doc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.doc.DocHistoryLite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DocHistoryLiteMapper extends BaseMapper<DocHistoryLite> {

    @Select("SELECT dh.id, dh.create_time AS \"createTime\", su.real_name AS \"realName\" " +
            "FROM doc_history dh LEFT JOIN sys_user su ON dh.update_user_id = su.\"id\" WHERE doc_id = #{docId} " +
            "ORDER BY dh.create_time DESC, dh.id DESC LIMIT 1000")
    List<Map> baseList(@Param("docId") String docId);
}