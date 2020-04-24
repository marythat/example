package com.newsupplytech.projectapplication.comm.core.interceptor;

import com.isoftstone.epsp.common.util.TokenDecrypt;
import com.isoftstone.epsp.common.util.TokenEncrypt;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.utils.RenderUtils;
import com.newsupplytech.projectapplication.comm.utils.SessionUserInfo;
import com.newsupplytech.projectapplication.comm.utils.SpringContextHolder;
import com.newsupplytech.projectapplication.comm.utils.StringUtils;
import com.newsupplytech.projectapplication.modules.entity.UserPer;
import com.newsupplytech.projectapplication.modules.service.EpspBaseDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * Rest Api接口鉴权
 */
@Component
@DependsOn("springContextHolder")
public class RestApiInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RestApiInterceptor.class);
    private EpspBaseDataService epspBaseDataService = SpringContextHolder.getBean(EpspBaseDataService.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof org.springframework.web.servlet.resource.ResourceHttpRequestHandler) {
            return true;
        }
        if (request.getMethod().equals("OPTIONS")) {
            RenderUtils.renderJson(response, "success");
            return true;
        }
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Headers","*");
        response.setHeader("Access-Control-Expose-Headers","Authorization,x-response-code,content-type,token,iv,phone");
        return check(request, response);
    }
    private boolean check(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String userAccessToken = request.getHeader("token");
        final String phone = request.getHeader("phone");
        if(StringUtils.isBlank(userAccessToken)){
            logger.error("Token参数不存在");
            RenderUtils.renderJson(response, RequestResult.error("用户未登录或登录信息已过期"));
            response.setHeader("x-response-code" , "FAILURE");
            return false;
        }
        Map res = TokenDecrypt.decodeToken(TokenEncrypt.ENCRYPT_KEY, request.getHeader("token"), request.getHeader("iv"));
        if((Boolean) res.get("result")) {
            long userId = Long.parseLong((String) res.get("data"));
            HttpSession session = request.getSession();
            SessionUserInfo sessionUserInfo = (SessionUserInfo) session.getAttribute(SessionUserInfo.SESSION_USER_INFO_KEY);
            //如果session还不存在用户信息,或者用户ID不等于当前Session用户的ID
            if(sessionUserInfo == null || !sessionUserInfo.getEpspUser().getUserid().equals(userId)
                    //企业人员的用户 session 与 header的 phone 值不一样，重新设置
                    || (StringUtils.isNotEmpty(phone) && !phone.equals(sessionUserInfo.getPhone()))
            ){
                sessionUserInfo = epspBaseDataService.sessionUserInfo(userId);
                UserPer userPer = epspBaseDataService.queryUserPer(userId);
                if(StringUtils.isNotEmpty(userPer)&&StringUtils.isNotEmpty(userPer.getPer())){
                    sessionUserInfo.getEpspUser().setPer(userPer.getPer());
                }
                if(sessionUserInfo.getEpspUser().getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)&& StringUtils.isNotEmpty(request.getHeader("phone"))&&!request.getHeader("phone").equals("undefined")){
                    sessionUserInfo.setPhone(request.getHeader("phone"));
                    sessionUserInfo.setCompanyPerson(epspBaseDataService.getCompanyPerson(request.getHeader("phone")));
                    response.setHeader("phone" , request.getHeader("phone"));
                }else{
                    sessionUserInfo.setPhone(null);
                }
                session.setAttribute(SessionUserInfo.SESSION_USER_INFO_KEY,sessionUserInfo);
            }
            Map<String, String> tokenIv = TokenEncrypt.getTokenIv(TokenEncrypt.ENCRYPT_KEY, String.valueOf(userId));
            response.setHeader("token" , tokenIv.get("token"));
            response.setHeader("iv" , tokenIv.get("iv"));
            logger.info(sessionUserInfo.getEpspUser().getUsername()+" 权限验证成功！");
        }else {
            logger.error("用户未登录或登录信息已过期");
            RenderUtils.renderJson(response, RequestResult.error("用户未登录或登录信息已过期"));
            response.setHeader("x-response-code" , "USER_ACCESS_TOKEN_NOTINVAL");
            return false;
        }
        response.setHeader("x-response-code" , "SUCCESS");
        return true;
    }
}
