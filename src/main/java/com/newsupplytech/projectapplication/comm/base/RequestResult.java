package com.newsupplytech.projectapplication.comm.base;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(
        value = "接口返回对象",
        description = "接口返回对象"
)
@Data
public class RequestResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("成功标志")
    private boolean success = true;
    @ApiModelProperty("返回处理消息")
    private String message = "操作成功！";
    @ApiModelProperty("返回代码")
    private Integer code = 0;
    @ApiModelProperty("返回数据对象")
    private Object data;
    @ApiModelProperty("时间戳")
    private long timestamp = System.currentTimeMillis();

    public RequestResult error500(String message) {
        this.message = message;
        this.code = 500;
        this.success = false;
        return this;
    }

    public RequestResult success(String message) {
        this.message = message;
        this.code = 200;
        this.success = true;
        return this;
    }

    public static RequestResult ok() {
        RequestResult r = new RequestResult();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("成功");
        return r;
    }

    public static RequestResult ok(String msg) {
        RequestResult r = new RequestResult();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage(msg);
        return r;
    }

    public static RequestResult ok(Object data) {
        RequestResult r = new RequestResult();
        r.setSuccess(true);
        r.setCode(200);
        r.setData(data);
        return r;
    }

    public static RequestResult error(String message) {
        RequestResult r = new RequestResult();
        r.setSuccess(false);
        r.setCode(500);
        r.setMessage(message);
        return r;
    }
}
