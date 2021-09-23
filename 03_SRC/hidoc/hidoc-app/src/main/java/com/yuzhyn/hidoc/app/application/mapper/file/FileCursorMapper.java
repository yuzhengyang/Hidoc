package com.yuzhyn.hidoc.app.application.mapper.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileCursorMapper extends BaseMapper<FileCursor> {

    /**
     * 查询文件清单（重名文件视为不同版本）
     * @param bucketId
     * @return
     */
    @Select("SELECT\n" +
            "\tt1.* \n" +
            "FROM\n" +
            "\tfile_cursor t1\n" +
            "\tINNER JOIN ( SELECT bucket_id, file_name, MAX ( VERSION ) current_version FROM PUBLIC.file_cursor WHERE bucket_id = #{bucketId} GROUP BY bucket_id, file_name ) t2 ON t1.bucket_id = t2.bucket_id \n" +
            "\tAND t1.file_name = t2.file_name \n" +
            "\tAND t1.VERSION = t2.current_version")
    List<FileCursor> selectFiles(@Param("bucketId") String bucketId);
}
