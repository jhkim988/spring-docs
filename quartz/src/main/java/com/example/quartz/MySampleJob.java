package com.example.quartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class MySampleJob extends QuartzJobBean {
    @Autowired
    private MyService myService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        this.myService.someMethod(context.getFireTime(), "MySampleJob");
    }
}
