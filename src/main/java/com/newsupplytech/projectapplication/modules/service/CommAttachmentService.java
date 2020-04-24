package com.newsupplytech.projectapplication.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newsupplytech.projectapplication.modules.entity.ProjectFlowNode;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newsupplytech.projectapplication.modules.entity.CommAttachment;
import com.newsupplytech.projectapplication.modules.mapper.CommAttachmentMapper;

@Service
public class CommAttachmentService extends ServiceImpl<CommAttachmentMapper, CommAttachment> {

    public List<CommAttachment>  selectRelatedId(String relatedTable,Long related_id){
        QueryWrapper<CommAttachment> queryWrapper =new QueryWrapper<CommAttachment>();
        queryWrapper.ne("is_del","1");
        queryWrapper.eq("related_table",relatedTable);
        queryWrapper.eq("related_id",related_id);
        List<CommAttachment> list = this.list(queryWrapper);
        return list;
    };


}

