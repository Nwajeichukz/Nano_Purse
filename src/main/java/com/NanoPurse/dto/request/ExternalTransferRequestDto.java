package com.NanoPurse.dto.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ExternalTransferRequestDto {
    @NotEmpty(message = "userId should not be blank")
    private String userId;

    @NotEmpty(message = "accountNumber should not be blank")
    private String accountNumber;

    @NotEmpty(message = "bankCode should not be blank")
    private String bankCode;

    @NotEmpty(message = "accountName should not be blank")
    private String accountName;

    @NotNull(message = "amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be greater than 0")
    private BigDecimal amount;

    private String reason;
}
