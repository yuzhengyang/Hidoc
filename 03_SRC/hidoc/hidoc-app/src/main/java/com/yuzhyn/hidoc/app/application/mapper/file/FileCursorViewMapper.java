package com.yuzhyn.hidoc.app.application.mapper.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursorView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileCursorViewMapper extends BaseMapper<FileCursorView> {

    /**
     * 查询文件清单（重名文件视为不同版本）
     * @param bucketId
     * @return
     */
    @Select("SELECT " +
            "        t1.* , " +
            "        t2.history_count, " +
            "        t3.\"name\" AS bucket_name, " +
            "        t4.url_prefix  " +
            "FROM " +
            "        file_cursor t1 " +
            "        INNER JOIN ( " +
            "        SELECT " +
            "                bucket_id, " +
            "                file_name, " +
            "                MAX ( \"version\" ) current_version, " +
            "                COUNT ( \"version\" ) history_count  " +
            "        FROM " +
            "                PUBLIC.file_cursor  " +
            "        WHERE " +
            "                bucket_id = #{bucketId}  " +
            "        GROUP BY " +
            "                bucket_id, " +
            "                file_name  " +
            "        ) t2 ON t1.bucket_id = t2.bucket_id " +
            "        INNER JOIN file_bucket t3 ON t1.bucket_id = t3.\"id\" " +
            "        INNER JOIN sys_user_file_conf t4 ON t1.user_id = t4.user_id  " +
            "        AND t1.file_name = t2.file_name  " +
            "        AND t1.VERSION = t2.current_version")
    List<FileCursorView> selectFiles(@Param("bucketId") String bucketId);
}
