package com.NanoPurse.service;

import com.NanoPurse.dto.request.FundWalletDto;
import com.NanoPurse.dto.request.TransferFundsDto;
import com.NanoPurse.dto.response.AppResponse;
import com.NanoPurse.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;

public interface WalletService {
    AppResponse<String> getWalletBalance(String userId);

    AppResponse<String> fundAccount(FundWalletDto fundWalletDto);

    Page<Transaction> getAllTransactions(Pageable pageable, String walletId);

    AppResponse<Transaction> transferFunds(TransferFundsDto transferFundsDto);
}
