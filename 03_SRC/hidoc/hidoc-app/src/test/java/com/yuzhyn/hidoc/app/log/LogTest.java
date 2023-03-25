package com.yuzhyn.hidoc.app.log;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class LogTest {

    @Autowired
    DocAccessLogMapper docAccessLogMapper;


    /**
     * 测试字符串转大写
     */
    @Test
    public void logQueryTest() {
        List<DocAccessLog> list = docAccessLogMapper.selectList(null);
        if (list != null) {
            log.info("有数据");
        } else {
            log.info("无数据");
        }
    }

}
