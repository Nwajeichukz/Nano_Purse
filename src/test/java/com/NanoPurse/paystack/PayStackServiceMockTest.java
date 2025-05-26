package com.NanoPurse.paystack;

import com.NanoPurse.BaseIntegrationTest;
import com.NanoPurse.dto.request.ExternalTransferRequestDto;
import com.NanoPurse.dto.request.PayStackWebhookEvent;
import com.NanoPurse.dto.response.PayStackRecipientData;
import com.NanoPurse.dto.response.PayStackResponse;
import com.NanoPurse.dto.response.PayStackTransferData;
import com.NanoPurse.enums.TransactionStatusEnum;
import com.NanoPurse.exception.ApiException;
import com.NanoPurse.model.Transaction;
import com.NanoPurse.model.User;
import com.NanoPurse.model.Wallet;
import com.NanoPurse.repository.TransactionRepository;
import com.NanoPurse.repository.WalletRepository;
import com.NanoPurse.service.PayStackService;
import com.NanoPurse.service.impl.PayStackServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PayStackServiceMockTest extends BaseIntegrationTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private PayStackServiceImpl payStackService;

    private static final String TEST_USER1_ID = "1am39e0d-4086-7386-bkl5-r283cp0264b6";

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000");

    private Wallet wallet;
    private User user;

    @BeforeEach
    void setup() {
    }

    @AfterEach
    void resetMockServer() {
        mockServer.reset();
    }


    @Test
    public void testTransferToBank_userWalletNotFound() {
        ExternalTransferRequestDto request = new ExternalTransferRequestDto();
        request.setUserId("non_existing_user_id");
        request.setAmount(new BigDecimal("100"));
        request.setAccountName("Test User");
        request.setAccountNumber("0000000000");
        request.setBankCode("057");
        request.setReason("Test transfer");

        ApiException exception = assertThrows(ApiException.class, () -> {
            payStackService.transferToBank(request);
        });

        assertEquals("User Id for wallet Not found", exception.getMessage());
    }


    @Test
    public void testTransferToBank_InsufficientBalance() {
        // Create test request with amount > balance
        ExternalTransferRequestDto request = new ExternalTransferRequestDto();
        request.setUserId(TEST_USER1_ID);
        request.setAmount(INITIAL_BALANCE.add(BigDecimal.ONE));
        request.setAccountName("Test User");
        request.setAccountNumber("0000000000");
        request.setBankCode("057");
        request.setReason("Test transfer");

        // Verify exception is thrown
        assertThrows(ApiException.class, () -> {
            payStackService.transferToBank(request);
        });
    }

    @Test
    public void testHandleTransferSuccessWebhook() {
        // Given
        PayStackWebhookEvent event = new PayStackWebhookEvent();
        event.setEvent("transfer.success");

        PayStackWebhookEvent.PaystackTransferData data = new PayStackWebhookEvent.PaystackTransferData();
        data.setReference("TRF_123abc");
        data.setAmount(10000);

        event.setData(data);

        Wallet userWallet =  createWallet();

        Transaction transaction = new Transaction();
        transaction.setReference("TRF_123abc");
        transaction.setAmount(new BigDecimal("100"));
        transaction.setStatus(TransactionStatusEnum.PENDING);
        transaction.setWalletId(userWallet.getId());

        when(transactionRepository.findByReference("TRF_123abc")).thenReturn(Optional.of(transaction));
        when(walletRepository.findById(userWallet.getId())).thenReturn(Optional.of(userWallet));

        // Mock save operations to return the same object
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));


        // When
        payStackService.handleTransferWebhook(event);

        // Then
        assertEquals(TransactionStatusEnum.SUCCESSFUL, transaction.getStatus());
        assertEquals(new BigDecimal("900"), userWallet.getBalance());
        verify(walletRepository).save(userWallet);
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testHandleTransferFailedWebhook() {
        // Given
        PayStackWebhookEvent event = new PayStackWebhookEvent();
        event.setEvent("transfer.failed");

        PayStackWebhookEvent.PaystackTransferData data = new PayStackWebhookEvent.PaystackTransferData();
        data.setReference("TRF_fail_123");

        event.setData(data);

        Transaction transaction = new Transaction();
        transaction.setReference("TRF_fail_123");
        transaction.setStatus(TransactionStatusEnum.PENDING);

        Wallet wallet = createWallet();

        when(transactionRepository.findByReference("TRF_fail_123")).thenReturn(Optional.of(transaction));
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));

        // Mock save operations to return the same object
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        payStackService.handleTransferWebhook(event);

        // Then
        assertEquals(TransactionStatusEnum.FAILED, transaction.getStatus());
        verify(transactionRepository).save(transaction);
        verify(walletRepository, never()).save(any());
    }

    @Test
    public void testHandleTransferWebhook_transactionNotFound() {
        // Given
        PayStackWebhookEvent event = new PayStackWebhookEvent();
        event.setEvent("transfer.success");

        PayStackWebhookEvent.PaystackTransferData data = new PayStackWebhookEvent.PaystackTransferData();
        data.setReference("TRF_not_found");

        event.setData(data);

        when(transactionRepository.findByReference("TRF_not_found")).thenReturn(Optional.empty());

        // Expect exception
        ApiException thrown = assertThrows(ApiException.class, () -> {
            payStackService.handleTransferWebhook(event);
        });

        assertEquals("Transaction not found", thrown.getMessage());
    }

    private Wallet createWallet(){
        // Create a test user
        user = User.builder()
                .id(TEST_USER1_ID)
                .email("test1@example.com")
                .build();


        // Create and save a test wallet
        wallet = Wallet.builder()
                .id("9a939e0d-5986-4380-tdb2-59a3c30k64u4")
                .balance(INITIAL_BALANCE)
                .user(user)
                .currency("NGN")
                .build();

        return wallet;
    }
}
