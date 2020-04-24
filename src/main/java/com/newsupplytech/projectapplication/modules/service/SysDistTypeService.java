package com.newsupplytech.projectapplication.modules.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newsupplytech.projectapplication.comm.core.annotion.DataSource;
import com.newsupplytech.projectapplication.comm.core.enums.DataSourceEnum;
import com.newsupplytech.projectapplication.modules.entity.Department;
import com.newsupplytech.projectapplication.modules.entity.DepartmentUser;
import com.newsupplytech.projectapplication.modules.entity.SysDistType;
import com.newsupplytech.projectapplication.modules.mapper.DepartmentMapper;
import com.newsupplytech.projectapplication.modules.mapper.SysDistTypeMapper;
import org.springframework.stereotype.Service;


@Service
public class SysDistTypeService extends ServiceImpl<SysDistTypeMapper, SysDistType> {

}
