package com.example.batchprocessing;

import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchDB2DBConfiguration {
    private static final int chunkSize= 10;
    @Autowired
    private DataSource dataSource;
    @Bean
    public JdbcCursorItemReader<Person> dbReader() {
        return new JdbcCursorItemReaderBuilder<Person>()
                .name("personDB2DBItemReader")
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Person.class))
                .sql("SELECT person_id, first_name, last_name from BAT_TEST_PERSON")
                .build();
    }

    @Bean
    public PersonItemProcessor dbProcessor() {
        return new PersonItemProcessor();
    }

    @Bean
    @Qualifier("dbToDBWriter")
    public JdbcBatchItemWriter<Person> dbToDBWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("Update BAT_TEST_PERSON set first_name = :first_name, last_name = :last_name where person_id = :person_id")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    @Qualifier("dbstep1")
    public Step dbStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager, @Qualifier("dbToDBWriter") JdbcBatchItemWriter<Person> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person> chunk(10, transactionManager)
                .reader(dbReader())
                .processor(dbProcessor())
                .writer(writer)
                /* step 실행 시 오류 처리 이벤트 설정 */
                .listener((ItemReadListener) new ItemFailureLoggerListener())
                .listener((ItemProcessListener) new ItemFailureLoggerListener())
                .listener((ItemWriteListener) new ItemFailureLoggerListener())
                .build();
    }

    @Bean
    public Job importDBUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, @Qualifier("dbstep1") Step step1) {
        return new JobBuilder("importDBUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
