package com.newsupplytech.projectapplication.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newsupplytech.projectapplication.comm.core.exception.NstException;
import com.newsupplytech.projectapplication.comm.utils.HttpQueryWrapper;
import com.newsupplytech.projectapplication.comm.utils.StringUtils;
import com.newsupplytech.projectapplication.modules.entity.ProjectFlowNode;
import com.newsupplytech.projectapplication.modules.mapper.ProjectFlowNodeMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectFlowNodeService extends ServiceImpl<ProjectFlowNodeMapper, ProjectFlowNode> {

    @Override
    public boolean saveOrUpdate(ProjectFlowNode entity) {
        //根据项目id查出项目所有的节点
        QueryWrapper<ProjectFlowNode> queryWrapper = new QueryWrapper<ProjectFlowNode>();
        queryWrapper.ne("is_del","1");
        queryWrapper.eq("project_id", entity.getProjectId());
        if(StringUtils.isNotEmpty(entity.getId())){
            queryWrapper.ne("id",entity.getId());
        }
        queryWrapper.orderByAsc("flow_seq");
        List<ProjectFlowNode> projectFlowNodeList = this.list(queryWrapper);
        //判断是否存在相同的数据或者排列序号是否相等
        if (projectFlowNodeList != null && projectFlowNodeList.size() > 0) {
            for (ProjectFlowNode projectFlowNode : projectFlowNodeList) {
                if (entity.getType() != null
                        && !entity.getType().equals(ProjectFlowNode.Type.NORMAL)
                        && projectFlowNode.getType().equals(entity.getType())) {
                    throw new NstException("保证必须有且仅有一个类型为开始和结束的节点。注（序号不可重复）");
                }
                if (projectFlowNode.getFlowSeq().equals(entity.getFlowSeq())) {
                    throw new NstException("序列号["+entity.getFlowSeq()+"]已存在，请重新编辑");
                }
            }
        }
        return super.saveOrUpdate(entity);
    }


    @Deprecated
    public void save(List<ProjectFlowNode> flowNodeList, Long projectInformationId) throws Exception {
        if (flowNodeList == null || flowNodeList.size() <= 0) {
            return;
        }
        ProjectFlowNode projectFlowNode1 = new ProjectFlowNode();
        projectFlowNode1.setProjectId(projectInformationId);
        QueryWrapper<ProjectFlowNode> queryWrapper = HttpQueryWrapper.genQueryWrapper(projectFlowNode1);
        queryWrapper.ne("is_del","1");
        List<ProjectFlowNode> list1 = this.list(queryWrapper);
        List<ProjectFlowNode> list2 = new ArrayList<>();
        for (ProjectFlowNode projectFlowNode : flowNodeList) {
            if (projectFlowNode != null && projectFlowNode.getId() == null) {
                projectFlowNode.setProjectId(projectInformationId);
                this.save(projectFlowNode);
            } else {
                list2.add(projectFlowNode);
            }
        }
        ok:
        for (ProjectFlowNode projectFlowNode : list1) {
            for (ProjectFlowNode projectFlowNode2 : flowNodeList) {
                if (projectFlowNode2.getId() == projectFlowNode.getId()) {
                    this.updateById(projectFlowNode2);
                    continue ok;
                }
            }
            this.removeById(projectFlowNode.getId());
        }
    }

    /**
     * 根据当前节点获取下一节点
     *
     * @param curNode
     * @return
     */
    public ProjectFlowNode getNextNode(ProjectFlowNode curNode) {
        QueryWrapper<ProjectFlowNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("is_del","1");
        queryWrapper.eq("project_id", curNode.getProjectId());
        queryWrapper.orderBy(true, true, "flow_seq");
        List<ProjectFlowNode> nodeList = this.baseMapper.selectList(queryWrapper);
        for (ProjectFlowNode node : nodeList) {
            if (node.getFlowSeq() > curNode.getFlowSeq()) {
                return node;
            }
        }
        return null;
    }
    public ProjectFlowNode getFristNode(String projectId) {
        QueryWrapper<ProjectFlowNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("is_del","1");
        queryWrapper.eq("project_id", projectId);
        queryWrapper.orderBy(true, true, "flow_seq");
        List<ProjectFlowNode> nodeList = this.baseMapper.selectList(queryWrapper);
        if(nodeList.size()>0){
            return nodeList.get(0);
        }
        return null;
    }
}
