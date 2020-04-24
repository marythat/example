package com.isoftstone.framework.core;

/**
 * Created by BHome on 2016/11/25.
 */

public class OperationPrompt {
    private boolean success;
    private String msg;

    public OperationPrompt() {
        this((String)null, true);
    }

    public OperationPrompt(String msg) {
        this((String)null, true);
    }

    public OperationPrompt(boolean success) {
        this((String)null, success);
    }

    public OperationPrompt(String msg, boolean success) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
