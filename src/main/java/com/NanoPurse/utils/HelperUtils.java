package com.NanoPurse.utils;

import com.NanoPurse.exception.ApiException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HelperUtils {

    public static long toKobo(BigDecimal amount){
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

    public static void fundsCheck(BigDecimal balance, BigDecimal amount){
        if (balance.compareTo(amount) < 0) {
            throw new ApiException("Insufficient funds");
        }
    }


    public static BigDecimal transactionFee(BigDecimal amount){
        BigDecimal dynamicRate = new BigDecimal("0.02"); //base rate

        if (amount.compareTo(new BigDecimal("500")) > 0){
            dynamicRate = dynamicRate.add(new BigDecimal("0.02")); // add 2% if amount > 1000
        }


        if (amount.compareTo(new BigDecimal("1000")) > 0){
            dynamicRate = dynamicRate.add(new BigDecimal("0.05")); // add 2% if amount > 1000
        }


        if (amount.compareTo(new BigDecimal("10000")) > 0){
            dynamicRate = dynamicRate.add(new BigDecimal("0.09")); // add 2% if amount > 1000
        }


        return dynamicRate;
    }

}



