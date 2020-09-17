package com.abhi.account.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
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

	private static final Logger LOGGER = LogManager.getLogger(TransactionServiceImpl.class);

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private AccountTransactionDao accountTransactionDao;

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Value("${TRANSACTION_STATUS_QUEUE}")
	private String transactionStatusQueue;

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
			transactionDto = CustomBeanUtility.convertToDto(transactionRepo.save(transaction));
			try {
				jmsTemplate.convertAndSend(transactionStatusQueue, transactionDto);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}

			return transactionDto;
		} else {
			throw new AccountDetailsNotFoundException();
		}

	}

}
