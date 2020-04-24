package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_industry")
public class IndustryType {
    @TableField(value = "industryTypeId")
    private Integer industrytypeid;

    @TableField(value = "industryType")
    private String industrytype;

    /**
     * 0代表工业 1 代表服务业
     */
    @TableField(value = "firstIndusType")
    private Integer firstindustype;
}