package com.newsupplytech.projectapplication.modules.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newsupplytech.projectapplication.modules.entity.ApplicationRecord;
import com.newsupplytech.projectapplication.modules.mapper.ApplicationRecordMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApplicationRecordService extends ServiceImpl<ApplicationRecordMapper, ApplicationRecord> {
    public List<Map> applyReportByMonth(String year, Wrapper<ApplicationRecord> queryWrapper) {
        return this.baseMapper.applyReportByMonth(year,queryWrapper);
    }
    public List<Map> applyReportByStreet(String street, Wrapper<ApplicationRecord> queryWrapper) {
        return this.baseMapper.applyReportByStreet(street,queryWrapper);
    }
    public Integer applyCompanyCount(Wrapper<ApplicationRecord> queryWrapper) {
        return this.baseMapper.applyCompanyCount(queryWrapper);
    }
    public List<Map> selectDates(Wrapper<ApplicationRecord> queryWrapper){
        return this.baseMapper.selectDates(queryWrapper);
    }
    public int count(Wrapper<ApplicationRecord> queryWrapper){
        return this.baseMapper.count(queryWrapper);
    }
}




