package com.NanoPurse.repository;

import com.NanoPurse.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("SELECT c FROM Transaction c WHERE c.walletId = :walletId")
    Page<Transaction> findAllByWalletId(Pageable pageable, String walletId);

    Optional<Transaction> findByReference(String reference);
}
