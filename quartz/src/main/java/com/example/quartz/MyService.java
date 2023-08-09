package com.example.quartz;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MyService {
    public void someMethod(Date date, String name) {
        System.out.println("Something");
    }
}
