package com.NanoPurse.repository;

import com.NanoPurse.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, String> {
   @Query("SELECT c FROM Wallet c WHERE c.user.id = :userId")
   Optional<Wallet> findByWalletUserId(String userId);
}
