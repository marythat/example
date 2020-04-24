package com.newsupplytech.projectapplication.config;

import com.newsupplytech.projectapplication.comm.utils.TaskUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;



@Configuration
@EnableScheduling
public class SaticScheduleTaskConfig {
    @Scheduled(cron = "${timingTask.time}")
//    @Scheduled(fixedRate=5000)//每5秒执行，测试使用
    private void configureTasks() {
        TaskUtils.updateAllProjectInformationStatus();
    }

    @Scheduled(cron = "${timingTask.smsNotificationTime}")
    private void smsNotificationTasks() {
        TaskUtils.smsNotificationTasks();
    }
}
