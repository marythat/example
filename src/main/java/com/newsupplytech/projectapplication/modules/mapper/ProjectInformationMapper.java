package com.newsupplytech.projectapplication.modules.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newsupplytech.projectapplication.modules.entity.ApplicationRecord;
import com.newsupplytech.projectapplication.modules.entity.ProjectInformation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectInformationMapper extends BaseMapper<ProjectInformation> {
//    public List<ProjectInformation> queryList(Page<ProjectInformation> page);
//    public int queryListCount(Page<ProjectInformation> page);
    /**
     * 数量
     */
    int countByPer(@Param(Constants.WRAPPER)Wrapper<ProjectInformation> queryWrapper);
    IPage<ProjectInformation> selectPage(IPage<ProjectInformation> page, @Param(Constants.WRAPPER) Wrapper<ProjectInformation> wrapper,
                                        @Param("companyId") Integer companyId);
}
