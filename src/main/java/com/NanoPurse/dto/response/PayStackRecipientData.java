package com.NanoPurse.dto.response;

import lombok.Data;

@Data
public class PayStackRecipientData {
    private String recipient_code;
    private String name;
    private String description;
    private String type;
    private String currency;
    private String account_number;
    private String bank_code;
}
