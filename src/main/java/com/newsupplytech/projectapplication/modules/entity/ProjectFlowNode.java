package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "ProjectFlowNode")
@Data
@TableName(value = "nst_project_flow_node")
public class ProjectFlowNode {
    public enum ApplyType {
        USER,DEPART
    }

    public enum Type{
        START("开始节点"),NORMAL("中间节点"),END("结尾节点");

        private String label;

        Type(String label) {
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
     * 节点名称
     */
    @TableField(value = "flow_name")
    @ApiModelProperty(value = "节点名称")
    private String flowName;

    /**
     * 序号
     */
    @TableField(value = "flow_seq")
    @ApiModelProperty(value = "序号")
    private Long flowSeq;

    /**
     * 上一节点ID
     */
    @TableField(value = "pre_flow_id")
    @ApiModelProperty(value = "上一节点ID")
    private Long preFlowId;

    /**
     * 节点类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "节点类型")
    private Type type;

    /**
     * 项目ID
     */
    @TableField(value = "project_id")
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 申请类型
     */
    @TableField(value = "apply_type")
    @ApiModelProperty(value = "申请类型")
    private ApplyType applyType;

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

    @TableField(value = "is_checked")
    @ApiModelProperty(value = "是否默认镇街")
    private Integer isChecked;

    /**
     * 是否删除状态
     */
    @TableField(value = "is_del")
    @ApiModelProperty(value = "是否删除状态")
    private String isDel;//'0默认有效,1删除状态';

    /**
     * 短信通知人员
     */
    @TableField(value = "sms_notification_names")
    @ApiModelProperty(value = "短信通知人员")
    private String smsNotificationNames;


    public String getTypeStr(){
        if(type!=null) {
            return type.getLabel();
        }
        return "";
    }

    public void setTypeStr(String typeLabel){
        for (Type s:Type.values()){
            if(s.label.equals(typeLabel)){
                this.type = s;
                return;
            }
        }
    }


}
