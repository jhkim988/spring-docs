package com.example.batchprocessing.batch;

import com.example.batchprocessing.Person;
import com.example.batchprocessing.batch.reader.PersonCSVReader;
import com.example.batchprocessing.batch.reader.PersonDBCursorReader;
import com.example.batchprocessing.batch.writer.PersonInsertWriter;
import com.example.batchprocessing.batch.writer.PersonUpdateWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public ItemReader<Person> itemReader() {
        PersonCSVReader readerFactory = new PersonCSVReader();
//        PersonDBCursorReader readerFactory = new PersonDBCursorReader(dataSource);
        return readerFactory.reader();
    }


    @Bean
    public ItemWriter<Person> itemWriter() {
        PersonInsertWriter writerFactory = new PersonInsertWriter();
//        PersonUpdateWriter writerFactory = new PersonUpdateWriter();
        return writerFactory.writer(dataSource);
    }
}
