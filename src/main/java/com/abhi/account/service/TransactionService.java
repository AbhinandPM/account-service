package com.abhi.account.service;

import com.abhi.account.dto.TransactionDto;
import com.abhi.account.util.AccountDetailsNotFoundException;
import com.abhi.account.util.InvalidInputException;

public interface TransactionService {

	TransactionDto handleTransaction(TransactionDto transactionDto) throws InvalidInputException, AccountDetailsNotFoundException;

}
