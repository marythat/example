package com.newsupplytech.projectapplication.modules.controller;

import com.isoftstone.epsp.common.util.TokenEncrypt;
import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.utils.SessionUserInfo;
import com.newsupplytech.projectapplication.modules.entity.EpspUser;
import com.newsupplytech.projectapplication.modules.service.EpspBaseDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Api(tags = "获取权限Token")
public class AuthController extends BaseController {
    @Autowired
    private EpspBaseDataService epspBaseDataService;

    @ApiOperation(value = "获取权限Token", notes = "根据用户名获取权限Token信息")
    @GetMapping("tokenPair")
    public RequestResult tokenPair(@ApiParam("用户账号") @RequestParam("userId") Long userId) throws Exception {
        EpspUser user = epspBaseDataService.getUser(userId);
        if(user!=null){
            Map<String, String> tokenIv = TokenEncrypt.getTokenIv(TokenEncrypt.ENCRYPT_KEY, userId.toString());
            return RequestResult.ok(tokenIv);
        }
        return RequestResult.error("没有对应的账号");
    }

    @ApiOperation(value = "获取当前登陆用户信息", notes = "获取当前登陆用户信息")
    @GetMapping("curUser")
    public RequestResult curUser(){
        SessionUserInfo sessionUserInfo = currentUserInfo();
        if(sessionUserInfo!=null) {
            return RequestResult.ok(sessionUserInfo);
        }
        return RequestResult.error("不存在登陆的用户信息");
    }

}
