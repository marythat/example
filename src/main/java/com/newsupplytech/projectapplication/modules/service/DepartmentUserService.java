package com.newsupplytech.projectapplication.modules.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newsupplytech.projectapplication.comm.core.annotion.DataSource;
import com.newsupplytech.projectapplication.comm.core.enums.DataSourceEnum;
import com.newsupplytech.projectapplication.modules.entity.DepartmentUser;
import com.newsupplytech.projectapplication.modules.mapper.DepartmentUserMapper;
import org.springframework.stereotype.Service;


@Service
@DataSource(value= DataSourceEnum.DB2)
public class DepartmentUserService extends ServiceImpl<DepartmentUserMapper, DepartmentUser> {
    public DepartmentUser getOne(Long userId){
        QueryWrapper queryWrapper = new QueryWrapper<DepartmentUser>();
        queryWrapper.eq("userId",userId);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
