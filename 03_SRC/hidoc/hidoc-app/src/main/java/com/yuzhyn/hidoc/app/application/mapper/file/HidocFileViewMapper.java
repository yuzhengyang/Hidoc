package com.yuzhyn.hidoc.app.application.mapper.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursorView;
import com.yuzhyn.hidoc.app.application.entity.file.HidocFileView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HidocFileViewMapper extends BaseMapper<HidocFileView> {

    /**
     * 查询Hidoc资源的列表信息
     *
     * @param userId
     * @return
     */
    @Select("SELECT T1.*,\n" +
            "\tT2.NAME AS COLLECTED_NAME,\n" +
            "\tT3.REAL_NAME AS COLLECTED_OWNER_NAME,\n" +
            "\tT4.SIZE\n" +
            "FROM FILE_CURSOR T1\n" +
            "LEFT JOIN DOC_COLLECTED T2 ON T1.COLLECTED_ID = T2.ID\n" +
            "LEFT JOIN SYS_USER T3 ON T2.OWNER_USER_ID = T3.ID\n" +
            "LEFT JOIN FILE T4 ON T1.FILE_ID = T4.ID\n" +
            "WHERE BUCKET_ID =\n" +
            "\t\t(SELECT ID\n" +
            "\t\t\tFROM FILE_BUCKET\n" +
            "\t\t\tWHERE NAME = '.hidoc'\n" +
            "\t\t\t\tAND USER_ID = #{userId})\n" +
            "\tAND T1.IS_DELETE = FALSE ORDER BY T1.CREATE_TIME DESC LIMIT #{size} OFFSET (#{current}-1)*#{size}")
    List<HidocFileView> selectFiles(@Param("userId") String userId, @Param("current") int current, @Param("size") int size);

    @Select("SELECT COUNT(1) AS count " +
            "FROM FILE_CURSOR T1\n" +
            "LEFT JOIN DOC_COLLECTED T2 ON T1.COLLECTED_ID = T2.ID\n" +
            "LEFT JOIN SYS_USER T3 ON T2.OWNER_USER_ID = T3.ID\n" +
            "LEFT JOIN FILE T4 ON T1.FILE_ID = T4.ID\n" +
            "WHERE BUCKET_ID =\n" +
            "\t\t(SELECT ID\n" +
            "\t\t\tFROM FILE_BUCKET\n" +
            "\t\t\tWHERE NAME = '.hidoc'\n" +
            "\t\t\t\tAND USER_ID = #{userId})\n" +
            "\tAND T1.IS_DELETE = FALSE")
    Long selectFilesCount(@Param("userId") String userId);
}
