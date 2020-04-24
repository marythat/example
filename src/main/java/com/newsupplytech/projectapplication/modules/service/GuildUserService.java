package com.newsupplytech.projectapplication.modules.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newsupplytech.projectapplication.comm.core.annotion.DataSource;
import com.newsupplytech.projectapplication.comm.core.enums.DataSourceEnum;
import com.newsupplytech.projectapplication.modules.entity.CompanyUser;
import com.newsupplytech.projectapplication.modules.entity.GuildUser;
import com.newsupplytech.projectapplication.modules.mapper.CompanyUserMapper;
import com.newsupplytech.projectapplication.modules.mapper.GuildUserMapper;
import org.springframework.stereotype.Service;


@Service
@DataSource(value= DataSourceEnum.DB2)
public class GuildUserService extends ServiceImpl<GuildUserMapper, GuildUser> {
    public GuildUser getOne(Long userId){
        QueryWrapper queryWrapper = new QueryWrapper<GuildUser>();
        queryWrapper.eq("userId",userId);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
