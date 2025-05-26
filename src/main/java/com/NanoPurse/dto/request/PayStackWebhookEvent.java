package com.NanoPurse.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class PayStackWebhookEvent {
    private String event;
    private PaystackTransferData data;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaystackTransferData {
        private int amount;
        private String currency;
        private String domain;
        private Object failures;
        private long id;
        private Integration integration;
        private String reason;
        private String reference;
        private String source;
        private Object sourceDetails;
        private String status;
        private Object titanCode;
        private String transferCode;
        private Object transferredAt;
        private Recipient recipient;
        private Session session;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recipient {
        private boolean active;
        private String currency;
        private String description;
        private String domain;
        private String email;
        private long id;
        private long integration;
        private Object metadata;
        private String name;
        private String recipientCode;
        private String type;
        private boolean isDeleted;
        private RecipientDetails details;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;
    }



    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecipientDetails {
        private String accountNumber;
        private String accountName;
        private String bankCode;
        private String bankName;
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Session {
        private Object provider;
        private Object id;
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Integration {
        private long id;
        private boolean isLive;
        private String businessName;
    }

}
