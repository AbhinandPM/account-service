package com.abhi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.abhi.account.controller.AccountController;
import com.abhi.account.controller.TransactionController;
import com.abhi.account.model.Transaction;
import com.abhi.account.repo.AccountTransactionDao;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceApplicationTests {
	
	@Autowired
	private AccountController accountController;
	
	@Autowired
	private TransactionController transactionController;
	
	@Autowired
	private AccountTransactionDao accountTransactionDao;

	@Test
	void contextLoads() {
		assertThat(accountController).isNotNull();
		assertThat(transactionController).isNotNull();
	}
	
	@Test
	@Sql({"/import_account.sql"})
	@Transactional
	void testTransactionDao() {
		Transaction transaction = new Transaction();
		transaction.setAccountNo(1L);
		transaction.setAmount(BigDecimal.valueOf(100L));
		transaction.setTransactionType("credit");
		transaction = accountTransactionDao.performTransaction(transaction );
		
		assertEquals(1,transaction.getStatus());
	}
	
	@Test
	@Sql({"/import_account.sql"})
	@Transactional
	void testTransactionDaoInsufficientBalance() {
		Transaction transaction = new Transaction();
		transaction.setAccountNo(1L);
		transaction.setAmount(BigDecimal.valueOf(100000L));
		transaction.setTransactionType("debit");
		transaction = accountTransactionDao.performTransaction(transaction );
		
		assertEquals(2,transaction.getStatus());
	}

}
