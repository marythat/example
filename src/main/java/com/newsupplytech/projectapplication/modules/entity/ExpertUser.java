package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_expert_user")
public class ExpertUser {
    /**
     * 主键用户id
     */
    @TableField(value = "userId")
    private Integer userid;

    /**
     * 联系人
     */
    @TableField(value = "userName")
    private String username;

    @TableField(value = "orgId")
    private Integer orgid;

    /**
     * 电话
     */
    @TableField(value = "mobile")
    private String mobile;

    @TableField(value = "expertId")
    private Integer expertid;

    /**
     * 国籍
     */
    @TableField(value = "country")
    private String country;

    /**
     * 证件类型：1身份证、2护照、3军官证、4其他
     */
    @TableField(value = "certifyType")
    private Byte certifytype;

    /**
     * 证件号码
     */
    @TableField(value = "certifyCode")
    private String certifycode;

    /**
     * 诊断类型：1 智能改造  2 工业设计  3 工业互联网  4 技术研发  5管理提升  6 利用资本
     */
    @TableField(value = "diagnoseType")
    private String diagnosetype;
}