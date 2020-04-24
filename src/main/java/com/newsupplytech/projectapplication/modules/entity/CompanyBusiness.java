package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_company_business")
public class CompanyBusiness {
    /**
     * 主键，同时也是企业标签ID
     */
    @TableField(value = "businessTypeId")
    private Integer businesstypeid;

    /**
     * 企业标签分类
     */
    @TableField(value = "businessType")
    private String businesstype;
}