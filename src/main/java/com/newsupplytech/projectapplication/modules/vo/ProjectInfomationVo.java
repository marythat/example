package com.newsupplytech.projectapplication.modules.vo;

import com.newsupplytech.projectapplication.modules.entity.CommAttachment;
import com.newsupplytech.projectapplication.modules.entity.ProjectInformation;
import lombok.Data;



@Data
public class ProjectInfomationVo {
    private ProjectInformation projectInformation;
    private CommAttachment commAttachment;
}
