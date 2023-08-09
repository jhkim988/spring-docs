package com.example.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class QuartzConfig {
    private static final Logger logger = LoggerFactory.getLogger(QuartzConfig.class);
    private final ApplicationContext ctx;

    @Autowired
    public QuartzConfig(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        AutoWiringSpringBeanJobFactory autoWiringSpringBeanJobFactory = new AutoWiringSpringBeanJobFactory();
        autoWiringSpringBeanJobFactory.setApplicationContext(ctx);
        schedulerFactoryBean.setJobFactory(autoWiringSpringBeanJobFactory);

        /* Job Store Type 을 DB 로 했을 때 설정*/
//        schedulerFactoryBean.setDataSource(dataSource);
//        schedulerFactoryBean.setOverwriteExistingJobs(true);
//        schedulerFactoryBean.setAutoStartup(true);
//        schedulerFactoryBean.setTransactionManager(platformTransactionManager);

        /* 따로 properties 파일을 관리할 때 설정 */
//        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        return schedulerFactoryBean;
    }

    /* 따로 properties 파일을 관리할 때 설정 */
    private Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        } catch (IOException e) {
            logger.error("quartzProperties parse error : {}", e);
        }
        return properties;
    }
}
