package com.tanerus.microservice.accountbalancecalculator.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalanceCalculatorDTO {

    private BigDecimal euroBalance;
    private BigDecimal usdBalance;
    private BigDecimal tryBalance;

    private BigDecimal totalBalance;
    private Currency totalCurrency;

    public enum Currency {
        TRY, USD, EUR
    }

}
