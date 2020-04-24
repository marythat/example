package com.newsupplytech.projectapplication.modules.controller;


import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.utils.SessionUserInfo;
import com.newsupplytech.projectapplication.modules.entity.*;
import com.newsupplytech.projectapplication.modules.service.ApplicationRecordService;
import com.newsupplytech.projectapplication.modules.service.EpspBaseDataService;
import com.newsupplytech.projectapplication.modules.service.FlowExecuteService;
import com.newsupplytech.projectapplication.modules.service.ProjectInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flowExecute")
@Api(tags = "流程执行")
public class FlowExecuteController extends BaseController {
    @Autowired
    private FlowExecuteService flowExecuteService;
    @Autowired
    private ApplicationRecordService applicationRecordService;
    @Autowired
    private EpspBaseDataService epspBaseDataService;
    @Autowired
    private ProjectInformationService projectInformationService;

    @ApiOperation("提交审批")
    @PostMapping("commit")
    public RequestResult commit(@RequestBody FlowExecuteRecord flowExecuteRecord){
        SessionUserInfo sessionUserInfo = currentUserInfo();
        EpspUser epspUser = sessionUserInfo.getEpspUser();
        if(epspUser.getOrgid().equals(SessionUserInfo.ORG_ID_DEPART)){
            String multipleSelectionIds=flowExecuteRecord.getExecutorId();
            flowExecuteRecord.setCreateId(epspUser.getUserid());
            flowExecuteRecord.setCreateName(epspUser.getUsername());
            flowExecuteRecord.setCreateBelongDept(sessionUserInfo.getDepartmentUser().getDptname());
            flowExecuteRecord.setCreatePhone(epspUser.getMobile());
            ApplicationRecord applicationRecord = applicationRecordService.getById(flowExecuteRecord.getApplicationId());
            Street street = epspBaseDataService.queryStreet(applicationRecord.getStreetId());
//            SessionUserInfo companyUser= epspBaseDataService.sessionUserInfo(Long.parseLong(applicationRecord.getCreateBy()));
            String companyUserPhone = applicationRecord.getCreatePerson();
            applicationRecord = flowExecuteService.commit(applicationRecord,flowExecuteRecord,multipleSelectionIds,street,companyUserPhone);
            return RequestResult.ok(applicationRecord);
        }else{
            return RequestResult.error("您没有该操作权限！");
        }

    }

}
