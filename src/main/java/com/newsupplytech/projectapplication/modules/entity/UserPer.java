package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_user_per")
public class UserPer {
    /**
     * 镇街id
     */
    @TableField(value = "per")
    private String per;

    /**
     * 镇街名称
     */
    @TableField(value = "ACCOUNT_ID")
    private Long accountId;
}
