package com.dashboard.eleonore;

import com.dashboard.eleonore.profile.ProfileScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@PropertySource("classpath:application.properties")
public class EleonoreApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(EleonoreApplication.class);

    @Value("${spring.task.execution.pool.core-size}")
    private String corePoolSize;

    @Value("${spring.task.execution.pool.max-size}")
    private String maxPoolSize;

    @Value("${spring.task.execution.pool.queue-capacity}")
    private String queueCapacity;

    @Value("${spring.task.execution.thread-name-prefix}")
    private String threadNamePrefix;

    public static void main(String[] args) {
        SpringApplication.run(EleonoreApplication.class, args);

        ProfileScheduler.getInstance();
    }

    @Bean
    public Executor taskExecutor() {
        LOGGER.info("Task executor initialization");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Integer.parseInt(this.corePoolSize));
        executor.setMaxPoolSize(Integer.parseInt(this.maxPoolSize));
        executor.setQueueCapacity(Integer.parseInt(this.queueCapacity));
        executor.setThreadNamePrefix(this.threadNamePrefix);

        return executor;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200", "http://eleonore:8080", "http://192.168.99.100:8080");
            }
        };
    }
}
