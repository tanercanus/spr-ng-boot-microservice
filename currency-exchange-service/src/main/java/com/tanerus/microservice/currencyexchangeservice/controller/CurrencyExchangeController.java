package com.tanerus.microservice.currencyexchangeservice.controller;

import com.tanerus.microservice.currencyexchangeservice.entity.ExchangeRate;
import com.tanerus.microservice.currencyexchangeservice.repository.ExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.env.Environment;

@RestController
@Slf4j
public class CurrencyExchangeController {

    @Autowired
    private Environment environment;

    @Autowired
    private ExchangeRateRepository repository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeRate retrieveExchangeValue
            (@PathVariable String from, @PathVariable String to) {

        ExchangeRate exchangeValue =
                repository.findByFromAndTo(from, to);

        exchangeValue.setPort(
                Integer.parseInt(environment.getProperty("local.server.port")));

        log.info("{}", exchangeValue);

        return exchangeValue;
    }

}
