package com.tanerus.microservice.currencyexchangeservice.repository;

import com.tanerus.microservice.currencyexchangeservice.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends
        JpaRepository<ExchangeRate, Long> {

    ExchangeRate findByFromAndTo(String from, String to);
}
