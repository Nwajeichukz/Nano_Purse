package com.NanoPurse.service.impl;

import com.NanoPurse.model.Transaction;
import com.NanoPurse.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PaymentPersistenceService {
    private final TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

}
