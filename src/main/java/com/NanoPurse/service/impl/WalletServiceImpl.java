package com.NanoPurse.service.impl;

import com.NanoPurse.dto.request.FundWalletDto;
import com.NanoPurse.dto.request.TransferFundsDto;
import com.NanoPurse.dto.response.AppResponse;
import com.NanoPurse.enums.TransactionStatusEnum;
import com.NanoPurse.enums.TransactionTypeEnum;
import com.NanoPurse.exception.ApiException;
import com.NanoPurse.factory.TransactionFactory;
import com.NanoPurse.model.Transaction;
import com.NanoPurse.model.Wallet;
import com.NanoPurse.repository.TransactionRepository;
import com.NanoPurse.repository.WalletRepository;
import com.NanoPurse.service.WalletService;
import com.NanoPurse.utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final String reference = "REF-768933";

    private final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    private final PaymentPersistenceService persistenceService;

    @Override
    public AppResponse<String> getWalletBalance(String userId) {
        Wallet wallet = walletRepository.findByWalletUserId(userId)
                .orElseThrow(() -> new ApiException("User Id for wallet Not found"));

        return new AppResponse<>("wallet balance", wallet.getBalance().toString());
    }

    @Transactional
    @Override
    public AppResponse<String> fundAccount(FundWalletDto fundWalletDto) {
        Wallet wallet = walletRepository.findByWalletUserId(fundWalletDto.getUserId())
                .orElseThrow(() -> new ApiException("Wallet not found"));

        BigDecimal amount = fundWalletDto.getAmount();

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        Transaction transaction = TransactionFactory.createTransaction(
                amount, fundWalletDto.getReference(), TransactionTypeEnum.DEPOSIT,
                TransactionStatusEnum.SUCCESSFUL, wallet.getId());


        transactionRepository.save(transaction);

        return new AppResponse<>("wallet funded", transaction.getAmount().toString()) ;
    }

    @Override
    public Page<Transaction> getAllTransactions(Pageable pageable, String walletId) {
        return transactionRepository.findAllByWalletId(pageable, walletId);
    }

    @Transactional
    @Override
    public AppResponse<Transaction> transferFunds(TransferFundsDto transferFundsDto) {
        BigDecimal amount =  HelperUtils.transactionFee(transferFundsDto.getAmount());

        Wallet senderWallet = walletRepository.findByWalletUserId(transferFundsDto.getSenderUserId())
                .orElseThrow(() -> new ApiException("sender wallet not found"));

        Wallet receiverWallet = walletRepository.findByWalletUserId(transferFundsDto.getReceiverUserId())
                .orElseThrow(() -> new ApiException("receiver wallet not found"));

        Transaction senderTransaction = TransactionFactory.createTransaction(
                transferFundsDto.getAmount(), reference, TransactionTypeEnum.TRANSFER,
                TransactionStatusEnum.FAILED, senderWallet.getId());

        persistenceService.saveFailedTransaction(senderTransaction);

        HelperUtils.fundsCheck(senderWallet.getBalance(), amount);

        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receiverWallet.setBalance(receiverWallet.getBalance().add(transferFundsDto.getAmount()));

        Transaction receiverTransaction = TransactionFactory.createTransaction(
                amount, reference, TransactionTypeEnum.TRANSFER,
                TransactionStatusEnum.SUCCESSFUL, receiverWallet.getId());

        transactionRepository.save(receiverTransaction);

        senderTransaction.setStatus(TransactionStatusEnum.SUCCESSFUL);
        senderTransaction.setAmount(amount);

        transactionRepository.save(senderTransaction);
        return new AppResponse<>("funds transfer", senderTransaction);
    }
}
