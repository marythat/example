package com.newsupplytech.projectapplication.comm.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newsupplytech.projectapplication.modules.entity.ApplicationRecord;
import com.newsupplytech.projectapplication.modules.entity.EpspUser;
import com.newsupplytech.projectapplication.modules.entity.ProjectInformation;
import com.newsupplytech.projectapplication.modules.service.ApplicationRecordService;
import com.newsupplytech.projectapplication.modules.service.EpspBaseDataService;
import com.newsupplytech.projectapplication.modules.service.ProjectInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class TaskUtils {
    private static ProjectInformationService projectInformationService;

    private static ApplicationRecordService applicationRecordService;

    private  static EpspBaseDataService epspBaseDataService;
    @Autowired
    public TaskUtils(ProjectInformationService projectInformationService,ApplicationRecordService applicationRecordService,EpspBaseDataService epspBaseDataService){
        this.projectInformationService=projectInformationService;
        this.applicationRecordService=applicationRecordService;
        this.epspBaseDataService=epspBaseDataService;
    }

    //根据项目有效日期更新所有项目的状态
    public static void updateAllProjectInformationStatus(){
        Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        ParsePosition pos=new ParsePosition(0);
        Date currentTime = formatter.parse(dateString,pos);
        QueryWrapper<ProjectInformation> queryWrapper=new QueryWrapper<>();
        queryWrapper.ne("is_del","1");
        queryWrapper.eq("apply_end_time",currentTime);
        List<ProjectInformation> list = projectInformationService.list(queryWrapper);
        list.forEach(projectInformation -> {
            projectInformation.setProjectStatus(ProjectInformation.Status.CLOSED);
            projectInformationService.updateById(projectInformation);
        });
    }
    public static void smsNotificationTasks(){
        HashMap<String,Integer> map=new HashMap<>();
        QueryWrapper<ApplicationRecord> queryWrapper=new QueryWrapper<>();
        queryWrapper.ne("is_del","1").eq("status","APPROVAL");
        List<ApplicationRecord> list = applicationRecordService.list(queryWrapper);
        list.forEach(applicationRecord -> {
            if(StringUtils.isNotEmpty(applicationRecord.getSmsNotificationNames())){
                String[] ids=applicationRecord.getSmsNotificationNames().split(",");
                for(String id :ids){
                    if(map.containsKey(id)){
                        map.put(id,map.get(id)+1);
                    }else{
                        map.put(id,1);
                    }
                }
            }
        });
        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()){
            String key = (String) keys.next();
            EpspUser user = epspBaseDataService.getUser(Long.parseLong(key));
            SmsUtils.sendDptWaitApproalMsg(user.getMobile(),map.get(key)+"");
        }
    }
}
