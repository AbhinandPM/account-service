package com.abhi.account.util;

import org.springframework.beans.BeanUtils;

import com.abhi.account.dto.AccountDto;
import com.abhi.account.dto.TransactionDto;
import com.abhi.account.model.Account;
import com.abhi.account.model.Transaction;

public class CustomBeanUtility {

	private CustomBeanUtility() {
	}

	public static Account convertToDomain(AccountDto accountDto) {
		if (null == accountDto) {
			return null;
		}
		Account account = new Account();
		BeanUtils.copyProperties(accountDto, account);
		return account;
	}

	public static AccountDto convertToDto(Account account) {
		if (null == account) {
			return null;
		}
		AccountDto accountDto = new AccountDto();
		BeanUtils.copyProperties(account, accountDto);
		return accountDto;
	}

	public static Transaction convertToDomain(TransactionDto transactionDto) {
		if (null == transactionDto) {
			return null;
		}
		Transaction transaction = new Transaction();
		BeanUtils.copyProperties(transactionDto, transaction);
		return transaction;
	}

	public static TransactionDto convertToDto(Transaction transaction) {
		if (null == transaction) {
			return null;
		}
		TransactionDto transactionDto = new TransactionDto();
		BeanUtils.copyProperties(transaction, transactionDto);
		return transactionDto;
	}

}
