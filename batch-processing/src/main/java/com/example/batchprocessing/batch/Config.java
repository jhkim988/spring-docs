package com.example.batchprocessing.batch;

import com.example.batchprocessing.Person;
import com.example.batchprocessing.batch.reader.PersonMybatisReader;
import com.example.batchprocessing.batch.writer.PersonMybatisWriter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class Config {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public ItemReader<Person> itemReader() {
//        PersonCSVReader readerFactory = new PersonCSVReader();
//        PersonDBCursorReader readerFactory = new PersonDBCursorReader(dataSource);
//        PersonDBPagingReader readerFactory = new PersonDBPagingReader(dataSource);
        PersonMybatisReader readerFactory = new PersonMybatisReader(sqlSessionFactory);
        return readerFactory.reader();
    }


    @Bean
    public ItemWriter<Person> itemWriter() {
//        PersonInsertWriter writerFactory = new PersonInsertWriter();
//        PersonUpdateWriter writerFactory = new PersonUpdateWriter();
//        return writerFactory.writer(dataSource);
        PersonMybatisWriter writerFactory = new PersonMybatisWriter();
        return writerFactory.writer(sqlSessionFactory);
    }
}
