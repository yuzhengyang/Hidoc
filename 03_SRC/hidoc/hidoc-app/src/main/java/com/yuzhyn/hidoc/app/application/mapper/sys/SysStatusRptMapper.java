package com.yuzhyn.hidoc.app.application.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.HidocFileView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface SysStatusRptMapper extends BaseMapper {


    @Select("SELECT TO_CHAR(BEGIN_TIME, 'yyyy-mm-dd hh24:mi'::text) BEGIN_TIME, ELAPSED_TIME AS ELAPSED_TIME\n" +
            "FROM SYS_ACCESS_LOG\n" +
            "WHERE BEGIN_TIME > #{beginDate}\n" +
            "\tAND BEGIN_TIME < #{endDate}\n" +
            "\tAND ELAPSED_TIME < (100 * 1000)\n" +
            "ORDER BY BEGIN_TIME")
    List<Map> elapsedTime(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);
}
