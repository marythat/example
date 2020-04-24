package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_user")
public class EpspUser {
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

    @TableField(value = "per")
    private String per;
}
