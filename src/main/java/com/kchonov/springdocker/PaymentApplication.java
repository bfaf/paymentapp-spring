package com.kchonov.springdocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
        // ApplicationContext appContext = 
        //BinarySearchImpl binarySearch = appContext.getBean(BinarySearchImpl.class);
        //int result = binarySearch.binarySearch(new int[] {10, 2}, 3);
        //System.out.println(result);
    }

}
