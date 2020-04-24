package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_company_person")
public class CompanyPerson {
    @TableField(value = "personId")
    private Integer personid;

    /**
     * 姓名
     */
    @TableField(value = "personName")
    private String personname;

    /**
     * 手机号码
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 职务
     */
    @TableField(value = "duty")
    private String duty;

    /**
     * 高管类型 0：非高管；1：高管
     */
    @TableField(value = "seniorType")
    private Integer seniortype;

    /**
     * 所属单位名称
     */
    @TableField(value = "companyName")
    private String companyname;

    /**
     * 企业id
     */
    @TableField(value = "companyId")
    private Integer companyid;
}