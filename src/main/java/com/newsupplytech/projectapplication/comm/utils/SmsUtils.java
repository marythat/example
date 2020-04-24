package com.newsupplytech.projectapplication.comm.utils;

import com.isoftstone.dp.service.demo.internal.SendSms;
import com.isoftstone.dp.service.demo.pojo.Sms;
import com.isoftstone.framework.core.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Bill Lee
 * @date 2020-02-26
 */
@Component
public class SmsUtils {
    private static String useSms;

    public SmsUtils(@Value("${sysconfig.sms-send.open}")String useSms){
        SmsUtils.useSms = useSms;
    }

    /**
     * 企业提交通知
     */
    public static Result sendCommitMsg(String mobile, String projectName){
        return sendSms(mobile,Sms.COMPANY_COMMIT,projectName);
    }


    /**
     * 退回企业申请
     */
    public static Result sendBackMsg(String mobile, String projectName){
        return sendSms(mobile,Sms.COMPANY_BACK,projectName);
    }

    /**
     * 不受理企业申请
     */
    public static Result sendNotAcceptMsg(String mobile, String projectName){
        return sendSms(mobile,Sms.COMPANY_NOT_ACCEPT,projectName);
    }

    /**
     * 企业申请流转完结
     */
    public static Result sendFinishMsg(String mobile, String count){
        return sendSms(mobile,Sms.COMPANY_FINISH,count);
    }

    /**
     * 政府端待审批通知
     */
    public static Result sendDptWaitApproalMsg(String mobile,String count){
        return sendSms(mobile,Sms.DPT_WAIT_APPROVAL,count);
    }

    private static Result sendSms(String mobile, String baseContent, String projectName){
        Sms sms = new Sms();
        sms.setOpen(useSms);
        sms.setPhone(mobile);
        sms.setContent(baseContent.replace("${项目名称}",projectName));
        return SendSms.sendSms(sms);
    }

}
