package com.NanoPurse.externals;


import com.NanoPurse.dto.request.ExternalTransferRequestDto;
import com.NanoPurse.dto.request.PayStackRecipientDto;
import com.NanoPurse.dto.request.PayStackTransferRequestDto;
import com.NanoPurse.dto.response.PayStackRecipientData;
import com.NanoPurse.dto.response.PayStackResponse;
import com.NanoPurse.dto.response.PayStackTransferData;
import com.NanoPurse.exception.ApiException;

import com.NanoPurse.utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PayStackAgent {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${paystack.secret.key}")
    private String payStackSecretKey;

    private final String BASE_URL = "https://api.paystack.co";


    public String createRecipient(ExternalTransferRequestDto requestDto){
        String url = BASE_URL + "/transferrecipient";

        PayStackRecipientDto body = PayStackRecipientDto.builder()
                .type("nuban")
                .name(requestDto.getAccountName())
                .account_number(requestDto.getAccountNumber())
                .bank_code(requestDto.getBankCode())
                .currency("NGN")
                .build();

        HttpEntity<PayStackRecipientDto> entity = new HttpEntity<>(body, createHeaders());

        ResponseEntity<PayStackResponse<PayStackRecipientData>> response = restTemplate.exchange(
                url, HttpMethod.POST, entity,  new ParameterizedTypeReference<>() {});


        PayStackResponse<PayStackRecipientData> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null || !responseBody.isStatus()) {
            String message = (responseBody != null) ? responseBody.getMessage() : "No response body from Paystack";
            throw new ApiException("Failed to create recipient: " + message);
        }

        return responseBody.getData().getRecipient_code();
    }

    public PayStackTransferData initiateTransfer(
            String recipientCode, ExternalTransferRequestDto requestDto, BigDecimal amountAfterFee){

        String url = BASE_URL + "/transfer";

        long amount = HelperUtils.toKobo(amountAfterFee);


        PayStackTransferRequestDto request = new PayStackTransferRequestDto();
        request.setAmount(amount);
        request.setRecipient(recipientCode);
        request.setReason(requestDto.getReason());

        HttpEntity<PayStackTransferRequestDto> entity = new HttpEntity<>(request, createHeaders());

        ResponseEntity<PayStackResponse<PayStackTransferData>> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {}
        );

        PayStackResponse<PayStackTransferData> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null || !responseBody.isStatus()) {
            String message = (responseBody != null) ? responseBody.getMessage() : "No response body from Paystack";
            throw new ApiException("Failed to initiate transfer: " + message);
        }


        return responseBody.getData();
    }

    private HttpHeaders createHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(payStackSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

}
