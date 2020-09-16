package com.abhi.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abhi.account.dto.AccountDto;
import com.abhi.account.model.Account;
import com.abhi.account.repo.AccountRepository;
import com.abhi.account.util.AccountDetailsNotFoundException;
import com.abhi.account.util.CustomBeanUtility;
import com.abhi.account.util.InvalidInputException;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepo;

	@Override
	public AccountDto createAccount(AccountDto accountDto) throws InvalidInputException {
		if (null == accountDto) {
			throw new InvalidInputException("Account Details are Required");
		}
		Account account = CustomBeanUtility.convertToDomain(accountDto);
		if (null == account.getAccountBalance()) {
			account.setAccountBalance(BigDecimal.ZERO);
		}
		account.setCreatedDate(LocalDateTime.now());
		account = accountRepo.save(account);
		return CustomBeanUtility.convertToDto(account);
	}

	@Override
	public AccountDto getAccountDetails(Long accountNo) throws AccountDetailsNotFoundException {
		Optional<Account> accountOp = accountRepo.findById(accountNo);
		if (accountOp.isPresent()) {
			return CustomBeanUtility.convertToDto(accountOp.get());
		} else {
			throw new AccountDetailsNotFoundException();
		}
	}

}
