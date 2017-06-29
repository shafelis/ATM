package com.ATMTestCase;
import java.math.BigDecimal;

public class TransactionsDaily {
    Integer transactionFrequency;
    BigDecimal dailySum;

    public TransactionsDaily(Integer transactionFrequency, BigDecimal dailySum){
        this.transactionFrequency = transactionFrequency;
        this.dailySum = dailySum;

    }
}
