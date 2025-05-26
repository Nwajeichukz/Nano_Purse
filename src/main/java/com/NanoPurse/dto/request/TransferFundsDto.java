package com.NanoPurse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TransferFundsDto {
    @NotEmpty(message = "senderUserId should not be blank")
    private String senderUserId;

    @NotEmpty(message = "receiverUserId should not be blank")
    private String receiverUserId;

    @NotNull(message = "amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be greater than 0")
    private BigDecimal amount;
}
