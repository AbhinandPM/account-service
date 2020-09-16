package com.abhi.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abhi.account.dto.AccountDto;
import com.abhi.account.service.AccountService;
import com.abhi.account.util.AccountDetailsNotFoundException;
import com.abhi.account.util.InvalidInputException;

@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;

	@PostMapping("/account")
	public AccountDto createAccount(@RequestBody AccountDto accountDto) throws InvalidInputException  {
		return accountService.createAccount(accountDto);
	}

	@GetMapping("/account/{accountNo}")
	public AccountDto getAccountDetailsByAccountNo(@PathVariable Long accountNo) throws AccountDetailsNotFoundException {
		return accountService.getAccountDetails(accountNo);
	}

}
