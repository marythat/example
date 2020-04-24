package com.newsupplytech.projectapplication.modules.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newsupplytech.projectapplication.modules.entity.ApplicationRecord;
import com.newsupplytech.projectapplication.modules.entity.CompanyPermission;
import com.newsupplytech.projectapplication.modules.mapper.ApplicationRecordMapper;
import com.newsupplytech.projectapplication.modules.mapper.CompanyPermissionMapper;
import org.springframework.stereotype.Service;

/**
 * @Description: 企业人员申报授权
 */
@Service
public class CompanyPermissionService extends ServiceImpl<CompanyPermissionMapper, CompanyPermission> {

}
