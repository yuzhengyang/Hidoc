package com.yuzhyn.hidoc.app.application.mapper.doc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollected;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface DocCollectedMapper extends BaseMapper<DocCollected> {

    /**
     * 更新文集的文档数量
     * @param collectedId
     * @return
     */
    @Update("<script>" +
            "UPDATE doc_collected SET doc_count = (SELECT COUNT(1) FROM doc WHERE is_delete = false AND collected_id = #{collectedId}) WHERE id = #{collectedId}" +
            "</script>")
    Integer updateDocCount(@Param("collectedId") String collectedId);
}
