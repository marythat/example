package com.newsupplytech.projectapplication.modules.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Maps;
import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.DefaultPage;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.utils.HttpQueryWrapper;
import com.newsupplytech.projectapplication.comm.utils.SessionUserInfo;
import com.newsupplytech.projectapplication.comm.utils.StringUtils;
import com.newsupplytech.projectapplication.modules.entity.ApplicationRecord;
import com.newsupplytech.projectapplication.modules.entity.ProjectInformation;
import com.newsupplytech.projectapplication.modules.service.ProjectInformationService;
import com.newsupplytech.projectapplication.modules.vo.ProjectInfomationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/projectInformation")
@Api(tags = "项目信息")
public class ProjectInformationController extends BaseController {
    @Autowired
    private ProjectInformationService projectInformationService;

    @PostMapping("save")
    @ApiOperation("保存(新增/更新)")
    public RequestResult save(@RequestBody ProjectInfomationVo projectInfomationVo){
        try {
            projectInformationService.save(projectInfomationVo);
            return RequestResult.ok(projectInfomationVo.getProjectInformation());
        }catch (Exception e){
            log.error(e.toString());
            return RequestResult.error(e.getMessage());
        }
    }

    @GetMapping("list")
    @ApiOperation("分页查询")
    public RequestResult list(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                              HttpServletRequest request){
        QueryWrapper<ProjectInformation> queryWrapper = HttpQueryWrapper.genQueryWrapper(request.getParameterMap());
        queryWrapper.ne("npi.is_del","1");
        IPage<ProjectInformation> page = new DefaultPage<>(pageNo,pageSize);
        SessionUserInfo sessionUserInfo = currentUserInfo();
        //拥有managerProject或者是企业用户可查询项目列表
        if ((StringUtils.isNotEmpty(sessionUserInfo.getEpspUser().getPer())&&sessionUserInfo.getEpspUser().getPer().contains("managerProject"))
                ||sessionUserInfo.getEpspUser().getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)) {
            if(StringUtils.isNotEmpty(SessionUserInfo.ORG_ID_COMPANY)&&sessionUserInfo.getEpspUser().getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)){
                queryWrapper.eq("project_status","AVAILABLE");
            }
        }
        //若登入企业用户phone不为空则加入检索条件
        if(StringUtils.isNotEmpty(sessionUserInfo.getPhone())&&sessionUserInfo.getEpspUser().getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)){
            String permissionSql = "select 1 from nst_company_permission x where x.company_id = "+sessionUserInfo.getCompanyUser().getCompanyid()
                    +" and (ncp.permission_keys = '"+sessionUserInfo.getPhone()
                    + "' or ncp.permission_keys like '%," +sessionUserInfo.getPhone()+ ",%' or ncp.permission_keys like '%," +sessionUserInfo.getPhone()
                    + "' or ncp.permission_keys like '" +sessionUserInfo.getPhone()+ ",%' )";
            queryWrapper.and(wrapper->wrapper.exists(permissionSql));
            projectInformationService.page(page, queryWrapper,sessionUserInfo.getCompanyUser().getCompanyid());
        }else{
            if(sessionUserInfo.getEpspUser().getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)) {
                projectInformationService.page(page, queryWrapper, sessionUserInfo.getCompanyUser().getCompanyid());
            }else{
                projectInformationService.page(page, queryWrapper, null);
            }
        }
        return RequestResult.ok(page);
    }

    @GetMapping("queryOne")
    @ApiOperation("详情查询")
    public RequestResult queryOne(@RequestParam("id")String id){
        if(StringUtils.isNotEmpty(id)){
            ProjectInformation projectInformation = projectInformationService.getById(id);
            return RequestResult.ok(projectInformation);
        }else{
            return RequestResult.error("id为空!");
        }
    }


    @DeleteMapping("remove/{id}")
    @ApiOperation("根据ID删除")
    public RequestResult remove(@PathVariable("id") Long id) {
        projectInformationService.removeById(id);
        return RequestResult.ok("删除成功");
    }
}
