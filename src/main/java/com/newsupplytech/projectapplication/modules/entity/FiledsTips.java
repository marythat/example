package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "nst_fileds_tips")
public class FiledsTips {
    /**
     * 自增主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键ID")
    private Integer id;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 申报平台标准化表格需要检查是否为空的字段
     */
    @TableField(value = "filed")
    private String filed;

    /**
     * 弹出的提示
     */
    @TableField(value = "tips")
    private String tips;

    /**
     * 跳转的 url 地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 跳转的 url 地址
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 跳转的 url 地址
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "is_del")
    private Integer isDel;
}
