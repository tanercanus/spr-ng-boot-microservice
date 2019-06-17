package com.tanerus.microservice.accountbalancecalculator;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients("com.tanerus.microservice.accountbalancecalculator")
@EnableDiscoveryClient
public class AccountBalanceCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountBalanceCalculatorApplication.class, args);
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

}
