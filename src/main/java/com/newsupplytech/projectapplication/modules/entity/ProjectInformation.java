package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "ProjectInformation")
@Data
@TableName(value = "nst_project_information")
public class ProjectInformation {
    public enum Status{
        AVAILABLE("上线"),CLOSED("下线"),ROUGHDRAFT("草稿");
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
     * 项目名称
     */
    @TableField(value = "project_name")
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**
     * 项目编码
     */
    @TableField(value = "project_code")
    @ApiModelProperty(value = "项目编码")
    private String projectCode;

    /**
     * 项目所属科室名称
     */
    @TableField(value = "belong_dept")
    @ApiModelProperty(value = "项目所属科室名称")
    private String belongDept;

    /**
     * 所属科室部门Id
     */
    @TableField(value = "belong_dept_id")
    @ApiModelProperty(value = "所属科室部门Id")
    private Integer belongDeptId;

    /**
     * 科室咨询电话
     */
    @TableField(value = "ask_phone")
    @ApiModelProperty(value = "科室咨询电话")
    private String askPhone;

    /**
     * 申报开始日期
     */
    @TableField(value = "apply_start_time")
    @ApiModelProperty(value = "申报开始日期")
    private Date applyStartTime;

    /**
     * 申报结束日期
     */
    @TableField(value = "apply_end_time")
    @ApiModelProperty(value = "申报结束日期")
    private Date applyEndTime;

    /**
     * 申报条件
     */
    @TableField(value = "apply_conditions")
    @ApiModelProperty(value = "申报条件")
    private String applyConditions;

    /**
     * 项目申报表单的数据项名称json字符串
     */
    @TableField(value = "project_data_item")
    @ApiModelProperty(value = "项目申报表单的数据项名称json字符串")
    private String projectDataItem;

    /**
     * 项目申报的填报表单URL地址
     */
    @TableField(value = "apply_form_url")
    @ApiModelProperty(value = "项目申报的填报表单URL地址")
    private String applyFormUrl;

    /**
     * 项目状态
     */
    @TableField(value = "project_status")
    @ApiModelProperty(value = "项目状态")
    private Status projectStatus;

    /**
     * 项目适合的企业类型标签
     */
    @TableField(value = "company_tag")
    @ApiModelProperty(value = "项目适合的企业类型标签")
    private String companyTag;

    /**
     * 当前项目审批流转的模板编码
     */
    @TableField(value = "audit_template")
    @ApiModelProperty(value = "当前项目审批流转的模板编码")
    private String auditTemplate;

    /**
     * 项目发布时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "项目发布时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否删除状态
     */
    @TableField(value = "is_del")
    @ApiModelProperty(value = "是否删除状态")
    private String isDel;//'0默认有效,1删除状态';
    /**
     * 必须上传文件
     */
    @TableField(value = "necessary_upload")
    @ApiModelProperty(value = "必须上传文件")
    private String necessaryUpload;

    /**
     * 业务名称
     */
    @TableField(value = "business_name")
    @ApiModelProperty(value = "业务名称")
    private String businessName;

    /**
     * 项目分类
     */
    @TableField(value = "project_type")
    @ApiModelProperty(value = "项目分类")
    private String  projectType;

    @TableField(value = "declare_count",updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "企业可申报数")
    private Integer  declareCount;

    /**
     * 自动保存的属性
     */
    @TableField(value = "auto_save_properties",updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "自动保存的属性")
    private String autoSaveProperties;

    @TableField(exist = false)
    @ApiModelProperty(value = "授权对象")
    private String permissionKeys;

    @TableField(exist = false)
    @ApiModelProperty(value = "授权对象id")
    private Long ncpId;

    public String getProjectStatusStr(){
        if(projectStatus!=null) {
            return projectStatus.getLabel();
        }
        return "";
    }

    public void setProjectStatusStr(String statusLabel){


        if(Status.AVAILABLE.label.equals(statusLabel)){
            this.projectStatus = Status.AVAILABLE;
            return;
        }else if(Status.ROUGHDRAFT.label.equals(statusLabel)){
            this.projectStatus = Status.ROUGHDRAFT;
            return;
        }
        this.projectStatus = Status.CLOSED;
    }

    public Status getProjectStatus(){
        return projectStatus;
    }

}
