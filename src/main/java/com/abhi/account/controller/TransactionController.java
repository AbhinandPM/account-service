package com.abhi.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abhi.account.dto.TransactionDto;
import com.abhi.account.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@PostMapping("/transaction")
	public TransactionDto handleTransaction(@RequestBody TransactionDto transactionDto) throws Exception {
		return transactionService.handleTransaction(transactionDto);
	}
}
