package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_dpt_user")
public class DepartmentUser {
    @TableField(value = "userId")
    private Long userid;

    @TableField(value = "userName")
    private String username;

    @TableField(value = "orgId")
    private Long orgid;

    @TableField(value = "mobile")
    private String mobile;

    @TableField(value = "email")
    private String email;

    /**
     * 组织结构名称
     */
    @TableField(value = "dptName")
    private String dptname;

    @TableField(value = "dptId")
    private Integer dptid;
}