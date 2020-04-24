package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_street")
public class Street {
    /**
     * 镇街id
     */
    @TableField(value = "id")
    private Integer userid;

    /**
     * 镇街名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 镇街id
     */
    @TableField(value = "streetDptId")
    private Long streetDptId;
}
