package com.yuzhyn.hidoc.app.application.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.SysFile;
import com.yuzhyn.hidoc.app.application.entity.SysFileBucket;
import com.yuzhyn.hidoc.app.application.entity.SysFileCursor;
import com.yuzhyn.hidoc.app.application.mapper.SysFileBucketMapper;
import com.yuzhyn.hidoc.app.application.mapper.SysFileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.SysFileMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.yuzhyn.azylee.core.datas.collections.MapTool;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("bucket")
public class FileBucketController {

    @Autowired
    SysFileBucketMapper sysFileBucketMapper;

    @Autowired
    SysFileCursorMapper sysFileCursorMapper;

    /**
     * 查看文件桶列表
     *
     * @return
     */
    @PostMapping({"list"})
    public ResponseData list(@RequestBody Map<String, Object> params) {
        List<SysFileBucket> list = sysFileBucketMapper.selectList(null);
        return ResponseData.okData(list);
    }

    @PostMapping({"create"})
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name", "description")) {
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            Boolean isOpen = MapTool.getBoolean(params, "isOpen", false);

            SysFileBucket fileBucket = new SysFileBucket();
            fileBucket.setId(R.SnowFlake.nexts());
            fileBucket.setUserId(CurrentUserManager.getUser().getId());
            fileBucket.setName(name);
            fileBucket.setIsOpen(isOpen);

            int flag = sysFileBucketMapper.insert(fileBucket);
            if (flag > 0) {
                return ResponseData.okData("fileBucket", fileBucket);
            }
        }
        return ResponseData.error("创建失败，请填写完整信息");
    }

    @PostMapping({"edit"})
    public ResponseData edit(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "name", "description")) {
            String id = MapTool.get(params, "id", "").toString();
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            Boolean isOpen = MapTool.getBoolean(params, "isOpen", false);

            SysFileBucket fileBucket = sysFileBucketMapper.selectById(id);
            if (StringTool.ok(name)) fileBucket.setName(name);
            fileBucket.setIsOpen(isOpen);

            int flag = sysFileBucketMapper.updateById(fileBucket);
            if (flag > 0) {
                return ResponseData.okData("fileBucket", fileBucket);
            }
        }
        return ResponseData.error("更新失败，请填写完整信息");
    }

    @PostMapping({"delete"})
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();

            SysFileBucket fileBucket = sysFileBucketMapper.selectById(id);
            int cursorCount = sysFileCursorMapper.selectCount(new LambdaQueryWrapper<SysFileCursor>().eq(SysFileCursor::getBucketId, id));

            if (fileBucket != null) {
                if (cursorCount > 0) {
                    return ResponseData.error("删除失败，文件桶中存在文件");
                } else {

                    int flag = sysFileBucketMapper.deleteById(id);
                    if (flag > 0) {
                        return ResponseData.okData("fileBucket", fileBucket);
                    }
                }

            } else {
                return ResponseData.error("删除失败，不存在的文件桶");
            }
        }
        return ResponseData.error("更新失败，请重新选择");
    }
}
