package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_dpt")
public class Department {
    @TableField(value = "dptId")
    private Long dptid;

    /**
     * 组织结构名称
     */
    @TableField(value = "dptName")
    private String dptname;

    @TableField(value = "parentDptId")
    private Long parentdptid;
}