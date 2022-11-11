package com.yuzhyn.hidoc.app.application.mapper.doc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollected;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface DocAccessLogMapper extends BaseMapper<DocAccessLog> {

    /**
     * 查询我的文档被阅读的记录
     * @param userId
     * @param docId
     * @return
     */
    @Select("<script>" +
            "SELECT " +
            "dal.ip, " +
            "dal.create_time AS \"readTime\", " +
            "COALESCE(su.real_name, '匿名用户') AS \"readUser\", " +
            "dc.name AS \"collectedName\", " +
            "d.title AS \"docTitle\", " +
            "su2.real_name AS \"createUser\", " +
            "su3.real_name AS \"ownerUser\" \n" +
            "FROM doc_access_log dal \n" +
            "LEFT JOIN sys_user su ON dal.user_id = su.id \n" +
            "LEFT JOIN doc_collected dc ON dal.collected_id = dc.id \n" +
            "LEFT JOIN doc d ON dal.doc_id = d.id \n" +
            "LEFT JOIN sys_user su2 ON dal.create_user_id = su2.id \n" +
            "LEFT JOIN sys_user su3 ON dal.owner_user_id = su3.id \n" +
            "<where>" +
            "(dal.create_user_id = #{userId} OR dal.owner_user_id = #{userId}) AND dal.user_id != #{userId} \n" +
            "<if test=\"docId != null and docId != '' \">" +
            "AND dal.doc_id = #{docId}" +
            "</if>" +
            "</where>" +
            "ORDER BY dal.create_time DESC LIMIT 200" +
            "</script>")
    List<Map> myDocReadLog(@Param("userId") String userId, @Param("docId") String docId);
}
