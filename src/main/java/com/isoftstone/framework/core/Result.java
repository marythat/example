package com.isoftstone.framework.core;

/**
 * Created by BHome on 2016/11/25.
 *
 */

public class Result extends OperationPrompt {
    private String content;
    private String msgCode;
    private Object retObj;

    public Result() {
    }

    public Result(boolean success, String msg, String content, String msgCode) {
        this.setSuccess(success);
        this.setMsg(msg);
        this.setContent(content);
        this.setMsgCode(msgCode);
    }

    public static Result success() {
        return new Result(true, "成功");
    }

    public static Result fail() {
        return new Result(false, "失败");
    }

    public Result(boolean success, String msg) {
        this(success, msg, (String)null, (String)null);
    }

    public Result(boolean success, String msg, String content) {
        this(success, msg, content, (String)null);
    }

    public String getMsgCode() {
        return this.msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getRetObj() {
        return this.retObj;
    }

    public void setRetObj(Object retObj) {
        this.retObj = retObj;
    }
}
