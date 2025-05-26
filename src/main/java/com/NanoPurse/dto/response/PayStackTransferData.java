package com.NanoPurse.dto.response;

import lombok.Data;

@Data
public class PayStackTransferData {
    private String transfer_code;
    private String reference;
    private String status;
}
