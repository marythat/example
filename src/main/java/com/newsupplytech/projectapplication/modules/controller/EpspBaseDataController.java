package com.newsupplytech.projectapplication.modules.controller;

import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.utils.SessionUserInfo;
import com.newsupplytech.projectapplication.modules.entity.CompanyBusiness;
import com.newsupplytech.projectapplication.modules.entity.IndustryType;
import com.newsupplytech.projectapplication.modules.service.EpspBaseDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/epspBaseData")
@Api(tags = "企莞家基本信息获取")
public class EpspBaseDataController extends BaseController {
    @Autowired
    private EpspBaseDataService epspBaseDataService;

    @GetMapping("companyBusinessList")
    @ApiOperation("企业关联标签")
    public RequestResult companyBusinessList(){
        List<CompanyBusiness> businessList = epspBaseDataService.companyBusinessList();
        return RequestResult.ok(businessList);
    }

    @ApiOperation("行业分类")
    @GetMapping("industryTypeMapperList")
    public RequestResult industryTypeMapperList(){
        List<IndustryType> industryTypeList = epspBaseDataService.industryTypeMapperList();
        return RequestResult.ok(industryTypeList);
    }

    @ApiOperation("部门树")
    @GetMapping("departmentTree")
    public RequestResult departmentTree(){
        //TODO
        return RequestResult.ok(epspBaseDataService.departmentTree());
    }

    @ApiOperation("根据id查询部门")
    @GetMapping("getDepartments")
    public RequestResult getDepartments(@RequestParam(name="dptIds") String dptIds){
        //TODO
        return RequestResult.ok(epspBaseDataService.getDepartments(dptIds));
    }

    @ApiOperation("部门用户")
    @GetMapping("departmentUser")
    public RequestResult departmentUser(@RequestParam(name="dptId") Long dptId,
                                        @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        //TODO
        return RequestResult.ok(epspBaseDataService.departmentUser(dptId,pageNo,pageSize));
    }
    @ApiOperation("根据ids用户")
    @GetMapping("searchUser")
    public RequestResult searchUser(@RequestParam(name="ids") String ids,
                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        //TODO
        return RequestResult.ok(epspBaseDataService.searchUser(ids));
    }
    @ApiOperation("企业用户")
    @GetMapping("companyPersons")
    public RequestResult companyPersons(HttpServletRequest request){
        //TODO
        return RequestResult.ok(epspBaseDataService.getCompanyPersons((SessionUserInfo) request.getSession().getAttribute(SessionUserInfo.SESSION_USER_INFO_KEY)));
    }
    @ApiOperation("查询需要补充的信息")
    @GetMapping("getTips")
    public RequestResult getTips(HttpServletRequest request){
        SessionUserInfo sessionUserInfo = currentUserInfo();
        return epspBaseDataService.getTips(sessionUserInfo.getCompanyUser().getCompanyid(),request.getParameter("field"));
    }
}
