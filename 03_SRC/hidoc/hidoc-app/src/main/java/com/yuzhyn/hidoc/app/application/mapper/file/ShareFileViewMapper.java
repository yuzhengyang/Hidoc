package com.yuzhyn.hidoc.app.application.mapper.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.HidocFileView;
import com.yuzhyn.hidoc.app.application.entity.file.ShareFileView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShareFileViewMapper extends BaseMapper<HidocFileView> {

    /**
     * 查询共享文件
     *
     * @param userId
     * @return
     */
    @Select("SELECT t1.file_name, t1.user_id, t4.real_name, t1.create_time, t1.expiry_time, t1.uname, t1.download_time, t1.download_count, t3.ext, t3.size\n" +
            "FROM file_cursor t1\n" +
            "LEFT JOIN file_bucket t2 ON t1.bucket_id=t2.id\n" +
            "LEFT JOIN file t3 ON t1.file_id=t3.id\n" +
            "LEFT JOIN sys_user t4 ON t1.user_id=t4.id\n" +
            "WHERE t2.name='.share' AND t1.is_delete=false\n" +
            "ORDER BY t1.create_time DESC\n" +
            "LIMIT #{size} OFFSET (#{current}-1)*#{size}")
    List<ShareFileView> selectFiles(@Param("current") int current, @Param("size") int size);

    @Select("SELECT COUNT(1) AS count\n" +
            "FROM file_cursor t1\n" +
            "LEFT JOIN file_bucket t2 ON t1.bucket_id=t2.id\n" +
            "LEFT JOIN file t3 ON t1.file_id=t3.id\n" +
            "WHERE t2.name='.share' AND t1.is_delete=false\n")
    Long selectFilesCount();
}
