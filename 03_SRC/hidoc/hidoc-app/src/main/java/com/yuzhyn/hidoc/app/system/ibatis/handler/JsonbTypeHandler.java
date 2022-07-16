package com.yuzhyn.hidoc.app.system.ibatis.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JsonbTypeHandler extends AbstractJsonTypeHandler<Object> {
    private static final PGobject jsonObject = new PGobject();
    private final Class<?> type;

    public JsonbTypeHandler(Class<?> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (ps != null) {
            jsonObject.setType("jsonb");
            jsonObject.setValue(JSON.toJSONString(parameter));
            ps.setObject(i, jsonObject);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object v = rs.getObject(columnName);
        return toFill(v);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object v = rs.getObject(columnIndex);
        return toFill(v);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object v = cs.getObject(columnIndex);
        return toFill(v);
    }

    @Override
    protected Object parse(String json) {
        return JSON.parseObject(json, this.type);
    }

    //必须将v转成 PGObject处理
    private Object toFill(Object v) {
        if (v != null && v instanceof PGobject) {
            PGobject p = (PGobject) v;
            String pv = p.getValue();
            if (StringTool.ok(pv) && (p.getType().equals("jsonb") || p.getType().equals("json"))) {
                return parse(p.getValue());
            }
        }
        return v;
    }

    @Override
    protected String toJson(Object obj) {
        return JSON.toJSONString(obj, new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty});
    }
}