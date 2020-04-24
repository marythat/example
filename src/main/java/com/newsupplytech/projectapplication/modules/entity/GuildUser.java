package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "v_guild_user")
public class GuildUser {
    /**
     * 主键用户id
     */
    @TableField(value = "userId")
    private Integer userid;

    /**
     * 联系人
     */
    @TableField(value = "userName")
    private String username;

    @TableField(value = "orgId")
    private Integer orgid;

    /**
     * 电话
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 协会id
     */
    @TableField(value = "guildId")
    private Integer guildid;

    /**
     * 协会名称
     */
    @TableField(value = "guildName")
    private String guildname;

    /**
     * 组织机构代码
     */
    @TableField(value = "guildCode")
    private String guildcode;
}