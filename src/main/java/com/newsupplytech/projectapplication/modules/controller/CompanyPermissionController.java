package com.newsupplytech.projectapplication.modules.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.utils.HttpQueryWrapper;
import com.newsupplytech.projectapplication.comm.utils.SessionUserInfo;
import com.newsupplytech.projectapplication.comm.utils.StringUtils;
import com.newsupplytech.projectapplication.modules.entity.CompanyPermission;
import com.newsupplytech.projectapplication.modules.service.CompanyPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;

/**
 * @Description: 企业人员申报授权
 */
@Slf4j
@RestController
@RequestMapping("/companyPermission")
@Api(tags = "企业人员申报授权")
public class CompanyPermissionController extends BaseController {
    @Autowired
    private CompanyPermissionService companyPermissionService;
    @PostMapping("save")
    @ApiOperation("保存(新增/更新)")
    public RequestResult save(@RequestBody CompanyPermission companyPermission) throws Exception {
        SessionUserInfo sessionUserInfo = currentUserInfo();
        companyPermission.setCompanyId(sessionUserInfo.getCompanyUser().getCompanyid());
        if(StringUtils.isEmpty(companyPermission.getId())){
            QueryWrapper queryWrapper = new QueryWrapper<CompanyPermission>();
            if(StringUtils.isNotEmpty(companyPermission.getCompanyId())&&StringUtils.isNotEmpty(companyPermission.getProjectId())) {
                queryWrapper.eq("company_id", companyPermission.getCompanyId());
                queryWrapper.eq("project_id", companyPermission.getProjectId());
                companyPermissionService.remove(queryWrapper);
            }
        }
        companyPermission.setIsDel(0);
        companyPermissionService.saveOrUpdate(companyPermission);
        return RequestResult.ok(companyPermission.getId());
    }
}
