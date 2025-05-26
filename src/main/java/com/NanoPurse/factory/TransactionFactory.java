package com.NanoPurse.factory;

import com.NanoPurse.enums.TransactionStatusEnum;
import com.NanoPurse.enums.TransactionTypeEnum;
import com.NanoPurse.model.Transaction;

import java.math.BigDecimal;

public class TransactionFactory {
    private TransactionFactory() {
        // Prevent instantiation
    }

    public static Transaction createTransaction(
            BigDecimal amount,
            String reference,
            TransactionTypeEnum transactionType,
            TransactionStatusEnum status,
            String walletId
    ) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setReference(reference);
        transaction.setType(transactionType);
        transaction.setStatus(status);
        transaction.setWalletId(walletId);

        return transaction;
    }
}
