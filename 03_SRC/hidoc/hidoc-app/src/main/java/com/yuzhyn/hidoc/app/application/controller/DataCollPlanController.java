package com.yuzhyn.hidoc.app.application.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.datacoll.DataCollPlan;
import com.yuzhyn.hidoc.app.application.mapper.datacoll.DataCollPlanMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("datacollplan")
public class DataCollPlanController {

    @Autowired
    DataCollPlanMapper dataCollPlanMapper;

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name", "description", "startTime", "stopTime", "isEnable")) {
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            LocalDateTime startTime = MapTool.getLocalDateTime(params, "startTime", "");
            LocalDateTime stopTime = MapTool.getLocalDateTime(params, "stopTime", "");
            Boolean isEnable = MapTool.getBoolean(params, "isEnable", false);

            if (StringTool.ok(name, description)) {
                DataCollPlan plan = new DataCollPlan();
                plan.setId(R.SnowFlake.nexts());
                plan.setName(name);
                plan.setDescription(description);
                plan.setStartTime(startTime.toLocalDate());
                plan.setStopTime(stopTime.toLocalDate());
                plan.setIsEnable(isEnable);
                plan.setDataCount(0L);
                plan.setToken(UUIDTool.getId64());
                plan.setCreateUserId(CurrentUserManager.getUser().getId());
                plan.setOwnerUserId(CurrentUserManager.getUser().getId());
                plan.setIsDelete(false);
                plan.setCreateTime(LocalDateTime.now());
                plan.setUpdateTime(LocalDateTime.now());
                int flag = dataCollPlanMapper.insert(plan);
                if (flag > 0) {
                    return ResponseData.okData("plan", plan);
                }
            }
        }
        return ResponseData.error("创建失败，请填写完整信息");
    }

    @PostMapping("edit")
    public ResponseData edit(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name", "description", "startTime", "stopTime", "isEnable")) {
            String id = MapTool.get(params, "id", "").toString();
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            LocalDateTime startTime = MapTool.getLocalDateTime(params, "startTime", "");
            LocalDateTime stopTime = MapTool.getLocalDateTime(params, "stopTime", "");
            Boolean isEnable = MapTool.getBoolean(params, "isEnable", false);

            if (StringTool.ok(id, name)) {
                DataCollPlan plan = dataCollPlanMapper.selectById(id);
                if (plan != null) {
                    // 非所属者不能编辑
                    if (!plan.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("编辑失败，非所属者不能编辑");
                    }
                    plan.setName(name);
                    plan.setDescription(description);
                    plan.setStartTime(startTime.toLocalDate());
                    plan.setStopTime(stopTime.toLocalDate());
                    plan.setIsEnable(isEnable);
                    plan.setUpdateTime(LocalDateTime.now());

                    int flag = dataCollPlanMapper.updateById(plan);
                    if (flag > 0) {
                        return ResponseData.okData("plan", plan);
                    }
                }
            }
        }
        return ResponseData.error("编辑失败，请填写完整信息");
    }


    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "token")) {
            String id = MapTool.get(params, "id", "").toString();
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(id, token)) {
                DataCollPlan plan = dataCollPlanMapper.selectById(id);
                if (plan != null) {
                    // 非所属者不能删除
                    if (!plan.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("删除失败，非所属者不能删除");
                    }
                    // 存在数据不能删除
                    if (plan.getDataCount() > 0) {
                        return ResponseData.error("删除失败，存在数据不能删除");
                    }

                    plan.setIsDelete(true);
                    int flag = dataCollPlanMapper.updateById(plan);
                    if (flag > 0) {
                        return ResponseData.okData("plan", plan);
                    }
                }
            }
        }
        return ResponseData.error("删除失败，请重新选择");
    }

    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        List<DataCollPlan> plans = dataCollPlanMapper.selectList(new LambdaQueryWrapper<DataCollPlan>()
                .eq(DataCollPlan::getOwnerUserId, CurrentUserManager.getUser().getId())
                .eq(DataCollPlan::getIsDelete, false).orderByDesc(DataCollPlan::getCreateTime).orderByDesc(DataCollPlan::getStopTime));
        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("plans", plans);
        return responseData;
    }

    @PostMapping("enable")
    public ResponseData enable(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "enable")) {
            String id = MapTool.get(params, "id", "").toString();
            Boolean enable = MapTool.getBoolean(params, "enable", false);

            if (StringTool.ok(id)) {
                DataCollPlan plan = dataCollPlanMapper.selectById(id);
                if (plan != null) {
                    plan.setIsEnable(enable);
                    int flag = dataCollPlanMapper.updateById(plan);
                    if (flag > 0) {
                        return ResponseData.okData("plan", plan);
                    }
                }
            }
        }
        return ResponseData.error("处理失败，请重新选择");
    }
}
