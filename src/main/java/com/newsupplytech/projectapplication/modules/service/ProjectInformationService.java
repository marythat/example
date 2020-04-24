package com.newsupplytech.projectapplication.modules.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newsupplytech.projectapplication.comm.core.exception.NstException;
import com.newsupplytech.projectapplication.comm.utils.StringUtils;
import com.newsupplytech.projectapplication.modules.entity.CommAttachment;
import com.newsupplytech.projectapplication.modules.entity.ProjectFlowNode;
import com.newsupplytech.projectapplication.modules.vo.ProjectInfomationVo;
import com.newsupplytech.projectapplication.modules.entity.ProjectInformation;
import com.newsupplytech.projectapplication.modules.mapper.ProjectInformationMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ProjectInformationService extends ServiceImpl<ProjectInformationMapper, ProjectInformation> {
    private ProjectFlowNodeService flowNodeService;
    private CommAttachmentService commAttachmentService;

    @Autowired
    public void setFlowNodeService(ProjectFlowNodeService flowNodeService) {
        this.flowNodeService = flowNodeService;
    }

    @Autowired
    public void setCommAttachmentService(CommAttachmentService commAttachmentService) {
        this.commAttachmentService = commAttachmentService;
    }
    /**
     * 检查是否存在项目编号
     * @param projectCode
     * @return
     */
    public boolean existsProjectCode(String projectCode) {
        QueryWrapper<ProjectInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("is_del","1");
        queryWrapper.eq("project_code",projectCode);
        List<ProjectInformation> resultList = this.list(queryWrapper);
        if(resultList!=null&&resultList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 保存项目信息  级联保存 项目流程节点  项目附件关联信息
     * @param projectInfomationVo
     * @throws Exception
     */
    public void save(ProjectInfomationVo projectInfomationVo) throws Exception {
        ProjectInformation projectInformation = projectInfomationVo.getProjectInformation();
        String applyFormUrl = projectInformation.getApplyFormUrl();
        if(StringUtils.isNotEmpty(projectInformation.getBelongDeptId())){

        }
        //更新项目信息
        if(projectInformation!=null&&projectInformation.getId()!=null){
            this.updateById(projectInformation);
        //新增项目信息
        }else{
            if(this.existsProjectCode(projectInformation.getProjectCode())){
                throw new NstException("项目编号重复");
            }
            this.save(projectInformation);
        }

        //关联项目申报指南信息
        String tableName = ProjectInformation.class.getAnnotation(TableName.class).value();
        if(StringUtils.isNotEmpty(applyFormUrl)){
            String[] applyFormUrls=applyFormUrl.split(",");
            String applyFormUrlExtend="";
            for(String commAttachmentId :applyFormUrls){
                CommAttachment declareCommAttachment = commAttachmentService.getById(commAttachmentId);
                if(declareCommAttachment==null)continue;
                if("1".equals(declareCommAttachment.getIsDel()))continue;
                declareCommAttachment.setRelatedId(projectInformation.getId()+"");
                declareCommAttachment.setRelatedTable(tableName);
                declareCommAttachment.setType("2");
                commAttachmentService.updateById(declareCommAttachment);
                applyFormUrlExtend+=commAttachmentId+",";
            }
            projectInformation.setApplyFormUrl(applyFormUrlExtend);
            this.updateById(projectInformation);
        }

        //关联项目模板信息
        CommAttachment commAttachment = projectInfomationVo.getCommAttachment();
        if(commAttachment!=null&&commAttachment.getId()!=null){
            commAttachment.setRelatedId(projectInformation.getId()+"");
            commAttachment.setRelatedTable(tableName);
            commAttachment.setType("1");
            commAttachmentService.updateById(commAttachment);
        }

    }
    /**
     * 数量
     */
    public int countByPer(Wrapper<ProjectInformation> queryWrapper){
        return this.baseMapper.countByPer(queryWrapper);
    }
    /**
     * 分页查询
     */
    public IPage<ProjectInformation> page(IPage<ProjectInformation> page, Wrapper<ProjectInformation> wrapper, Integer companyId) {
        return this.baseMapper.selectPage(page,wrapper,companyId);
    }
}

