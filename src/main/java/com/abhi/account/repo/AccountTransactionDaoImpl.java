package com.abhi.account.repo;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.abhi.account.model.Account;
import com.abhi.account.model.Transaction;

@Repository
public class AccountTransactionDaoImpl implements AccountTransactionDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Transaction performTransaction(Transaction transaction) {
		
		Account account = entityManager.find(Account.class, transaction.getAccountId(), LockModeType.PESSIMISTIC_WRITE);
		
		if ("credit".equalsIgnoreCase(transaction.getTransactionType())) {
			BigDecimal balance = account.getAccountBalance();
			balance = balance.add(transaction.getAmount());
			transaction.setStatus(1);
			account.setAccountBalance(balance);
		} else {
			BigDecimal balance = account.getAccountBalance();
			balance = balance.subtract(transaction.getAmount());
			if (balance.compareTo(BigDecimal.ZERO) < 0) {
				transaction.setFailureReason("Insufficient Balance");
				transaction.setStatus(2);
			} else {
				transaction.setStatus(1);
				account.setAccountBalance(balance);
			}

		}
		entityManager.merge(account);
		entityManager.flush();
		return transaction;
	}

}
