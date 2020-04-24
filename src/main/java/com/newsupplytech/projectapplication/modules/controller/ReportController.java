package com.newsupplytech.projectapplication.modules.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.utils.DateUtil;
import com.newsupplytech.projectapplication.comm.utils.SessionUserInfo;
import com.newsupplytech.projectapplication.comm.utils.StringUtils;
import com.newsupplytech.projectapplication.modules.entity.ApplicationRecord;
import com.newsupplytech.projectapplication.modules.entity.DepartmentUser;
import com.newsupplytech.projectapplication.modules.entity.EpspUser;
import com.newsupplytech.projectapplication.modules.entity.ProjectInformation;
import com.newsupplytech.projectapplication.modules.service.ApplicationRecordService;
import com.newsupplytech.projectapplication.modules.service.ProjectInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags="统计")
@RestController
@RequestMapping("/report")
public class ReportController extends BaseController {
    @Autowired
    private ApplicationRecordService applicationRecordService;
    @Autowired
    private ProjectInformationService projectInformationService;

    @GetMapping(value = "/applyReportByMonth")
    @ApiOperation(value = "按时间分组统计申请数量", notes = "按时间分组统计申请数量")
    public RequestResult taskReportByMonth(@RequestParam Map applicationRecord, HttpServletRequest req) {
        QueryWrapper<ApplicationRecord> queryWrapper = new QueryWrapper();
        queryWrapper.ne("nar.is_del","1");
        queryWrapper.ne("nar.status",ApplicationRecord.Status.DRAFT);
        SessionUserInfo sessionUserInfo = currentUserInfo();
        queryWrapper=otherSql(queryWrapper,sessionUserInfo);
        List<Map> list = applicationRecordService.applyReportByMonth(StringUtils.isNotEmpty(applicationRecord.get("year"))?
        applicationRecord.get("year").toString():null,queryWrapper);
        //按月分组查询
        Date starttm = DateUtil.getYearStartTime(new Date());
        Date endtm = DateUtil.getYearEndTime(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(starttm);
        List<Date> results = new ArrayList<>();
        List mapList=new ArrayList<>();
        while (calendar.getTime().before(endtm)){
            Date st = calendar.getTime();
            results.add(st);
            calendar.add(Calendar.MONTH, 1);
        }
        int k = 1;
        for(Date s:results){
            Map<String,Object> map= Maps.newHashMap();
            map.put("item",k+"月");
            map.put("value",0);
            for(Map m:list){
                if(m.containsKey("item")&&m.get("item").equals(DateUtil.toStr("yyyy-MM",s))&&StringUtils.isNotEmpty(m.get("count"))){
                    map.put("value",m.get("count"));
                    break;
                }
            }
            mapList.add(map);
            ++k;
        }
        return RequestResult.ok(mapList);
    }
    @GetMapping(value = "/applyReportByStreet")
    @ApiOperation(value = "按镇街分组统计申请数量", notes = "按镇街分组统计申请数量")
    public RequestResult applyReportByStreet(@RequestParam Map applicationRecord, HttpServletRequest req) {
        QueryWrapper<ApplicationRecord> queryWrapper = new QueryWrapper();
        SessionUserInfo sessionUserInfo = currentUserInfo();
        queryWrapper.ne("nar.is_del","1");
        queryWrapper.ne("nar.status",ApplicationRecord.Status.DRAFT);
        queryWrapper=otherSql(queryWrapper,sessionUserInfo);
        List<Map> list = applicationRecordService.applyReportByStreet(StringUtils.isNotEmpty(applicationRecord.get("street"))?
                applicationRecord.get("street").toString():null,queryWrapper);
        List mapList=new ArrayList<>();
        for(Map map:list){
            Map<String,Object> m= Maps.newHashMap();
            m.put("item",map.get("item"));
            m.put("value",map.get("count"));
            mapList.add(m);
        }
        return RequestResult.ok(mapList);
    }
    @GetMapping(value = "/reportCount")
    @ApiOperation(value = "统计数量", notes = "统计数量")
    public RequestResult reportCount(HttpServletRequest req) {
        Map map = Maps.newHashMap();
        SessionUserInfo sessionUserInfo = currentUserInfo();
        //累计申报
        QueryWrapper queryWrapper = new QueryWrapper<ApplicationRecord>();
        queryWrapper.ne("nar.is_del","1");
        queryWrapper.ne("nar.status",ApplicationRecord.Status.DRAFT);
        queryWrapper=otherSql(queryWrapper,sessionUserInfo);
        map.put("first",applicationRecordService.count(queryWrapper));
        //申报企业
        map.put("second",applicationRecordService.applyCompanyCount(queryWrapper));
        //开通业务
        queryWrapper = new QueryWrapper<ProjectInformation>();
        if(!sessionUserInfo.getEpspUser().getPer().contains("managerApplyAdmin")) {
            queryWrapper.ne("nar.is_del", "1");
            queryWrapper = otherSql(queryWrapper, sessionUserInfo);
            map.put("third", projectInformationService.countByPer(queryWrapper));
        }else{
            queryWrapper.ne("is_del", "1");
            map.put("third", projectInformationService.count(queryWrapper));
        }
        //已通过
        queryWrapper = new QueryWrapper<ApplicationRecord>();
        queryWrapper.eq("nar.status",ApplicationRecord.Status.PASSED);
        queryWrapper.ne("nar.is_del","1");
        queryWrapper=otherSql(queryWrapper,sessionUserInfo);
        map.put("fourth",applicationRecordService.count(queryWrapper));
        return RequestResult.ok(map);
    }
    @GetMapping(value = "/selectDates")
    @ApiOperation(value = "申请日期查询", notes = "申请日期查询")
    public RequestResult selectDates(HttpServletRequest req) {
        QueryWrapper<ApplicationRecord> queryWrapper = new QueryWrapper();
        SessionUserInfo sessionUserInfo = currentUserInfo();
        queryWrapper=otherSql(queryWrapper,sessionUserInfo);
        return RequestResult.ok(applicationRecordService.selectDates(queryWrapper));
    }

    private QueryWrapper<ApplicationRecord> otherSql(QueryWrapper<ApplicationRecord> queryWrapper,SessionUserInfo sessionUserInfo){
        EpspUser epspUser = sessionUserInfo.getEpspUser();
        DepartmentUser departmentUser = sessionUserInfo.getDepartmentUser();
        if (epspUser.getOrgid().equals(SessionUserInfo.ORG_ID_DEPART) && !sessionUserInfo.getEpspUser().getPer().contains("managerApplyAdmin")) {
            //新增项目时选择的科室部门，该部门人员能够看到此项目的所有申请记录
            String deptRightSql = "select 1 from nst_project_information m where m.id = nar.project_id and m.belong_dept_id = " + departmentUser.getDptid();
            //申请流转到该部门或者该人员时，他们能够看到此申请记录，并且在他们处理后，仍然能查看此记录
            String existsExecuteSql = "select 1 from nst_flow_execute_record n where n.application_id = nar.id and (n.executor_id = '" + epspUser.getUserid()
                    + "' or n.executor_id like '%," + epspUser.getUserid() + ",%' or n.executor_id like '%," + epspUser.getUserid()
                    + "' or n.executor_id like '" + epspUser.getUserid() + ",%' or n.enforce_depart = '" + departmentUser.getDptid() + "' )";
            queryWrapper.and(wrapper -> wrapper.exists(deptRightSql)
                    .or().exists(existsExecuteSql));
        }
        return queryWrapper;
    }
}
