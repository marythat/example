package com.newsupplytech.projectapplication.modules.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newsupplytech.projectapplication.comm.utils.*;
import com.newsupplytech.projectapplication.modules.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FlowExecuteService {
    @Autowired
    private FlowExecuteRecordService flowExecuteRecordService;

    @Autowired
    private ApplicationRecordService applicationRecordService;

    @Autowired
    private ProjectFlowNodeService projectFlowNodeService;


    @Autowired
    private CommAttachmentService commAttachmentService;

    @Autowired
    private EpspBaseDataService epspBaseDataService;

    @Autowired
    private AutoSavePropertiesService autoSavePropertiesService;

    /**
     * 开始流程
     *
     * @param sessionUserInfo
     * @param applicationRecord
     * @return
     */
    @Transactional
    public ApplicationRecord startFlow(SessionUserInfo sessionUserInfo, ApplicationRecord applicationRecord,Street street,ProjectInformation projectInformation) {
        CompanyUser companyUser = sessionUserInfo.getCompanyUser();
        EpspUser epspUser = sessionUserInfo.getEpspUser();
        CompanyPerson companyPerson = sessionUserInfo.getCompanyPerson();
        QueryWrapper<ProjectFlowNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("is_del","1");
        queryWrapper.eq("project_id", applicationRecord.getProjectId());
        queryWrapper.eq("type", ProjectFlowNode.Type.START);
        ProjectFlowNode firstNode = projectFlowNodeService.getOne(queryWrapper);
        ProjectFlowNode secondNode = projectFlowNodeService.getNextNode(firstNode);
        applicationRecord.setCurNode(firstNode.getId() + "");
        applicationRecord.setFlowName(firstNode.getFlowName());
        if(!secondNode.getIsChecked().equals(1)){
            street = null;
        }
        //设置当前环节审批人
        if(StringUtils.isNotEmpty(secondNode)&&!ApplicationRecord.Status.DRAFT.equals(applicationRecord.getStatus())) {
            if (secondNode.getApplyType().equals(ProjectFlowNode.ApplyType.USER)) {
                applicationRecord.setExecutorId(secondNode.getExecutorId());
                applicationRecord.setEnforceDepart(null);
            } else {
                applicationRecord.setEnforceDepart(StringUtils.isNotEmpty(street)&&StringUtils.isNotEmpty(street.getStreetDptId())?street.getStreetDptId().toString():secondNode.getEnforceDepart());
                applicationRecord.setExecutorId(null);
            }
        }
        applicationRecord.setCreateBy(companyUser.getUserid() + "");
        applicationRecord.setCreatePerson(StringUtils.isNotEmpty(companyPerson)?companyPerson.getMobile():companyUser.getMobile());
        applicationRecord.setCreatorName(StringUtils.isNotEmpty(companyPerson)?companyPerson.getPersonname():companyUser.getUsername());
        applicationRecord.setCompanyId(companyUser.getCompanyid() + 0L);
        applicationRecord.setCompanyName(companyUser.getCompanyname());
        applicationRecord.setStreetId(companyUser.getStreetid());
        applicationRecord.setCommitDate(new Date());
        if(!ApplicationRecord.Status.DRAFT.equals(applicationRecord.getStatus())) {
            //1.先删除子表数据
            if (StringUtils.isNotEmpty(applicationRecord.getId())) {
                QueryWrapper<AutoSaveProperties> wrapper = new QueryWrapper();
                wrapper.eq("apply_id", applicationRecord.getId());
                autoSavePropertiesService.remove(wrapper);
            }
            applicationRecordService.saveOrUpdate(applicationRecord);
            applicationRecord.setAcceptCode(DateUtil.toStr("MMdd",new Date()) +
                    String.format("%04d",applicationRecord.getId()) + RandomUtil.randomString(RandomUtil.BASE_CHAR,3));
            //2.子表数据重新插入
            if (StringUtils.isNotEmpty(projectInformation.getAutoSaveProperties())) {
                List<String> list = new ArrayList(Arrays.asList(projectInformation.getAutoSaveProperties().split(",")));
                List<JSONObject> jsonObjects = (List<JSONObject>) JSONObject.parse(applicationRecord.getFormJson());
                JSONObject jsonObject = jsonObjects.get(0);
                for (String s : list) {
                    AutoSaveProperties autoSaveProperties = new AutoSaveProperties();
                    //外键设置
                    autoSaveProperties.setUserId(Long.parseLong(applicationRecord.getCreateBy()));
                    autoSaveProperties.setProjectId(projectInformation.getId());
                    autoSaveProperties.setApplyId(applicationRecord.getId());
                    autoSaveProperties.setAutoKey(s);
                    autoSaveProperties.setAutoValue(jsonObject.getString(s));
                    autoSaveProperties.setCreateTime(new Date());
                    autoSaveProperties.setIsDel(0);
                    autoSavePropertiesService.save(autoSaveProperties);
                }
            }
        }

        //添加提交流程信息
        if (ApplicationRecord.Status.APPROVAL.equals(applicationRecord.getStatus())) {
            applicationRecord.setNextNodeId(secondNode.getId()+"");
            applicationRecord.setNextNodeName(secondNode.getFlowName());
            FlowExecuteRecord flowExecuteRecord = new FlowExecuteRecord();
            flowExecuteRecord.setCreateId(companyUser.getUserid() + 0L);
            flowExecuteRecord.setCreateName(StringUtils.isNotEmpty(companyPerson)?companyPerson.getPersonname():companyUser.getUsername());
            flowExecuteRecord.setApplicationId(applicationRecord.getId());
            flowExecuteRecord.setCurNode(applicationRecord.getCurNode());
            if (secondNode.getApplyType().equals(ProjectFlowNode.ApplyType.USER)) {
                flowExecuteRecord.setExecutorId(secondNode.getExecutorId());
                flowExecuteRecord.setEnforceDepart(null);
            } else {
                flowExecuteRecord.setEnforceDepart(StringUtils.isNotEmpty(street)&&StringUtils.isNotEmpty(street.getStreetDptId())?street.getStreetDptId().toString():secondNode.getEnforceDepart());
                flowExecuteRecord.setExecutorId(null);
            }
            flowExecuteRecord.setFlowName(applicationRecord.getFlowName());
            flowExecuteRecord.setProjectId(applicationRecord.getProjectId());
            flowExecuteRecord.setFormJson(applicationRecord.getFormJson());
            flowExecuteRecord.setComments("企业提交");
            flowExecuteRecord.setCreateBelongDept(companyUser.getCompanyname());
            flowExecuteRecord.setApprovalResult(FlowExecuteRecord.ApprovalResult.SUBMIT);
            flowExecuteRecord.setCreatePhone(StringUtils.isNotEmpty(companyPerson)?companyPerson.getMobile():epspUser.getMobile());
            flowExecuteRecordService.save(flowExecuteRecord);

            //更新节点附件关联
            String attachmentIds = applicationRecord.getAttachmentIds();
            if (StringUtils.isNotBlank(attachmentIds)) {
                for (String idStr : attachmentIds.split(",")) {
                    CommAttachment commAttachment = commAttachmentService.getById(idStr);
                    commAttachment.setRelatedTable(FlowExecuteRecord.class.getAnnotation(TableName.class).value());
                    commAttachment.setRelatedId(flowExecuteRecord.getId()+"");
                    commAttachmentService.updateById(commAttachment);
                }
            }
        }

        if(StringUtils.isNotEmpty(secondNode.getSmsNotificationNames())){
            applicationRecord.setSmsNotificationNames(secondNode.getSmsNotificationNames().split("-")[1]);
        }else{
            applicationRecord.setSmsNotificationNames("");
        }
        // 3. 发送短信
        SmsUtils.sendCommitMsg(StringUtils.isNotEmpty(companyPerson)?companyPerson.getMobile():epspUser.getMobile(),applicationRecord.getProjectName());
        return applicationRecord;
    }


    /**
     * 执行审批
     *
     * @param flowExecuteRecord
     * @param multipleSelectionIds
     * @return
     */
    public ApplicationRecord commit(ApplicationRecord applicationRecord, FlowExecuteRecord flowExecuteRecord, String multipleSelectionIds,Street street,String companyUserPhone) {
        List<CommAttachment> commAttachmentList = flowExecuteRecord.getCommAttachmentList();
        // 1. 保存审核记录
        ProjectFlowNode projectFlowNode = null;
        if (StringUtils.isNotEmpty(multipleSelectionIds)) {
            projectFlowNode=projectFlowNodeService.getById(applicationRecord.getCurNode());
        }else{
            projectFlowNode=projectFlowNodeService.getById(flowExecuteRecord.getCurNode());
        }
        ProjectFlowNode secondNode=null;
        ProjectFlowNode fristNode = projectFlowNodeService.getFristNode(applicationRecord.getProjectId() + "");
        if(!ProjectFlowNode.Type.END.equals(projectFlowNode.getType())) {
            secondNode = projectFlowNodeService.getNextNode(projectFlowNode);
            if (!secondNode.getIsChecked().equals(1)) {
                street = null;
            }
            if (StringUtils.isNotEmpty(multipleSelectionIds)) {
                flowExecuteRecord.setExecutorId(multipleSelectionIds);
                flowExecuteRecord.setEnforceDepart(null);
            } else {
                if (secondNode.getApplyType().equals(ProjectFlowNode.ApplyType.USER)) {
                    flowExecuteRecord.setExecutorId(secondNode.getExecutorId());
                    flowExecuteRecord.setEnforceDepart(null);
                } else {
                    flowExecuteRecord.setEnforceDepart(StringUtils.isNotEmpty(street) && StringUtils.isNotEmpty(street.getStreetDptId()) ? street.getStreetDptId().toString() : secondNode.getEnforceDepart());
                    flowExecuteRecord.setExecutorId(null);
                }
            }
        }
        // 2. 修改项目状态
        applicationRecord.setFlowName(flowExecuteRecord.getFlowName());
        applicationRecord.setCurNode(flowExecuteRecord.getCurNode() + "");

        if (StringUtils.isNotEmpty(multipleSelectionIds)) {
            flowExecuteRecord.setCurNode(secondNode.getId()+"");
            flowExecuteRecord.setFlowName(secondNode.getFlowName());
        }
        flowExecuteRecordService.save(flowExecuteRecord);

        switch (flowExecuteRecord.getApprovalResult()) {
            case PASS:
            case TURN:
                if (ProjectFlowNode.Type.END.equals(projectFlowNode.getType())) {
                    applicationRecord.setStatus(ApplicationRecord.Status.PASSED);
                    applicationRecord.setNextNodeName("");
                    applicationRecord.setNextNodeId("");
                } else {
//                    ProjectFlowNode nextNode = projectFlowNodeService.getNextNode(projectFlowNode);
                    applicationRecord.setStatus(ApplicationRecord.Status.APPROVAL);
                    applicationRecord.setNextNodeName(secondNode.getFlowName());
                    applicationRecord.setNextNodeId(secondNode.getId()+"");
                    //设置当前环节审批人
                    if (StringUtils.isNotEmpty(flowExecuteRecord.getExecutorId())) {
                        applicationRecord.setExecutorId(flowExecuteRecord.getExecutorId());
                        applicationRecord.setEnforceDepart(null);
                    } else {
                        applicationRecord.setEnforceDepart(StringUtils.isNotEmpty(street) && StringUtils.isNotEmpty(street.getStreetDptId()) ? street.getStreetDptId().toString() : flowExecuteRecord.getEnforceDepart());
                        applicationRecord.setExecutorId(null);
                    }
/*                    if (projectFlowNode != null) {
                    applicationRecord.setEnforceDepart(nextNode.getEnforceDepart());
                    if (StringUtils.isNotEmpty(multipleSelectionIds)) {
                        applicationRecord.setExecutorId(multipleSelectionIds);
                    } else {
                        applicationRecord.setExecutorId(nextNode.getExecutorId());
                    }
                } else {
                    throw new NstException("不能找到流程的下一节点");
                }*/
                }
                break;
            case BACK:
                applicationRecord.setStatus(ApplicationRecord.Status.RETURN);
                applicationRecord.setNextNodeName(fristNode.getFlowName());
                applicationRecord.setNextNodeId(fristNode.getId()+"");
                break;
            case CLOSE:
                applicationRecord.setStatus(ApplicationRecord.Status.REJECTED);
                break;
            default:
        }
        if (StringUtils.isNotEmpty(multipleSelectionIds)) {
            applicationRecord.setSmsNotificationNames("");
        }else{
            if(secondNode!=null&&StringUtils.isNotEmpty(secondNode.getSmsNotificationNames())){
                applicationRecord.setSmsNotificationNames(secondNode.getSmsNotificationNames().split("-")[1]);
            }else{
                applicationRecord.setSmsNotificationNames("");
            }
        }
        applicationRecordService.updateById(applicationRecord);

        //更新节点附件关联
        if (commAttachmentList!=null) {
            for (CommAttachment commAttachment : commAttachmentList) {
                commAttachment.setRelatedTable(FlowExecuteRecord.class.getAnnotation(TableName.class).value());
                commAttachment.setRelatedId(flowExecuteRecord.getId()+"");
                commAttachmentService.updateById(commAttachment);
            }
        }

        // 3. 发送短信
        switch (applicationRecord.getStatus()) {
            case PASSED:
                SmsUtils.sendFinishMsg(companyUserPhone,applicationRecord.getProjectName());
                break;
            case RETURN:
                SmsUtils.sendBackMsg(companyUserPhone,applicationRecord.getProjectName());
                break;
            case REJECTED:
                SmsUtils.sendNotAcceptMsg(companyUserPhone,applicationRecord.getProjectName());
                break;
            default:
        }
        return applicationRecord;
    }
}
