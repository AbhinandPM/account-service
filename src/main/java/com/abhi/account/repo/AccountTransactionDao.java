package com.abhi.account.repo;

import com.abhi.account.model.Transaction;

public interface AccountTransactionDao {

	Transaction performTransaction(Transaction transaction);
	
}
