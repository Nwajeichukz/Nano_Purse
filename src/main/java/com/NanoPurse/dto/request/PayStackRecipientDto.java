package com.NanoPurse.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayStackRecipientDto {
    private String name;
    private String type;
    private String currency;
    private String account_number;
    private String bank_code;
}
