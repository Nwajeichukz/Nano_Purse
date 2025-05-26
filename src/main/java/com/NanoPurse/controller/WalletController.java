package com.NanoPurse.controller;

import com.NanoPurse.dto.request.FundWalletDto;
import com.NanoPurse.dto.request.TransferFundsDto;
import com.NanoPurse.dto.response.AppResponse;
import com.NanoPurse.model.Transaction;
import com.NanoPurse.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
@PreAuthorize("hasRole('USER')")
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/balance/{userId}")
    AppResponse<String> getBalance(@PathVariable String userId){
        return walletService.getWalletBalance(userId);
    }

    @PostMapping("/fund")
    AppResponse<String> fundWallet(@Valid @RequestBody FundWalletDto fundWalletDto){
        return walletService.fundAccount(fundWalletDto);
    }

    @GetMapping("/transactions/{walletId}")
    public Page<Transaction> getAllTransaction(@PageableDefault(size = 10, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String walletId){
        return walletService.getAllTransactions(pageable, walletId);
    }

    @PostMapping("/transfer")
    public AppResponse<Transaction> transferFunds(@RequestBody @Valid TransferFundsDto transferFundsDto){
        return walletService.transferFunds(transferFundsDto);
    }

}
