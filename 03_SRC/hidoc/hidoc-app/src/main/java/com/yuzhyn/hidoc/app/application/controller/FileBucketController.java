package com.yuzhyn.hidoc.app.application.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.regexs.RegexPattern;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.FileBucket;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.mapper.file.FileBucketMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("bucket")
public class FileBucketController {

    @Autowired
    FileBucketMapper fileBucketMapper;

    @Autowired
    FileCursorMapper fileCursorMapper;

    /**
     * 查看文件桶列表
     *
     * @return
     */
    @PostMapping({"list"})
    public ResponseData list(@RequestBody Map<String, Object> params) {
        List<FileBucket> list = fileBucketMapper.selectList(new LambdaQueryWrapper<FileBucket>().eq(FileBucket::getUserId, CurrentUserManager.getUser().getId()));
        list = list.stream().filter(x->!x.getName().contains(".")).collect(Collectors.toList());
        return ResponseData.okData(list);
    }

    @PostMapping({"create"})
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name", "description")) {
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            Boolean isOpen = MapTool.getBoolean(params, "isOpen", false);

            if (name.length() == 0 || !RegexPattern.GENERAL.isMatch(name)) {
                return ResponseData.error("账号不符合规则，仅支持字母数字下划线组合");
            }

            FileBucket fileBucket = new FileBucket();
            fileBucket.setId(R.SnowFlake.nexts());
            fileBucket.setUserId(CurrentUserManager.getUser().getId());
            fileBucket.setName(name);
            fileBucket.setIsOpen(isOpen);

            int flag = fileBucketMapper.insert(fileBucket);
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

            if (name.length() == 0 || !RegexPattern.GENERAL.isMatch(name)) {
                return ResponseData.error("账号不符合规则，仅支持字母数字下划线组合");
            }

            FileBucket fileBucket = fileBucketMapper.selectById(id);
            fileBucket.setName(name);
            fileBucket.setIsOpen(isOpen);

            int flag = fileBucketMapper.updateById(fileBucket);
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

            FileBucket fileBucket = fileBucketMapper.selectById(id);
            int cursorCount = fileCursorMapper.selectCount(new LambdaQueryWrapper<FileCursor>().eq(FileCursor::getBucketId, id));

            if (fileBucket != null) {
                if (!fileBucket.getUserId().equals(CurrentUserManager.getUser().getId())) {
                    return ResponseData.error("删除失败，不能删除其他用户数据");
                }

                if (cursorCount > 0) {
                    return ResponseData.error("删除失败，文件桶中存在文件");
                } else {

                    int flag = fileBucketMapper.deleteById(id);
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
