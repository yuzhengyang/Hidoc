package com.yuzhyn.hidoc.app.application.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysAccessLogMapper extends BaseMapper<SysAccessLog> {

    /**
     * 使用流式查询获取数据，通过 ResultHandler 处理每一条记录
     * 这里必须使用新事务，否则会查询所有数据
     *
     * @param params 查询条件
     * @param resultHandler 回调处理
     */
    @Transactional
    @Select("SELECT * FROM sys_access_log LIMIT 1000000")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 500)
    @ResultType(SysAccessLog.class)
    void selectAllLog(Map<String, Object> params, ResultHandler<SysAccessLog> resultHandler);

    @Select("SELECT * FROM sys_access_log")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 500)
    @ResultType(SysAccessLog.class)
    Cursor<SysAccessLog> selectAllLog(Map<String, Object> params);
}
