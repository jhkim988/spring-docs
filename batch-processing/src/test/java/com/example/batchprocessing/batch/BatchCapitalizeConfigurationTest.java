package com.example.batchprocessing.batch;

import com.example.batchprocessing.TestDataSourceConfiguration;
import com.example.batchprocessing.batch.listener.ItemFailureLoggerListener;
import com.example.batchprocessing.batch.listener.JobCompletionNotificationListener;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

@SpringBatchTest
@SpringJUnitConfig({
        BatchCapitalizeConfiguration.class,
        Config.class,
        TestDataSourceConfiguration.class,
        JobCompletionNotificationListener.class,
        ItemFailureLoggerListener.class,
})
@EnableBatchProcessing
public class BatchCapitalizeConfigurationTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private DataSource dataSource;

    @Test
    public void testJob(@Autowired @Qualifier("capitalizeJob") Job job) throws Exception {
        this.jobLauncherTestUtils.setJob(job);

//        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
//        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}