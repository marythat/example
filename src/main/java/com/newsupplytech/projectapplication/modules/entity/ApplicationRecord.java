package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@ApiModel(value = "ApplicationRecord")
@Data
@TableName(value = "nst_application_record")
public class ApplicationRecord {
    public enum Status{
        DRAFT("草稿"),APPROVAL("审批中"),PASSED("通过"),REJECTED("不通过"),RETURN("退回修改");

        private String label;

        Status(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * 自增主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键ID")
    private Long id;

    /**
     * 项目ID
     */
    @TableField(value = "project_id")
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value = "企业ID")
    private Long companyId;

    /**
     * 企业名称
     */
    @TableField(value = "company_name")
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    /**
     * 镇街id
     */
    @TableField(value = "street_id")
    @ApiModelProperty(value = "镇街id")
    private Integer streetId;

    /**
     * 镇街名称
     */
    @TableField(value = "street")
    @ApiModelProperty(value = "镇街名称")
    private String street;

    /**
     * 表单提交的Json
     */
    @TableField(value = "form_json")
    @ApiModelProperty(value = "表单提交的Json")
    private String formJson;

    /**
     * 申请创建人ID
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value = "申请创建人ID")
    private String createBy;

    /**
     * 创建申请的companyPerson的mobile
     */
    @TableField(value = "create_person")
    @ApiModelProperty(value = "创建申请的companyPerson的mobile")
    private String createPerson;

    /**
     * 申请创建人名称
     */
    @TableField(value = "creator_name")
    @ApiModelProperty(value = "申请创建人名称")
    private String creatorName;

    /**
     * 申请提交时间
     */
    @TableField(value = "commit_date")
    @ApiModelProperty(value = "申请提交时间")
    private Date commitDate;

    /**
     * 当前的节点ID
     */
    @TableField(value = "cur_node")
    @ApiModelProperty(value = "当前的节点ID")
    private String curNode;

    /**
     * 待审核节点ID
     */
    @TableField(value = "next_node_id")
    @ApiModelProperty(value = "待审核节点ID")
    private String nextNodeId;

    /**
     * 待审核节点名称
     */
    @TableField(value = "next_node_name")
    @ApiModelProperty(value = "待审核节点名称")
    private String nextNodeName;

    /**
     * 节点名称
     */
    @TableField(value = "flow_name")
    @ApiModelProperty(value = "节点名称")
    private String flowName;

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
     * 申请状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "申请状态")
    private Status status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 可视用户的ids
     */
    @TableField(value = "permission_keys",updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "可视用户的mobiles")
    private String permissionKeys;

    /**
     * 附件ID字符串
     */
    @TableField(value = "attachment_ids")
    @ApiModelProperty(value = "附件ID字符串")
    private String attachmentIds;

    /**
     * 自动填写属性，多个以英文;;隔开
     */
    @TableField(value = "auto_input_properties")
    @ApiModelProperty(value = "自动填写属性，Json")
    private String autoInputProperties;

    /**
     * 是否删除状态
     */
    @TableField(value = "is_del")
    @ApiModelProperty(value = "是否删除状态")
    private String isDel;//'0默认有效,1删除状态';

    /**
     * 受理编号
     */
    @TableField(value = "accept_code")
    @ApiModelProperty(value = "受理编号")
    private String acceptCode;

    /**
     * 短信通知人员
     */
    @TableField(value = "sms_notification_names")
    @ApiModelProperty(value = "短信通知人员")
    private String smsNotificationNames;


    //ProjectInformation 相关字段begin
    @TableField(exist = false)
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目编码")
    private String projectCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目所属科室名称")
    private String belongDept;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目所属科室id")
    private Integer belongDeptId;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目分类")
    private String projectType;

    @TableField(exist = false)
    @ApiModelProperty(value = "科室电话")
    private String askPhone;

    @TableField(exist = false)
    @ApiModelProperty(value = "必须上传文件")
    private String necessaryUpload;



    //end

    public String getStatusStr(){
        if(status!=null) {
            return status.getLabel();
        }
        return "";
    }

    public void setStatusStr(String statusLabel){
        for (Status s:Status.values()){
            if(s.label.equals(statusLabel)){
                this.status = s;
                return;
            }
        }
    }
}
