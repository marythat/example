package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

import lombok.Data;

@ApiModel(value = "FlowExecuteRecord")
@Data
@TableName(value = "nst_flow_execute_record")
public class FlowExecuteRecord {
    public enum ApprovalResult{
        PASS("受理"),CLOSE("不受理"),BACK("回退"),SUBMIT("提交"),TURN("转办");

        private String label;

        ApprovalResult(String label) {
            this.label = label;
        }


    }

    /**
     * 自增主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键ID")
    private Long id;

    /**
     * 当前的节点ID
     */
    @TableField(value = "cur_node")
    @ApiModelProperty(value = "当前的节点ID")
    private String curNode;

    /**
     * 节点名称
     */
    @TableField(value = "flow_name")
    @ApiModelProperty(value = "节点名称")
    private String flowName;

    /**
     * 项目ID
     */
    @TableField(value = "project_id")
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 申请ID
     */
    @TableField(value = "application_id")
    @ApiModelProperty(value = "申请ID")
    private Long applicationId;

    /**
     * 审批意见
     */
    @TableField(value = "comments")
    @ApiModelProperty(value = "审批意见")
    private String comments;

    /**
     * 批准结果
     */
    @TableField(value = "approval_result")
    @ApiModelProperty(value = "批准结果")
    private ApprovalResult approvalResult;

    /**
     * 表单提交的Json
     */
    @TableField(value = "form_json")
    @ApiModelProperty(value = "表单提交的Json")
    private String formJson;

    /**
     * 执行部门ID
     */
    @TableField(value = "enforce_depart",updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "执行部门ID")
    private String enforceDepart;

    /**
     * 执行人ID(可能有多个)
     */
    @TableField(value = "executor_id",updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "执行人ID(可能有多个)")
    private String executorId;

    /**
     * 创建人ID
     */
    @TableField(value = "create_id")
    @ApiModelProperty(value = "创建人ID")
    private Long createId;

    /**
     * 创建人名称
     */
    @TableField(value = "create_name")
    @ApiModelProperty(value = "创建人名称")
    private String createName;

    /**
     * 创建人电话号码
     */
    @TableField(value = "create_phone")
    @ApiModelProperty(value = "创建人电话号码")
    private String createPhone;

    /**
     * 创建人所属部门
     */
    @TableField(value = "create_Belong_Dept")
    @ApiModelProperty(value = "创建人所属部门")
    private String createBelongDept;

    /**
     * 执行时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "执行时间")
    private Date createTime;


    public String getApprovalResultStr(){
        if(approvalResult!=null) {
            return approvalResult.label;
        }
        return "";
    }

    public void setApprovalResultStr(String statusLabel){
        for (ApprovalResult s:ApprovalResult.values()){
            if(s.label.equals(statusLabel)){
                this.approvalResult = s;
                return;
            }
        }
    }

    @TableField(exist = false)
    public List<CommAttachment> commAttachmentList;
}
