package com.abhi.account.service;

import com.abhi.account.dto.AccountDto;
import com.abhi.account.util.AccountDetailsNotFoundException;
import com.abhi.account.util.InvalidInputException;

public interface AccountService {

	AccountDto createAccount(AccountDto accountDto) throws InvalidInputException ;

	AccountDto getAccountDetails(Long accountNo) throws AccountDetailsNotFoundException;
}
