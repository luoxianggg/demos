package com.lx.project.demo1.bean;

import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class SchedulerConfig {
    @Bean(name = "SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException{
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setQuartzProperties(quartzProperties());
        return factoryBean;
    }
    @Bean
    public Properties quartzProperties() throws IOException{
        PropertiesFactoryBean  propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.propertites 中的属性被读取并注入后在初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return  propertiesFactoryBean.getObject();

    }

    /*
    * quartz初始化监听器
    */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    @Bean(name = "Scheduler")
    public Scheduler scheduler() throws IOException{
        return schedulerFactoryBean().getScheduler();
    }
}
