package com.newsupplytech.projectapplication.modules.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newsupplytech.projectapplication.modules.entity.ApplicationRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApplicationRecordMapper extends BaseMapper<ApplicationRecord> {
    /**
     * 根据月份分组统计申请
     */
    List<Map> applyReportByMonth(@Param("year")String year, @Param(Constants.WRAPPER)Wrapper<ApplicationRecord> queryWrapper);
    /**
     * 根据镇街分组统计申请
     */
    List<Map> applyReportByStreet(@Param("street")String street, @Param(Constants.WRAPPER)Wrapper<ApplicationRecord> queryWrapper);
    /**
     * 申报企业数量
     */
    Integer applyCompanyCount(@Param(Constants.WRAPPER)Wrapper<ApplicationRecord> queryWrapper);
    /**
     * 申报数量
     */
    int count(@Param(Constants.WRAPPER)Wrapper<ApplicationRecord> queryWrapper);
    /**
     * 申报日期查询
     */
    List<Map> selectDates(@Param(Constants.WRAPPER)Wrapper<ApplicationRecord> queryWrapper);
    /**
     * 详情汇总
     */
    IPage<Map> details(IPage<Map> page,@Param("ew") Wrapper<Map> queryWrapper);

    /**
     * 审批流程详情汇总
     */
    List<Map> detailsOne(IPage<Map> page,@Param("ew") Wrapper<Map> queryWrapper,@Param("id") String id);
}
