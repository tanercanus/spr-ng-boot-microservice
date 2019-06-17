package com.tanerus.microservice.accountbalancecalculator.feign;

import com.tanerus.microservice.accountbalancecalculator.dto.AccountBalanceCalculatorDTO;
import com.tanerus.microservice.accountbalancecalculator.dto.externalservice.CurrencyConversionDTO;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "currency-exchange-service", url = "localhost:8000")
@FeignClient(name="currency-exchange-service")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeService {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
        //@GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
    CurrencyConversionDTO retrieveExchangeValue(@PathVariable("from") AccountBalanceCalculatorDTO.Currency from, @PathVariable("to") AccountBalanceCalculatorDTO.Currency to);

}
