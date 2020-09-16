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

import com.abhi.account.controller.TransactionController;
import com.abhi.account.dto.TransactionDto;
import com.abhi.account.service.TransactionService;
import com.abhi.account.util.AccountDetailsNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TransactionController.class)
@ActiveProfiles("test")
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionService transactionService;
	
	private static ObjectMapper mapper = new ObjectMapper();

	@Test
	void testDebitTransaction() throws Exception {

		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setAccountNo(123L);
		transactionDto.setAmount(BigDecimal.valueOf(100L));
		transactionDto.setTransactionType("debit");
		
		String json = mapper.writeValueAsString(transactionDto);
		
		transactionDto.setStatus(1);

		when(transactionService.handleTransaction(ArgumentMatchers.any())).thenReturn(transactionDto);

		mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accountNo", Matchers.equalTo(123)))
				.andExpect(jsonPath("$.status", Matchers.equalTo(1)))
				.andReturn();
	}
	
	@Test
	void testCreditTransaction() throws Exception {

		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setAccountNo(123L);
		transactionDto.setAmount(BigDecimal.valueOf(100L));
		transactionDto.setTransactionType("credit");
		
		String json = mapper.writeValueAsString(transactionDto);
		
		transactionDto.setStatus(1);

		when(transactionService.handleTransaction(ArgumentMatchers.any())).thenReturn(transactionDto);

		mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accountNo", Matchers.equalTo(123)))
				.andExpect(jsonPath("$.status", Matchers.equalTo(1)))
				.andReturn();
	}
	
	@Test
	void testCreditTransactionWithIncorrectAccountNo() throws Exception {

		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setAccountNo(123L);
		transactionDto.setAmount(BigDecimal.valueOf(100L));
		transactionDto.setTransactionType("credit");
		
		String json = mapper.writeValueAsString(transactionDto);
		
		when(transactionService.handleTransaction(ArgumentMatchers.any())).thenThrow(AccountDetailsNotFoundException.class);

		mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

}
