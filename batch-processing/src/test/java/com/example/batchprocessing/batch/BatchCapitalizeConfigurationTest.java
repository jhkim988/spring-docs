package com.example.batchprocessing.batch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SpringBatchTest
public class BatchCapitalizeConfigurationTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testJob(@Autowired
                        @Qualifier("capitalizeJob") Job job) throws Exception {
        jobLauncherTestUtils.setJob(job);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}