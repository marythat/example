package com.newsupplytech.projectapplication.modules.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 企业人员申报授权
 */
@ApiModel(value = "CompanyPermission")
@Data
@TableName(value = "nst_company_permission")
public class CompanyPermission {

	/**
	 * 自增主键ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "自增主键ID")
	private Long id;
	/**
	 * 企业id
	 */
	@TableField(value = "company_id")
	private Integer companyId;
	/**
	 * 项目ID
	 */
	@TableField(value = "project_id")
	@ApiModelProperty(value = "项目ID")
	private long projectId;
	/**
	 * 授权对象
	 */
	@TableField(value = "permission_keys",updateStrategy = FieldStrategy.IGNORED)
	@ApiModelProperty(value = "授权对象")
	private String permissionKeys;
	/**
	 * 是否删除状态
	 */
	@TableField(value = "is_del")
	@ApiModelProperty(value = "是否删除状态")
	private Integer isDel;
}
