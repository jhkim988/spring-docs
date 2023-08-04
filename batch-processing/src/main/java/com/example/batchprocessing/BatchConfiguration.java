package com.example.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Spring Batch 에서 Step 은 ItemReader -> ItemProcessor -> ItemWriter 의 단계로 구성된다.
 * 각각의 단계(read, process, write) 를 재사용할 수 있도록 별도의 파일로 분리할 수도 있고, 특수한 작업의 경우 하나로 합칠 수도 있다.
 * Step 을 모아 Job 을 만든다.
 */
@Configuration
public class BatchConfiguration {
    /**
     * csv 파일을 읽어 Person 객체의 Reader 를 리턴한다.
     */
    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv")) // resource 를 읽는다.
                .delimited() // 구분자
                .names(new String[]{"firstName", "lastName"}) // 구분자로 구분하여 이름을 붙인다.
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>(){{ // 이름에 맞게 Person 객체에 매핑한다.
                    setTargetType(Person.class);
                }})
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    /**
     * processor() 로부터 받은 데이터를 DataSource 에 쓴다.
     */
    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("Insert INTO people (first_name, last_name) values (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    /**
     * Step 을 정의한다. chunk 사이즈와 ItemReader, ItemProcessor, ItemWriter 를 설정한다.
     */
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Person> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person> chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    /**
     * Step 을 통해 Job 을 정의한다.
     */
    @Bean
    public Job importUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
