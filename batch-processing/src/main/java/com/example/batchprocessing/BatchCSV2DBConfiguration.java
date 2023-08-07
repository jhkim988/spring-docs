package com.example.batchprocessing;

import org.springframework.batch.core.*;
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
import org.springframework.beans.factory.annotation.Qualifier;
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
public class BatchCSV2DBConfiguration {
    /**
     * csv 파일을 읽어 Person 객체의 Reader 를 리턴한다.
     */
    @Bean
    public FlatFileItemReader<Person> csvReader() {
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
    public PersonItemProcessor csvProcessor() {
        return new PersonItemProcessor();
    }

    /**
     * processor() 로부터 받은 데이터를 DataSource 에 쓴다.
     */
    @Bean
    @Qualifier("csvToDBWriter")
    public JdbcBatchItemWriter<Person> csvToDBWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("Insert INTO bat_test_person (first_name, last_name) values (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    /**
     * Step 을 정의한다. chunk 사이즈와 ItemReader, ItemProcessor, ItemWriter 를 설정한다.
     */
    @Bean
    @Qualifier("csvStep1")
    public Step csvStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager, @Qualifier("csvToDBWriter") JdbcBatchItemWriter<Person> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person> chunk(10, transactionManager)
                .reader(csvReader())
                .processor(csvProcessor())
                .writer(writer)
                /* step 실행 시 오류 처리 이벤트 설정 */
                .listener((ItemReadListener) new ItemFailureLoggerListener())
                .listener((ItemProcessListener) new ItemFailureLoggerListener())
                .listener((ItemWriteListener) new ItemFailureLoggerListener())
                .build();
    }

    /**
     * Step 을 통해 Job 을 정의한다.
     */
    @Bean
    public Job importCSVUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, @Qualifier("csvStep1") Step step1) {
        return new JobBuilder("importCSVUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
