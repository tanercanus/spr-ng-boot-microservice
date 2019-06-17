package com.tanerus.microservice.accountbalancecalculator.controller;

import com.tanerus.microservice.accountbalancecalculator.dto.AccountBalanceCalculatorDTO;
import com.tanerus.microservice.accountbalancecalculator.dto.externalservice.CurrencyConversionDTO;
import com.tanerus.microservice.accountbalancecalculator.feign.CurrencyExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@RestController
@Slf4j
public class AccountBalanceCalculatorController {

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    private static BigDecimal eurToTry = new BigDecimal(6.70, new MathContext(10));
    private static BigDecimal usdToTry = new BigDecimal(5.60, new MathContext(10));

    private static BigDecimal eurToUsd = eurToTry.divide(usdToTry, 10, RoundingMode.CEILING);
    private static BigDecimal tryToUsd = BigDecimal.ONE.divide(usdToTry, 10, RoundingMode.CEILING);

    private static BigDecimal tryToEur = BigDecimal.ONE.divide(eurToTry, 10, RoundingMode.CEILING);
    private static BigDecimal usdToEur = usdToTry.divide(eurToTry, 10, RoundingMode.CEILING);


    @GetMapping("/account-balance-calculator/{userId}/{totalCurencyType}")
    public AccountBalanceCalculatorDTO convertCurrency(@PathVariable String userId,
                                                       @PathVariable AccountBalanceCalculatorDTO.Currency totalCurencyType) {

        AccountBalanceCalculatorDTO accountBalanceCalculatorDTO = new AccountBalanceCalculatorDTO();

        //It can get from User account table with userId
        accountBalanceCalculatorDTO.setEuroBalance(new BigDecimal(2000));
        accountBalanceCalculatorDTO.setTryBalance(new BigDecimal(1000));
        accountBalanceCalculatorDTO.setUsdBalance(new BigDecimal(4000));

        BigDecimal totalBalance = BigDecimal.ZERO;

        switch (totalCurencyType) {
            case USD:

                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getEuroBalance().multiply(eurToUsd));
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getTryBalance().multiply(tryToUsd));
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getUsdBalance());

                accountBalanceCalculatorDTO.setTotalBalance(totalBalance.setScale(2, RoundingMode.CEILING));
                accountBalanceCalculatorDTO.setTotalCurrency(totalCurencyType);

                break;
            case EUR:

                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getEuroBalance());
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getTryBalance().multiply(tryToEur));
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getUsdBalance().multiply(usdToEur));

                accountBalanceCalculatorDTO.setTotalBalance(totalBalance.setScale(2, RoundingMode.CEILING));
                accountBalanceCalculatorDTO.setTotalCurrency(totalCurencyType);

                break;
            default:

                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getEuroBalance().multiply(eurToTry));
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getTryBalance());
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getUsdBalance().multiply(usdToTry));

                accountBalanceCalculatorDTO.setTotalBalance(totalBalance.setScale(2, RoundingMode.CEILING));
                accountBalanceCalculatorDTO.setTotalCurrency(totalCurencyType);

                break;
        }

        return accountBalanceCalculatorDTO;

    }

    @GetMapping("/account-balance-calculator-feign/{userId}/{totalCurencyType}")
    public AccountBalanceCalculatorDTO convertCurrencyFeign(@PathVariable String userId,
                                                            @PathVariable AccountBalanceCalculatorDTO.Currency totalCurencyType) {

        AccountBalanceCalculatorDTO accountBalanceCalculatorDTO = new AccountBalanceCalculatorDTO();

        //It can get from User account table with userId
        accountBalanceCalculatorDTO.setEuroBalance(new BigDecimal(2000));
        accountBalanceCalculatorDTO.setTryBalance(new BigDecimal(1000));
        accountBalanceCalculatorDTO.setUsdBalance(new BigDecimal(4000));

        BigDecimal totalBalance = BigDecimal.ZERO;

        switch (totalCurencyType) {
            case USD:
                CurrencyConversionDTO responseEurToUsd = currencyExchangeService.retrieveExchangeValue(AccountBalanceCalculatorDTO.Currency.EUR, AccountBalanceCalculatorDTO.Currency.USD);
                log.info("responseEurToUsd: {}", responseEurToUsd);
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getEuroBalance().multiply(responseEurToUsd.getConversionMultiple()));

                CurrencyConversionDTO responseTryToUsd = currencyExchangeService.retrieveExchangeValue(AccountBalanceCalculatorDTO.Currency.TRY, AccountBalanceCalculatorDTO.Currency.USD);
                log.info("responseTryToUsd: {}", responseTryToUsd);
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getTryBalance().multiply(responseTryToUsd.getConversionMultiple()));

                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getUsdBalance());

                accountBalanceCalculatorDTO.setTotalBalance(totalBalance.setScale(2, RoundingMode.CEILING));
                accountBalanceCalculatorDTO.setTotalCurrency(totalCurencyType);

                break;
            case EUR:
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getEuroBalance());

                responseTryToUsd = currencyExchangeService.retrieveExchangeValue(AccountBalanceCalculatorDTO.Currency.TRY, AccountBalanceCalculatorDTO.Currency.USD);
                log.info("responseTryToUsd: {}", responseTryToUsd);
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getTryBalance().multiply(responseTryToUsd.getConversionMultiple()));

                CurrencyConversionDTO responseUsdToTry = currencyExchangeService.retrieveExchangeValue(AccountBalanceCalculatorDTO.Currency.USD, AccountBalanceCalculatorDTO.Currency.TRY);
                log.info("responseUsdToTry: {}", responseUsdToTry);
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getUsdBalance().multiply(responseUsdToTry.getConversionMultiple()));

                accountBalanceCalculatorDTO.setTotalBalance(totalBalance.setScale(2, RoundingMode.CEILING));
                accountBalanceCalculatorDTO.setTotalCurrency(totalCurencyType);

                break;
            case TRY:

                CurrencyConversionDTO responseEurToTry = currencyExchangeService.retrieveExchangeValue(AccountBalanceCalculatorDTO.Currency.EUR, AccountBalanceCalculatorDTO.Currency.TRY);
                log.info("responseEurToTry: {}", responseEurToTry);
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getEuroBalance().multiply(responseEurToTry.getConversionMultiple()));

                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getTryBalance());

                responseUsdToTry = currencyExchangeService.retrieveExchangeValue(AccountBalanceCalculatorDTO.Currency.USD, AccountBalanceCalculatorDTO.Currency.TRY);
                log.info("responseUsdToTry: {}", responseUsdToTry);
                totalBalance = totalBalance.add(accountBalanceCalculatorDTO.getUsdBalance().multiply(usdToTry));

                accountBalanceCalculatorDTO.setTotalBalance(totalBalance.setScale(2, RoundingMode.CEILING));
                accountBalanceCalculatorDTO.setTotalCurrency(totalCurencyType);

                break;
            default:
                break;
        }

        return accountBalanceCalculatorDTO;

    }


}
