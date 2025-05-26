package com.NanoPurse.dto.request;

import lombok.Data;

@Data
public class PayStackTransferRequestDto {
    private String source = "balance";
    private long amount;
    private String recipient;
    private String reason;
}
