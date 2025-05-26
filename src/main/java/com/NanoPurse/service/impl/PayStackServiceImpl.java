package com.NanoPurse.service.impl;

import com.NanoPurse.dto.request.ExternalTransferRequestDto;
import com.NanoPurse.dto.request.PayStackWebhookEvent;
import com.NanoPurse.dto.response.PayStackTransferData;
import com.NanoPurse.enums.TransactionStatusEnum;
import com.NanoPurse.enums.TransactionTypeEnum;
import com.NanoPurse.exception.ApiException;
import com.NanoPurse.externals.PayStackAgent;
import com.NanoPurse.factory.TransactionFactory;
import com.NanoPurse.model.Transaction;
import com.NanoPurse.model.Wallet;
import com.NanoPurse.repository.TransactionRepository;
import com.NanoPurse.repository.WalletRepository;
import com.NanoPurse.service.PayStackService;
import com.NanoPurse.utils.HelperUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PayStackServiceImpl implements PayStackService {

    private final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    private final PayStackAgent payStackAgent;

    @Transactional
    @Override
    public Transaction transferToBank(ExternalTransferRequestDto requestDto) {
        Wallet wallet = walletRepository.findByWalletUserId(requestDto.getUserId())
                .orElseThrow(() -> new ApiException("User Id for wallet Not found"));

        BigDecimal amountAfterFee = HelperUtils .transactionFee(requestDto.getAmount());

        HelperUtils.fundsCheck(wallet.getBalance(), requestDto.getAmount());


        String recipientCode = payStackAgent.createRecipient(requestDto);

        PayStackTransferData data = payStackAgent.initiateTransfer(recipientCode, requestDto, amountAfterFee);

        return TransactionFactory.createTransaction(
                amountAfterFee,
                data.getReference(),
                TransactionTypeEnum.BANK_TRANSFER,
                TransactionStatusEnum.PENDING,
                wallet.getId());
    }

    @Override
    public void handleTransferWebhook(PayStackWebhookEvent event) {

        if ("transfer.success".equals(event.getEvent())) {
            PayStackWebhookEvent.PaystackTransferData data = event.getData();
            Transaction transaction = transactionRepository.findByReference(data.getReference())
                    .orElseThrow(() -> new ApiException("Transaction not found"));

            if (transaction.getStatus() != TransactionStatusEnum.SUCCESSFUL) {

                Wallet wallet = walletRepository.findById(transaction.getWalletId())
                        .orElseThrow(() -> new ApiException("wallet Not found"));

                wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount().abs()));
                walletRepository.save(wallet);

                transaction.setStatus(TransactionStatusEnum.SUCCESSFUL);
                transactionRepository.save(transaction);
            }
        } else if ("transfer.failed".equals(event.getEvent())) {
            PayStackWebhookEvent.PaystackTransferData data = event.getData();
            Transaction transaction = transactionRepository.findByReference(data.getReference())
                    .orElseThrow(() -> new ApiException("Transaction not found"));

            transaction.setStatus(TransactionStatusEnum.FAILED);
            transactionRepository.save(transaction);
        }
    }


}