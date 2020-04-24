package com.newsupplytech.projectapplication.comm.utils;

import com.newsupplytech.projectapplication.modules.entity.*;
import lombok.Data;

import java.util.Map;

@Data
public class SessionUserInfo {
    public static final String SESSION_USER_INFO_KEY = "USER_INFORMATION";

    //机构类型对应的orgId值
    public static final Long ORG_ID_DEPART = 12L;
    public static final Long ORG_ID_COMPANY = 1000L;
    public static final Long ORG_ID_GUILD = 1500L;
    public static final Long ORG_ID_EXPERT = 2000L;

    private EpspUser epspUser;

    //部门用户信息
    private DepartmentUser departmentUser;
    private Department department;

    //企业用户信息
    private CompanyUser companyUser;
    private CompanyPerson companyPerson;
    //企业用户的phone
    private String phone;
//    private List<CompanyPerson> companyPersonList;

    //专家用户
    private ExpertUser expertUser;

    //机构用户
    private GuildUser guildUser;
}
