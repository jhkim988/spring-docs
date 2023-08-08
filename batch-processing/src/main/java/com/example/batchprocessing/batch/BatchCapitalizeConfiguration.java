package com.example.batchprocessing.batch;

import com.example.batchprocessing.Person;
import com.example.batchprocessing.batch.listener.ItemFailureLoggerListener;
import com.example.batchprocessing.batch.listener.JobCompletionNotificationListener;
import com.example.batchprocessing.batch.processor.PersonItemCapitalizeProcessor;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchCapitalizeConfiguration {
    @Autowired
    private ItemReader<Person> reader;
    @Autowired
    private ItemWriter<Person> writer;

    @Bean
    public PersonItemCapitalizeProcessor dbCapitalizePerson() {
        return new PersonItemCapitalizeProcessor();
    }

    @Bean
    @Qualifier("dbCapitalizestep1")
    public Step dbCapitalizestep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person>chunk(10, transactionManager)
                .reader(reader)
                .processor(dbCapitalizePerson())
                .writer(writer)
                /* step 실행 시 오류 처리 이벤트 설정 */
                .listener((ItemReadListener) new ItemFailureLoggerListener())
                .listener((ItemProcessListener) new ItemFailureLoggerListener())
                .listener((ItemWriteListener) new ItemFailureLoggerListener())
                .build();
    }

    @Bean
    @Qualifier("capitalizeJob")
    public Job importCapitalizeUserJob(JobRepository jobRepository,
                                       JobCompletionNotificationListener listener, @Qualifier("dbCapitalizestep1") Step step1) {
        return new JobBuilder("importCapitalizeUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
