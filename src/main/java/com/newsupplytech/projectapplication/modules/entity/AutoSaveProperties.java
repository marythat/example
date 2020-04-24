package com.newsupplytech.projectapplication.modules.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 申请记录自动保存属性表
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@Data
@ApiModel(value = "AutoSaveProperties")
@TableName("nst_auto_save_properties")
public class AutoSaveProperties implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "自增主键ID")
	private Long id;
	/**申请人id*/
	@TableField(value = "user_id")
	@ApiModelProperty(value = "项目ID")
	private Long userId;
	/**项目id*/
	@TableField(value = "project_id")
	@ApiModelProperty(value = "项目ID")
	private Long projectId;
	/**申请记录id*/
	@TableField(value = "apply_id")
	@ApiModelProperty(value = "项目ID")
	private Long applyId;
	/**键*/
	@TableField(value = "auto_key")
	@ApiModelProperty(value = "项目ID")
	private String autoKey;
	/**值*/
	@TableField(value = "auto_value")
	@ApiModelProperty(value = "项目ID")
	private String autoValue;
	/**创建时间*/
	@TableField(value = "create_time")
	@ApiModelProperty(value = "项目ID")
	private Date createTime;
	/**是否删除（0否1是）*/
	@TableField(value = "is_del")
	@ApiModelProperty(value = "是否删除（0否1是）")
	private Integer isDel;
}
