package com.newsupplytech.projectapplication.modules.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_dict_type")
public class SysDistType {
    /**
     * 字典表Id
     */
    @TableField(value = "dict_id")
    @ApiModelProperty(value = "字典表Id")
    private String dictId;

    /**
     * 字典名称
     */
    @TableField(value = "dict_name")
    @ApiModelProperty(value = "字典名称")
    private String dictName;

    /**
     * 字典值
     */
    @TableField(value = "dict_val")
    @ApiModelProperty(value = "字典值")
    private String dictVal;

    /**
     * 字典类型
     */
    @TableField(value = "dict_type")
    @ApiModelProperty(value = "字典类型")
    private String dictType;

    /**
     * 状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "状态")
    private String status;


    /**
     * 创建人
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value = "创建人")
    private String createBy;


    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value = "修改人")
    private String updateBy;


    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

}
