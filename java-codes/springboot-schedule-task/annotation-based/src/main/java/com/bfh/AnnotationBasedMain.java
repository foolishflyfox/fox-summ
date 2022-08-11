package com.bfh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 参考: https://blog.csdn.net/qianlixiaomage/article/details/106599951
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class AnnotationBasedMain {
    public static void main(String[] args) {
        SpringApplication.run(AnnotationBasedMain.class, args);
    }

//    @Scheduled(cron = "*/5 * * * * ?")  // 每隔 5s 执行一次
//    public void sayHello() {
//        log.info("hello");
//    }

    @Scheduled(
            fixedDelay = 2000    // 在上一个任务执行结束后，间隔2s再执行下一个
            ,initialDelay = 3000  // 第一次启动间隔 3s
    )
    void fixedDelay() throws InterruptedException {
        log.info("fixed Delay start");
        Thread.sleep(5000);
        log.info("fixed Delay end");
    }

}
