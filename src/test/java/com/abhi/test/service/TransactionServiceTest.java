package com.abhi.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.abhi.account.dto.TransactionDto;
import com.abhi.account.model.Account;
import com.abhi.account.repo.AccountRepository;
import com.abhi.account.repo.AccountTransactionDao;
import com.abhi.account.repo.TransactionRepository;
import com.abhi.account.service.TransactionServiceImpl;
import com.abhi.account.util.AccountDetailsNotFoundException;
import com.abhi.account.util.CustomBeanUtility;
import com.abhi.account.util.InvalidInputException;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TransactionServiceTest {

	@Mock
	private AccountRepository accountRepo;

	@Mock
	private TransactionRepository transactionRepo;

	@Mock
	private AccountTransactionDao accountTransactionDao;

	@Mock
	private JmsTemplate jmsTemplate;

	@InjectMocks
	private TransactionServiceImpl transactionService;

	@Test
	void shouldPerformDebitTransaction()
			throws JsonProcessingException, InvalidInputException, AccountDetailsNotFoundException {

		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setAccountNo(123L);
		transactionDto.setAmount(BigDecimal.valueOf(100L));
		transactionDto.setTransactionType("debit");

		Account account = new Account();
		account.setAccountNo(123L);
		account.setCustomerId(1L);
		account.setAccountBalance(BigDecimal.valueOf(1000L));
		account.setAccountType("current");
		account.setBranchCode(12L);
		account.setIfscCode("BANK123");

		when(accountRepo.findById(ArgumentMatchers.any())).thenReturn(Optional.of(account));

		when(transactionRepo.save(ArgumentMatchers.any()))
				.thenReturn(CustomBeanUtility.convertToDomain(transactionDto));

		TransactionDto transaction = transactionService.handleTransaction(transactionDto);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAccountNo()).isEqualTo(123L);
	}

	@Test
	void shoulReturn400WhenPerformTransactionWithNullValue() {
		assertThrows(InvalidInputException.class, () -> {
			transactionService.handleTransaction(null);
		});
	}

	@Test
	void shoulReturn400IfAccountDoesnotExist() {
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setAccountNo(123L);
		transactionDto.setAmount(BigDecimal.valueOf(100L));
		transactionDto.setTransactionType("debit");
		when(accountRepo.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
		assertThrows(AccountDetailsNotFoundException.class, () -> {
			transactionService.handleTransaction(transactionDto);
		});
	}
}
