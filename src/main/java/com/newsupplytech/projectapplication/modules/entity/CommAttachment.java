package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "CommAttachment")
@Data
@TableName(value = "nst_comm_attachment")
public class CommAttachment {
    /**
     * 自增主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键ID")
    private Long id;

    /**
     * 关联的表名称
     */
    @TableField(value = "related_table")
    @ApiModelProperty(value = "关联的表名称")
    private String relatedTable;

    /**
     * 关联表的ID
     */
    @TableField(value = "related_id")
    @ApiModelProperty(value = "关联表的ID")
    private String relatedId;

    /**
     * 文件名称
     */
    @TableField(value = "file_name")
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 文件路径
     */
    @TableField(value = "file_path")
    @ApiModelProperty(value = "文件路径")
    private String filePath;
    /**
     * 文件类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "文件类型")
    private String type;//"1"是模板文件,"2"申报指南文件

    /**
     * 是否删除状态
     */
    @TableField(value = "is_del")
    @ApiModelProperty(value = "是否删除状态")
    private String isDel;//'0默认有效,1删除状态';

    /**
     * 附件额外属性
     */
    @TableField(value = "ext_property")
    @ApiModelProperty(value = "附件额外属性")
    private String extProperty;

}