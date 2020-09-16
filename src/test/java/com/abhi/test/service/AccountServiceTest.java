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
import org.springframework.test.context.ActiveProfiles;

import com.abhi.account.dto.AccountDto;
import com.abhi.account.model.Account;
import com.abhi.account.repo.AccountRepository;
import com.abhi.account.service.AccountServiceImpl;
import com.abhi.account.util.AccountDetailsNotFoundException;
import com.abhi.account.util.CustomBeanUtility;
import com.abhi.account.util.InvalidInputException;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AccountServiceTest {

	@Mock
	private AccountRepository accountRepo;

	@InjectMocks
	private AccountServiceImpl accountService;

	@Test
	void shouldCreateAccount() throws JsonProcessingException, InvalidInputException {
		AccountDto accountDto = new AccountDto();
		accountDto.setCustomerId(1L);
		accountDto.setAccountBalance(BigDecimal.valueOf(1000L));
		accountDto.setAccountType("current");
		accountDto.setBranchCode(12L);
		accountDto.setIfscCode("BANK123");

		Account account = CustomBeanUtility.convertToDomain(accountDto);
		account.setAccountNo(123L);
		when(accountRepo.save(ArgumentMatchers.any())).thenReturn(account);

		accountService.createAccount(accountDto);
		assertThat(account).isNotNull();
		assertThat(account.getAccountNo()).isEqualTo(123L);
	}

	@Test
	void shoulReturn400WhenCreateAccountAsNull() {
		assertThrows(InvalidInputException.class, () -> {
			accountService.createAccount(null);
		});
	}

	@Test
	void shouldReturnAccountDetails() throws AccountDetailsNotFoundException {
		Account account = new Account();
		account.setCustomerId(1L);
		account.setAccountBalance(BigDecimal.valueOf(1000L));
		account.setAccountType("current");
		account.setBranchCode(12L);
		account.setIfscCode("BANK123");
		account.setAccountNo(123L);

		when(accountRepo.findById(ArgumentMatchers.any())).thenReturn(Optional.of(account));

		AccountDto accountDto = accountService.getAccountDetails(123L);
		assertThat(accountDto).isNotNull();
		assertThat(accountDto.getAccountNo()).isEqualTo(123L);

	}

	@Test
	void shoulReturn400IfAccountDoesnotExist() {
		when(accountRepo.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
		assertThrows(AccountDetailsNotFoundException.class, () -> {
			accountService.getAccountDetails(111L);
		});
	}
}
