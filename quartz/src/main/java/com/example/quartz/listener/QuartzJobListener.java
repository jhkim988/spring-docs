package com.example.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzJobListener implements JobListener {
    private static Logger logger = LoggerFactory.getLogger(QuartzJobListener.class);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.info("Job Listener To Be Executed");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.info("Job Listener Vetoed");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        logger.info("Job Listener was Executed");
    }
}
