package com.isoftstone.dp.service.demo.internal;

import com.isoftstone.dp.service.demo.pojo.Sms;
import com.isoftstone.framework.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class SendSms {
    /**
     * 发送短信
     *
     * @param sms 发送对象
     * @return
     */
    public static Result sendSms(Sms sms) {
        Assert.notNull(sms.getContent());
        Assert.notNull(sms.getPhone());
        if (!"0".equals(sms.getOpen())) {
            String errorMsg = "短信发送没开启";
            log.warn(errorMsg);
            return new Result(false, "提示", errorMsg);
        }

        Result r = null;
        //外网
        String requestUrl = "http://" + sms.getSendIp() + "/mspf/servlet/SpInterface";
        StringBuffer requestData = new StringBuffer();
        requestData.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        requestData.append("<REQUEST><TRANS_TYPE>" + sms.getTransType() + "</TRANS_TYPE>");
        requestData.append("<SP_ID>" + sms.getSpId() + "</SP_ID>");
        requestData.append("<PASSWORD>" + sms.getPassWord() + "</PASSWORD>");
        requestData.append("<SEQ_NUM>" + sms.getSpId() + (int) (Math.random() * 10000 + 10000) + "</SEQ_NUM><MOBILE>" + sms.getPhone() + "</MOBILE>");

        requestData.append("<CONTENT><![CDATA[" + sms.getContent() + "]]></CONTENT>");
        requestData.append("<DATETIME>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "</DATETIME>");
        requestData.append("<PRIORITY>" + sms.getPriority() + "</PRIORITY><EXTEND_CODE>" + (int) (Math.random() * 100000 + 100000) + "</EXTEND_CODE></REQUEST>");

        String respData = "";
        HttpURLConnection httprequestUrlconnection = null;
        InputStream is = null;
        BufferedReader reader = null;
        try {
            boolean sendOk = false;
            int resendCount = 0;
            int result = 0;
            URL url = null;
            while (!sendOk && resendCount < 3) {
                url = new URL(requestUrl);
                httprequestUrlconnection = (HttpURLConnection) url.openConnection();
                httprequestUrlconnection.setDoOutput(true);
                httprequestUrlconnection.setRequestMethod("POST");
                httprequestUrlconnection.getOutputStream().write(new String(requestData).getBytes("utf-8"));
                httprequestUrlconnection.getOutputStream().flush();
                httprequestUrlconnection.getOutputStream().close();
                result = httprequestUrlconnection.getResponseCode();
                if (result == 200) {
                    StringBuffer respStr = new StringBuffer("");
                    is = httprequestUrlconnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is, "gbk"));
                    String currentLine;
                    while ((currentLine = reader.readLine()) != null) {
                        if (currentLine.length() > 0) {
                            respStr.append(currentLine.trim());
                        }
                    }
                    // 处理响应结果
                    sendOk = true;
                    respData = respStr.toString();
                } else {
                    log.error("远程服务器连接失败,错误代码: " + result);
                    resendCount++;
                }
            }
            if (sendOk) {
                r = new Result(true, "提示", "发送短信成功");
            } else {
                r = new Result(false, "提示", "发送短信失败");
            }

        } catch (Exception e) {
            /*e.printStackTrace();*/
            log.error("向远程服务器提交请求数据失败: " + e.getMessage());
            r = new Result(false, "错误", "发送短信异常");
        } finally {
            if (httprequestUrlconnection != null) {
                httprequestUrlconnection.disconnect();
            }
            //关闭io流
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭reader
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.debug("短信发送结果：" + respData);

        return r;
    }
}
