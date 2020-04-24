package com.newsupplytech.projectapplication.modules.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.newsupplytech.projectapplication.modules.entity.AutoSaveProperties;
import com.newsupplytech.projectapplication.modules.mapper.AutoSavePropertiesMapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 申请记录自动保存属性表
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@Service
public class AutoSavePropertiesService extends ServiceImpl<AutoSavePropertiesMapper, AutoSaveProperties>  {
    public int remove(Long applyId) {
        UpdateWrapper<AutoSaveProperties> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("apply_id",applyId);
        updateWrapper.set("is_del",1);
        return this.baseMapper.update(new AutoSaveProperties(),updateWrapper);
    }
}
