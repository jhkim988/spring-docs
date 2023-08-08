package com.example.batchprocessing.batch;

import com.example.batchprocessing.Person;
import com.example.batchprocessing.batch.listener.ItemFailureLoggerListener;
import com.example.batchprocessing.batch.listener.JobCompletionNotificationListener;
import com.example.batchprocessing.batch.processor.PersonItemSmallizeProcessor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
public class BatchSmallizeConfiguration {
    private static final int chunkSize = 10;
    @Autowired
    private ItemReader<Person> reader;

    @Autowired
    private ItemWriter<Person> writer;
    @Autowired
    private DataSource dataSource;

    @Bean
    public PersonItemSmallizeProcessor dbSmallizePerson() {
        return new PersonItemSmallizeProcessor();
    }

    @Bean
    @Qualifier("dbSmallizestep1")
    public Step dbSmallizeStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person>chunk(10, transactionManager)
                .reader(reader)
                .processor(dbSmallizePerson())
                .writer(writer)
                .listener((ItemReadListener) new ItemFailureLoggerListener())
                .listener((ItemProcessListener) new ItemFailureLoggerListener())
                .listener((ItemWriteListener) new ItemFailureLoggerListener())
                .build();
    }

    @Bean
    public Job importSmallizeUserJob(JobRepository jobRepository,
                                     JobCompletionNotificationListener listener, @Qualifier("dbSmallizestep1") Step step1) {
        return new JobBuilder("importSmallizeUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
