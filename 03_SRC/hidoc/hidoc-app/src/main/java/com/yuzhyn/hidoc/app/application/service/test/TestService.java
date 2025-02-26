package com.yuzhyn.hidoc.app.application.service.test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestService {

    @Autowired
    SysAccessLogMapper sysAccessLogMapper;

    /**
     * 流式查询测试示例
     * 查询数据库中，符合条件的前100万条数据
     *
     * @return
     */
    public String streamQueryByPage() {
        Page<SysAccessLog> page = new Page<>(1, 1000000);
        sysAccessLogMapper.selectList(page, Wrappers.emptyWrapper(), new ResultHandler<>() {
            int count = 0;

            @Override
            public void handleResult(ResultContext<? extends SysAccessLog> resultContext) {
                SysAccessLog h2User = resultContext.getResultObject();
                System.out.println("当前处理第 " + (++count) + " 条记录，共有 " + resultContext.getResultCount() + " 条记录");
            }
        });
        return "";
    }
}
