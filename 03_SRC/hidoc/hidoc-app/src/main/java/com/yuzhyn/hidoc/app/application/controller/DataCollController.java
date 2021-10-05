package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.datacoll.DataColl;
import com.yuzhyn.hidoc.app.application.entity.datacoll.DataCollPlan;
import com.yuzhyn.hidoc.app.application.mapper.datacoll.DataCollMapper;
import com.yuzhyn.hidoc.app.application.mapper.datacoll.DataCollPlanMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("datacoll")
public class DataCollController {

    @Autowired
    DataCollMapper dataCollMapper;

    @Autowired
    DataCollPlanMapper dataCollPlanMapper;

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        LocalDateTime now = LocalDateTime.now();
        if (MapTool.ok(params, "createTime")) {
            String token = MapTool.get(params, "token", "").toString();
            String dataSource = MapTool.get(params, "dataSource", "").toString();
            String clientType = MapTool.get(params, "clientType", "").toString();
            String mac = MapTool.get(params, "mac", "").toString();
            String senderId = MapTool.get(params, "senderId", "").toString();
            String senderName = MapTool.get(params, "senderName", "").toString();
            String data = MapTool.get(params, "data", "").toString();
            LocalDateTime createTime = MapTool.getLocalDateTime(params, "createTime", "");

            JSONObject jsonObject = JSONObject.parseObject(data);
            if (jsonObject == null) {
                return ResponseData.error("创建失败，提交的数据为空");
            }

            if (StringTool.ok(token, mac, senderId, senderName, data) && createTime != null) {
                // 查询收集计划
                // 条件：1、token相同 2、可用 3、没删除 4、开始时间<=now 5、结束时间>=now 6、开始时间<=数据创建时间 7、结束时间>=数据创建时间
                DataCollPlan plan = dataCollPlanMapper.selectOne(new LambdaQueryWrapper<DataCollPlan>().eq(DataCollPlan::getToken, token)
                        .eq(DataCollPlan::getIsEnable, true).eq(DataCollPlan::getIsDelete, false)
                        .le(DataCollPlan::getStartTime, now).ge(DataCollPlan::getStopTime, now)
                        .le(DataCollPlan::getStartTime, createTime).ge(DataCollPlan::getStopTime, createTime));
                if (plan != null) {
                    DataColl dataColl = new DataColl();
                    dataColl.setId(R.SnowFlake.nexts());
                    dataColl.setCreateTime(createTime);
                    dataColl.setPlanId(plan.getId());
                    dataColl.setDataSource(dataSource);
                    dataColl.setClientType(clientType);
                    dataColl.setIp(CurrentUserManager.ip.get());
                    dataColl.setMac(mac);
                    dataColl.setSenderId(senderId);
                    dataColl.setSenderName(senderName);
                    dataColl.setData(jsonObject);

                    int flag = dataCollMapper.insert(dataColl);
                    if (flag > 0) {

                        plan.setDataCount(plan.getDataCount() + 1);
                        dataCollPlanMapper.updateById(plan);

                        return ResponseData.ok();
                    }
                } else {
                    return ResponseData.error("创建失败，提交的数据不符合收集计划要求（请检查：1、token 2、状态可用 3、未删除 4、收集时间有效 5、数据时间有效）");
                }
            }
        }
        return ResponseData.error("创建失败，提交的信息不完整");
    }

    /**
     * 可使用PG库jsonb特性查询
     * SELECT *,data->>'funName' AS funName FROM data_coll WHERE (data->>'times')::int4 > 30
     *
     * @param params
     * @return
     */
    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        List<DataColl> datas = dataCollMapper.selectList(null);
        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("datas", datas);
        return responseData;
    }
}
