package com.example.quartz;

import com.example.quartz.listener.QuartzJobListener;
import com.example.quartz.listener.QuartzTriggerListener;
import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration
public class QuartzService {
    private static Logger logger = LoggerFactory.getLogger(QuartzService.class);
    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            scheduler.clear();
            scheduler.getListenerManager().addJobListener(new QuartzJobListener());
            scheduler.getListenerManager().addTriggerListener(new QuartzTriggerListener());
            addJob(MySampleJob.class, "mySampleJob", "jobId", null, "0/5 * * * * ?");
        } catch (Exception ex) {
            logger.error("QuartzService Error {}", ex);
        }
    }

    public void addJob(Class job, String name, String desc, Map param, String cron) throws SchedulerException {
        JobDataMap jobData = new JobDataMap();
        if (param != null) {
            jobData.putAll(param);
        }
        JobDetail jobDetail = JobBuilder.newJob(job)
                .withIdentity(name)
                .withDescription(desc)
                .usingJobData(jobData)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        if (scheduler.checkExists(jobDetail.getKey())) {
            scheduler.deleteJob(jobDetail.getKey());
        }
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
