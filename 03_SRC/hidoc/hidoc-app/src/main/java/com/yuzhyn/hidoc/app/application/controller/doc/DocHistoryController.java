package com.yuzhyn.hidoc.app.application.controller.doc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.Doc;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollected;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollectedMember;
import com.yuzhyn.hidoc.app.application.entity.doc.DocComment;
import com.yuzhyn.hidoc.app.application.entity.doc.DocHistory;
import com.yuzhyn.hidoc.app.application.entity.doc.DocHistoryLite;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.entity.doc.DocThumb;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCollectedMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCollectedMemberMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCommentMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocHistoryLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocHistoryMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocThumbMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysMachineStatusLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.service.DocAccessLogService;
import com.yuzhyn.hidoc.app.application.service.doc.DocParseService;
import com.yuzhyn.hidoc.app.common.enums.ResponseCode;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("dochistory")
public class DocHistoryController {

    @Autowired
    DocHistoryLiteMapper docHistoryLiteMapper;
    @Autowired
    DocHistoryMapper docHistoryMapper;

    @PostMapping("get")
    public ResponseData get(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            DocHistory docHistory = docHistoryMapper.selectById(id);

            ResponseData responseData = ResponseData.ok();
            responseData.putDataMap("docHistory", docHistory);
            return responseData;
        }
        return ResponseData.error();
    }

    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            List<Map> historyList = docHistoryLiteMapper.baseList(id);
            return ResponseData.okData(historyList);
        }
        return ResponseData.error();
    }

    @PostMapping("revert")
    public ResponseData revert(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "docId", "historyId")) {
            String docId = MapTool.get(params, "docId", "").toString();
            String historyId = MapTool.get(params, "historyId", "").toString();

            // 先锁定文档，防止此时有人编辑

            // 备份当前文档到历史

            // 修改当前文档为历史版本

            // 解除文档锁定
        }
        return ResponseData.error();
    }
}
