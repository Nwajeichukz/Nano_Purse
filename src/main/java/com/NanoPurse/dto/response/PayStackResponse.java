package com.NanoPurse.dto.response;

import lombok.Data;

@Data
public class PayStackResponse<T> {
    private boolean status;
    private String message;
    private T data;
}
