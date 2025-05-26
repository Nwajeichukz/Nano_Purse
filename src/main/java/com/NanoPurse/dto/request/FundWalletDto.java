package com.NanoPurse.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class FundWalletDto {
    @NotEmpty(message = "userId should not be blank")
    private String userId;

    @NotEmpty(message = "reference should not be blank")
    private String reference;

    @NotNull(message = "amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be greater than 0")
    private BigDecimal amount;
}
