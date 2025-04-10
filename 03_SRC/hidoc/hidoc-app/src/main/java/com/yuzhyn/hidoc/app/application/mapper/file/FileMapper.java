package com.yuzhyn.hidoc.app.application.mapper.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursorView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<File> {

    /**
     * 查询CURSOR已经删除，并且该文件所有引用都已删除
     *
     * @param deleteTime
     * @return
     */
    @Select("SELECT f.* \n" +
            "FROM ( SELECT file_id, COUNT(1) AS delete_count, MAX(delete_time) AS delete_time FROM file_cursor WHERE is_delete GROUP BY file_id ) t1\n" +
            "LEFT JOIN ( SELECT file_id, COUNT(1) AS cursor_count FROM file_cursor GROUP BY file_id ) t2 ON t1.file_id = t2.file_id\n" +
            "LEFT JOIN ( SELECT * FROM file WHERE is_delete = false ) f ON t1.file_id = f.id\n" +
            "WHERE f.id IS NOT NULL AND t1.delete_count = t2.cursor_count AND t1.delete_time <= #{deleteTime}")
    List<File> selectNoCursorFileList(@Param("deleteTime") LocalDateTime deleteTime);

    @Select("SELECT * FROM file WHERE is_delete = false AND ( check_time IS NULL OR check_time < #{checkTime} ) LIMIT #{limit}")
    List<File> selectNeedCheckFileList(@Param("checkTime") LocalDateTime checkTime, @Param("limit") int limit);

    @Select("SELECT * FROM file WHERE is_delete = false AND id IN (SELECT file_id FROM file_cursor WHERE is_delete = false)")
    List<File> selectHealthyFileList();
}
