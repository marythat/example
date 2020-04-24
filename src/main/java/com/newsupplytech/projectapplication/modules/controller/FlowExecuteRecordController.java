package com.newsupplytech.projectapplication.modules.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.DefaultPage;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.utils.HttpQueryWrapper;
import com.newsupplytech.projectapplication.modules.entity.CommAttachment;
import com.newsupplytech.projectapplication.modules.entity.FlowExecuteRecord;
import com.newsupplytech.projectapplication.modules.service.CommAttachmentService;
import com.newsupplytech.projectapplication.modules.service.FlowExecuteRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/flowExecuteRecord")
@Api(tags = "流程执行记录")
public class FlowExecuteRecordController extends BaseController {
    @Autowired
    private FlowExecuteRecordService flowExecuteRecordService;

    @Autowired
    private CommAttachmentService commAttachmentService;

    /**
     * 分页查询 仅仅提供给申请的节点查询，默认获取条数为200，请务必传入applicationId的值
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "分页查询",notes = "仅仅提供给申请的节点查询，默认获取条数为200，请务必传入applicationId的值")
    public RequestResult list(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "200") Integer pageSize,
                              HttpServletRequest request) {
        QueryWrapper<FlowExecuteRecord> queryWrapper = HttpQueryWrapper.genQueryWrapper(request.getParameterMap());
        IPage<FlowExecuteRecord> page = new DefaultPage<>(pageNo, pageSize);
        flowExecuteRecordService.page(page, queryWrapper);
        List<FlowExecuteRecord> FlowExecuteRecordList=page.getRecords();
        if(FlowExecuteRecordList.size()>0){
            List<FlowExecuteRecord> flowExecuteRecordList_addCommAttachment=this.addCommAttachmentList(FlowExecuteRecordList);
            page.setRecords(flowExecuteRecordList_addCommAttachment);
        }
        return RequestResult.ok(page);
    }

    //获取执行过程中上传的ID值类型
    public List<FlowExecuteRecord> addCommAttachmentList(List<FlowExecuteRecord> list){
        List<FlowExecuteRecord> flowExecuteRecordAddList=new ArrayList<>();
           for (FlowExecuteRecord  flowExecuteRecord:list){
                List<CommAttachment> commAttachmentList=commAttachmentService.selectRelatedId("nst_flow_execute_record",flowExecuteRecord.getId());
                flowExecuteRecord.setCommAttachmentList(commAttachmentList); ;
                flowExecuteRecordAddList.add(flowExecuteRecord);
           }
        return flowExecuteRecordAddList;
    }
}
