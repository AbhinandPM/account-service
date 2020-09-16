package com.abhi.account.util;

/**
 * @author abhin
 *
 */
public class AccountDetailsNotFoundException extends Exception {

	private static final long serialVersionUID = 6686441769231655213L;

	public AccountDetailsNotFoundException() {
		super("Account Not found");
	}
}
