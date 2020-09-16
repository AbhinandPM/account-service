package com.abhi.account.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abhi.account.dto.TransactionDto;
import com.abhi.account.model.Account;
import com.abhi.account.model.Transaction;
import com.abhi.account.repo.AccountRepository;
import com.abhi.account.repo.AccountTransactionDao;
import com.abhi.account.repo.TransactionRepository;
import com.abhi.account.util.AccountDetailsNotFoundException;
import com.abhi.account.util.CustomBeanUtility;
import com.abhi.account.util.InvalidInputException;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private AccountTransactionDao accountTransactionDao;

	@Autowired
	private AccountRepository accountRepo;

	@Override
	public TransactionDto handleTransaction(TransactionDto transactionDto)
			throws InvalidInputException, AccountDetailsNotFoundException {

		if (null == transactionDto) {
			throw new InvalidInputException("Transaction details are required.");
		}
		Optional<Account> accountOp = accountRepo.findById(transactionDto.getAccountNo());
		if (accountOp.isPresent()) {
			transactionDto.setTransactionDate(LocalDateTime.now());
			Transaction transaction = CustomBeanUtility.convertToDomain(transactionDto);
			accountTransactionDao.performTransaction(transaction);
			return CustomBeanUtility.convertToDto(transactionRepo.save(transaction));
		} else {
			throw new AccountDetailsNotFoundException();
		}

	}

}
