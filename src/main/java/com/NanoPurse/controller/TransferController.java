package com.NanoPurse.controller;

import com.NanoPurse.dto.request.ExternalTransferRequestDto;
import com.NanoPurse.dto.request.PayStackWebhookEvent;
import com.NanoPurse.model.Transaction;
import com.NanoPurse.service.PayStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("transfer")
public class TransferController {
    private final PayStackService payStackService;

    @PostMapping("/bank")
    public ResponseEntity<Transaction> transferToBank(@Valid @RequestBody ExternalTransferRequestDto requestDto){
        return ResponseEntity.ok(payStackService.transferToBank(requestDto));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebHook(@Valid @RequestBody PayStackWebhookEvent event){
        payStackService.handleTransferWebhook(event);
        return ResponseEntity.ok("Webhook processed");
    }

}
