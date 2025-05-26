package com.NanoPurse.dto.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class RegistrationDto {
    @NotEmpty(message = "email should not be blank")
    @Email(message = "use email format")
    private String email;

    @javax.validation.constraints.Pattern(
            regexp = "USER",
            message = "role should be USER,"
    )
    private String role;

    @javax.validation.constraints.Pattern(
            regexp = "NGN",
            message = "currency should be NGN,"
    )    private String currency;

    @NotEmpty(message = "password should not be blank")
    private String password;

    @NotEmpty(message = "confirmPassword should not be blank")
    private String confirmPassword;
}
