//package com.yuzhyn.hidoc.app.system.ibatis.handler;
//
//import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import org.apache.ibatis.type.MappedTypes;
//import org.postgresql.util.PGobject;
//
//import java.lang.reflect.Field;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//@MappedTypes({Object.class})
//public class JsonbTypeHandlerNew extends FastjsonTypeHandler {
//
//    public JsonbTypeHandlerNew(Class<?> type) {
//        super(type);
//    }
//
//    // 自3.5.6版本开始支持泛型,需要加上此构造.
//    public JsonbTypeHandlerNew(Class<?> type, Field field) {
//        super(type, field);
//    }
//
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
//        if (ps != null) {
//            PGobject jsonObject = new PGobject();
//            jsonObject.setType("jsonb");
//            jsonObject.setValue(toJson(parameter));
//            ps.setObject(i, jsonObject);
//        }
//    }
//}