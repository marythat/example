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
import com.newsupplytech.projectapplication.modules.entity.*;
import com.newsupplytech.projectapplication.modules.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/sysDistType")
@Api(tags = "字典分类")
public class SysDistTypeController extends BaseController {
    @Autowired
    private SysDistTypeService sysDistTypeService;


    @GetMapping("list")
    @ApiOperation("分页查询")
    public RequestResult list(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest request) {

        QueryWrapper<SysDistType> queryWrapper = HttpQueryWrapper.genQueryWrapper(request.getParameterMap());
        IPage<SysDistType> page = new DefaultPage<>(pageNo, pageSize);
        sysDistTypeService.page(page, queryWrapper);
        return RequestResult.ok(page);
    }

}
