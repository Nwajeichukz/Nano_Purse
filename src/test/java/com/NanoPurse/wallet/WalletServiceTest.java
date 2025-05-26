package com.NanoPurse.wallet;

import com.NanoPurse.dto.request.FundWalletDto;
import com.NanoPurse.dto.request.TransferFundsDto;
import com.NanoPurse.dto.response.AppResponse;
import com.NanoPurse.enums.TransactionStatusEnum;
import com.NanoPurse.exception.ApiException;
import com.NanoPurse.model.Transaction;
import com.NanoPurse.model.User;
import com.NanoPurse.model.Wallet;
import com.NanoPurse.repository.TransactionRepository;
import com.NanoPurse.repository.WalletRepository;
import com.NanoPurse.service.impl.PaymentPersistenceService;
import com.NanoPurse.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PaymentPersistenceService persistenceService;

    @InjectMocks
    private WalletServiceImpl walletService;

    private static final String TEST_USER1_ID = "1am39e0d-4086-7386-bkl5-r283cp0264b6";
    private static final String TEST_USER2_ID = "9a939e0d-5986-4380-tdb2-59a3c30k64u4";

    private static final String TEST_REFERENCE = "ref-123456";
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000");
    private static final BigDecimal FUND_AMOUNT = new BigDecimal("500");


    private FundWalletDto fundWalletDto;
    private Wallet wallet;
    private User user;
    private User user2;
    private TransferFundsDto transferFundsDto;

    private Wallet receiverWallet;
    @BeforeEach
    void setUp() {
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

         walletRepository.save(wallet);

        transferFundsDto = new TransferFundsDto();
        transferFundsDto.setSenderUserId(TEST_USER1_ID);
        transferFundsDto.setReceiverUserId(TEST_USER2_ID);
        transferFundsDto.setAmount(BigDecimal.valueOf(500));

        // Create a test user
        user2 = User.builder()
                .id(TEST_USER2_ID)
                .email("test2@example.com")
                .build();

        receiverWallet = new Wallet();
        receiverWallet.setId("j5979eht-0988-h389-ydb8-u9a3c30k64ty");
        receiverWallet.setBalance(BigDecimal.valueOf(200));
        receiverWallet.setUser(user2);

        walletRepository.save(receiverWallet);

         fundWalletDto = FundWalletDto.builder()
                .userId(TEST_USER1_ID)
                .amount(FUND_AMOUNT)
                .reference(TEST_REFERENCE)
                .build();
    }

    @Test
    void fundAccount_Successful() {
        // Arrange
        when(walletRepository.findByWalletUserId(fundWalletDto.getUserId()))
                .thenReturn(Optional.of(wallet));

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId("55539e0d-5986-4380-tdb5-59ayc30k64u9");
            return t;
        });

        AppResponse<String> response = walletService.fundAccount(fundWalletDto);

        // Assert
        assertNotNull(response);
        assertEquals("wallet funded", response.getMessage());
        assertEquals("500", response.getData());

        // Verify wallet balance was updated
        assertEquals(BigDecimal.valueOf(1500), wallet.getBalance());
    }

    @Test
    void fundAccount_WalletNotFound_ThrowsException() {
        when(walletRepository.findByWalletUserId(fundWalletDto.getUserId()))
                .thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            walletService.fundAccount(fundWalletDto);
        });

        assertEquals("Wallet not found", exception.getMessage());
        verify(walletRepository, times(1)).findByWalletUserId(fundWalletDto.getUserId());
    }

    @Test
    void getWalletBalance_Successful() {
        when(walletRepository.findByWalletUserId(TEST_USER1_ID))
                .thenReturn(Optional.of(wallet));

        AppResponse<String> response = walletService.getWalletBalance(TEST_USER1_ID);

        // Assert
        assertNotNull(response);
        assertEquals("wallet balance", response.getMessage());
        assertEquals("1000", response.getData());

        // Verify repository interaction
        verify(walletRepository, times(1)).findByWalletUserId(TEST_USER1_ID);
    }

    @Test
    void getWalletBalance_WalletNotFound_ThrowsException() {
        // Arrange
        String userId = "nonexistent-user";

        when(walletRepository.findByWalletUserId(userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            walletService.getWalletBalance(userId);
        });

        assertEquals("User Id for wallet Not found", exception.getMessage());
        verify(walletRepository, times(1)).findByWalletUserId(userId);
    }


    @Test
    void transferFunds_Successful() {
        // Arrange
        when(walletRepository.findByWalletUserId(transferFundsDto.getSenderUserId()))
                .thenReturn(Optional.of(wallet));
        when(walletRepository.findByWalletUserId(transferFundsDto.getReceiverUserId()))
                .thenReturn(Optional.of(receiverWallet));

        // Mock the failed transaction persistence
        doNothing().when(persistenceService).saveFailedTransaction(any(Transaction.class));

        // Mock transaction saves
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            if (t.getStatus() == TransactionStatusEnum.FAILED) {
                t.setId("failed-trans-123");
            } else {
                t.setId("success-trans-456");
            }
            return t;
        });

        // Mock wallet saves
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> {
            Wallet w = invocation.getArgument(0);
            return w; // Return the wallet with updated balance
        });

        doNothing().when(persistenceService).saveFailedTransaction(any(Transaction.class));

        AppResponse<Transaction> response = walletService.transferFunds(transferFundsDto);

        // Assert
        assertNotNull(response);
        assertEquals("funds transfer", response.getMessage());
        assertEquals(TransactionStatusEnum.SUCCESSFUL, response.getData().getStatus());

        // Verify balances were updated
        assertEquals(BigDecimal.valueOf(999.98), wallet.getBalance()); // 1000 - 500
        assertEquals(BigDecimal.valueOf(700), receiverWallet.getBalance()); // 200 + 500

        // Verify interactions
        verify(walletRepository, times(2)).save(any(Wallet.class)); // Both wallets updated
        verify(transactionRepository, times(2)).save(any(Transaction.class)); // Both transactions saved
        verify(persistenceService, times(1)).saveFailedTransaction(any(Transaction.class));
    }

    @Test
    void transferFunds_SenderWalletNotFound_ThrowsException() {
        // Arrange
        when(walletRepository.findByWalletUserId(transferFundsDto.getSenderUserId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            walletService.transferFunds(transferFundsDto);
        });

        assertEquals("sender wallet not found", exception.getMessage());
    }

    @Test
    void transferFunds_ReceiverWalletNotFound_ThrowsException() {
        // Arrange
        when(walletRepository.findByWalletUserId(transferFundsDto.getSenderUserId()))
                .thenReturn(Optional.of(wallet));
        when(walletRepository.findByWalletUserId(transferFundsDto.getReceiverUserId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            walletService.transferFunds(transferFundsDto);
        });

        assertEquals("receiver wallet not found", exception.getMessage());
    }

}
