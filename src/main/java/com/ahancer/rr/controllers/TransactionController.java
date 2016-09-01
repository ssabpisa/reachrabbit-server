package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController extends AbstractController {
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(method=RequestMethod.GET)
	@Authorization({Role.Admin, Role.Influencer, Role.Brand})
	public Page<Transaction> getAllTransaction(PageRequest pageable) {
		return transactionService.findAllTransactions(pageable);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@Authorization(Role.Brand)
	public Transaction createTransaction() throws Exception {
		Transaction transaction = transactionService.createTransactionByBrand(this.getUserRequest().getBrand().getBrandId());
		return transaction;
	}
	
	@RequestMapping(value="/{transactionId}",method=RequestMethod.GET)
	@Authorization(Role.Brand)
	public Transaction getTransaction(@PathVariable Long transactionId) throws Exception {
		Transaction transaction = transactionService.findOneTransaction(transactionId,this.getUserRequest().getBrand().getBrandId());
		return transaction;
	}
	
	
	@RequestMapping(value="/{transactionId}/confirm",method=RequestMethod.PUT)
	@Authorization(Role.Admin)
	public Transaction confirmTransaction(@PathVariable Long transactionId) throws Exception {
		Transaction transaction = transactionService.confirmTransaction(transactionId);
		return transaction;
	}
	
}
