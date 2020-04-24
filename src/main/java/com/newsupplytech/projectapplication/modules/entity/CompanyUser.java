package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "v_company_user")
public class CompanyUser {
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

    @TableField(value = "companyId")
    private Integer companyid;

    /**
     * 企业名
     */
    @TableField(value = "companyName")
    private String companyname;

    @TableField(value = "industryType")
    private String industrytype;

    @TableField(value = "industryTypeId")
    private Integer industrytypeid;

    /**
     * 其他类别
     */
    @TableField(value = "otherIndustryType")
    private String otherindustrytype;

    @TableField(value = "registerType")
    private String registertype;

    @TableField(value = "registerTypeId")
    private Integer registertypeid;

    /**
     * 企业注册时间
     */
    @TableField(value = "registerDateTime")
    private Date registerdatetime;

    /**
     * 业务类别
     */
    @TableField(value = "businessTypeId")
    private String businesstypeid;

    /**
     * 街镇名称
     */
    @TableField(value = "street")
    private String street;

    /**
     * 街道地址
     */
    @TableField(value = "streetId")
    private Integer streetid;

    @TableField(value = "firstIndusType")
    private String firstindustype;

    /**
     * 第一行业类别 0：工业 1：服务业
     */
    @TableField(value = "firstIndusTypeId")
    private Integer firstindustypeid;

    /**
     * 统一社会信用代码
     */
    @TableField(value = "companyCode")
    private String companycode;

    /**
     * 法定代表人
     */
    @TableField(value = "deputyPerson")
    private String deputyPerson;

    /**
     * 法定代表人手机
     */
    @TableField(value = "deputyPhone")
    private String deputyPhone;

    @TableField(value = "leaderPerson")
    private String leaderPerson;

    @TableField(value = "leaderPhone")
    private String leaderPhone;

    @TableField(value = "managePerson")
    private String managePerson;

    @TableField(value = "managePhone")
    private String managePhone;

    @TableField(value = "email")
    private String email;
}
