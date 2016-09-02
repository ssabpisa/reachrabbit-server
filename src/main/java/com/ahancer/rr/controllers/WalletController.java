package com.ahancer.rr.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.models.Wallet;
import com.ahancer.rr.request.PayoutRequest;
import com.ahancer.rr.services.TransactionService;
import com.ahancer.rr.services.WalletService;

@RestController
@RequestMapping("/wallets")
public class WalletController extends AbstractController {
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(method=RequestMethod.GET)
	@Authorization(Role.Influencer)
	public Wallet getPendingWallet() throws Exception {
		return walletService.findPendingByIndluencer(this.getUserRequest().getInfluencer().getInfluencerId());
	}
	
	@RequestMapping(value="/payout",method=RequestMethod.POST)
	@Authorization(Role.Influencer)
	public Transaction payoutWallet(@Valid @RequestBody PayoutRequest request) throws Exception {
		return walletService.payoutWallet(request,this.getUserRequest().getInfluencer().getInfluencerId());
	}
	
	@RequestMapping(value="/{walletId}/transaction",method=RequestMethod.GET)
	@Authorization({ Role.Brand, Role.Admin })
	public Transaction getTransactionFromWallet(@PathVariable Long walletId) throws Exception {
		if(Role.Influencer.equals(this.getUserRequest().getRole())){
			return transactionService.findOneTransactionFromWalletByInfluencer(walletId,this.getUserRequest().getInfluencer().getInfluencerId());
		}else if(Role.Admin.equals(this.getUserRequest().getRole())){
			return transactionService.findOneTransactionFromWalletByAdmin(walletId);
		}
		throw new ResponseException(HttpStatus.UNAUTHORIZED,"error.unauthorize");
	}

}
