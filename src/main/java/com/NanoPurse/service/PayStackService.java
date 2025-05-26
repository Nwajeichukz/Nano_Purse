package com.NanoPurse.service;

import com.NanoPurse.dto.request.ExternalTransferRequestDto;
import com.NanoPurse.dto.request.PayStackWebhookEvent;
import com.NanoPurse.model.Transaction;


public interface PayStackService {
    Transaction transferToBank(ExternalTransferRequestDto requestDto);

    void handleTransferWebhook(PayStackWebhookEvent event);
}
