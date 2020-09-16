package com.abhi.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.abhi.account.controller.AccountController;
import com.abhi.account.dto.AccountDto;
import com.abhi.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
@ActiveProfiles("test")
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	private static ObjectMapper mapper = new ObjectMapper();

	@Test
	void testSearchAccount() throws Exception {

		RequestBuilder request = MockMvcRequestBuilders.get("/account/123").accept(MediaType.APPLICATION_JSON);

		AccountDto account = new AccountDto();
		account.setAccountNo(123L);
		account.setCustomerId(1L);
		account.setAccountBalance(BigDecimal.valueOf(1000L));
		account.setAccountType("current");
		account.setBranchCode(12L);
		account.setIfscCode("BANK123");

		when(accountService.getAccountDetails(ArgumentMatchers.any())).thenReturn(account);

		mockMvc.perform(request).andExpect(status().isOk())
				//.andExpect(jsonPath("$.accountNo", Matchers.equalTo(account.getAccountNo())))
				.andExpect(jsonPath("$.customerId", Matchers.equalTo(1))).andReturn();

	}

	@Test
	void testCreateAccount() throws Exception {

		AccountDto account = new AccountDto();
		account.setAccountNo(123L);
		account.setCustomerId(1L);
		account.setAccountBalance(BigDecimal.valueOf(1000L));
		account.setAccountType("current");
		account.setBranchCode(12L);
		account.setIfscCode("BANK123");
		String json = mapper.writeValueAsString(account);

		when(accountService.createAccount(ArgumentMatchers.any())).thenReturn(account);

		mockMvc.perform(post("/account").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accountNo", Matchers.equalTo(123)))
				.andExpect(jsonPath("$.customerId", Matchers.equalTo(1))).andReturn();
	}

}
