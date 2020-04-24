package com.newsupplytech.projectapplication.modules.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newsupplytech.projectapplication.comm.core.annotion.DataSource;
import com.newsupplytech.projectapplication.comm.core.enums.DataSourceEnum;
import com.newsupplytech.projectapplication.modules.entity.Department;
import com.newsupplytech.projectapplication.modules.entity.DepartmentUser;
import com.newsupplytech.projectapplication.modules.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;


@Service
@DataSource(value= DataSourceEnum.DB2)
public class DepartmentService extends ServiceImpl<DepartmentMapper, Department> {
    public Department getOne(Integer deptId){
        QueryWrapper queryWrapper = new QueryWrapper<DepartmentUser>();
        queryWrapper.eq("dptId",deptId);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
