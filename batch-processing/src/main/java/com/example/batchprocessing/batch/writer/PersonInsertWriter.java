package com.example.batchprocessing.batch.writer;

import com.example.batchprocessing.Person;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

public class PersonInsertWriter {
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("Insert INTO bat_test_person (first_name, last_name) values (:first_name, :last_name)")
                .dataSource(dataSource)
                .build();
    }
}
