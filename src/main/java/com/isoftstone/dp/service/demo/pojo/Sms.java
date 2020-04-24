package com.isoftstone.dp.service.demo.pojo;


/**
 * 发送短信 邮件 实体类 Copyright: Copyright 2000 - 2015 Isoftstone Tech. Co. Ltd. All
 */
public class Sms {
    /**
     * VALI_CODE_OPEN 是否打开短息发送
     */
    private String open = "1";


    /**
     * 内容
     */
    private String content;
    /**
     * 手机
     */
    private String phone;

    // 发送短信则需要填充下列属性 读取系统配置项
    /**
     * SP_ID 由平台指定分配 可配置
     */
    private String spId = "086";
    /**
     * PASSWORD 由平台指定分配 可配置
     */
    private String passWord = "086";
    /**
     * PRIORITY 优先级 可配置
     */
    private String priority = "2";
    /**
     * TRANS_TYPE 下行短信只能填写SMS_DOWN_REQUEST 可配置
     */
    private String transType = "SMS_DOWN_REQUEST";
    /**
     * SENDIP 发送地址ip
     */
    private String sendIp = "10.145.197.13";

    /**
     * 企业提交通知
     */
    public static String COMPANY_COMMIT = "【企莞家】温馨提醒:平台已经收到贵企业提交的申请${项目名称}，请耐心等待审核结果，感谢您的提交。";

    /**
     * 退回企业申请
     */
    public static String COMPANY_BACK = "【企莞家】温馨提示:贵企业的申请(${项目名称})已被退回，请尽快登录企莞家平台https://im.dg.gov.cn 修改后重新提交,感谢您的配合。";

    /**
     * 不受理企业申请
     */
    public static String COMPANY_NOT_ACCEPT = "【企莞家】温馨提示:贵企业的申请(${项目名称})已作不受理回复，请登录企莞家平台https://im.dg.gov.cn 查看详细的回复结果,感谢您的配合。";

    /**
     * 企业申请流转完结
     */
    public static String COMPANY_FINISH = "【企莞家】温馨提示:贵企业的申请(${项目名称})审批已通过，请登录企莞家平台https://im.dg.gov.cn 查看审批结果,感谢您的配合。";

    /**
     * 政府端待审批通知
     */
    public static String DPT_WAIT_APPROVAL = "【企莞家】温馨提醒：当前贵单位在申报平台上有(${待处理数量})条申请记录需要审批,请及时登录平台https://im.dg.gov.cn查收处理。";



    /**
     * 无参构造方法
     */
    public Sms() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getSendIp() {
        return sendIp;
    }

    public void setSendIp(String sendIp) {
        this.sendIp = sendIp;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}
