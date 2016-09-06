package com.ahancer.rr.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.DocumentType;
import com.ahancer.rr.custom.type.TransactionStatus;
import com.ahancer.rr.custom.type.TransactionType;
import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.daos.InfluencerTransactionDocumentDao;
import com.ahancer.rr.daos.TransactionDao;
import com.ahancer.rr.daos.WalletDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.InfluencerTransactionDocument;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.models.Wallet;
import com.ahancer.rr.request.PayoutRequest;
import com.ahancer.rr.utils.EncodeUtil;

@Service
@Transactional(rollbackFor=Exception.class)
public class WalletService {
	
	@Autowired
	private WalletDao walletDao;
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private InfluencerTransactionDocumentDao influencerTransactionDocumentDao;
	
	public Wallet findPendingByIndluencer(Long influencerId) {
		return walletDao.findByInfluencerIdAndStatus(influencerId, WalletStatus.Pending);
	}
	
	public Transaction payoutWallet(PayoutRequest request, Long influencerId) throws Exception{
		
		Wallet wallet = walletDao.findByInfluencerIdAndStatus(influencerId, WalletStatus.Pending);
		if(null == wallet){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.wallet.not.exist");
		}
		if(0 == wallet.getProposals().size()){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.wallet.empty.proposal");
		}
		wallet.setStatus(WalletStatus.Paid);
		wallet = walletDao.save(wallet);
		//setup transaction
		Transaction transaction = new Transaction();
		transaction.setCompletedAt(new Date());
		transaction.setStatus(TransactionStatus.Pending);
		transaction.setType(TransactionType.Payout);
		transaction.setUserId(influencerId);
		transaction = transactionDao.save(transaction);
		double sum = 0.0;
		for(Proposal proposal : wallet.getProposals()){
			InfluencerTransactionDocument baseDocument = new InfluencerTransactionDocument();
			baseDocument.setAccountName(request.getAccountName());
			baseDocument.setAccountNumber(request.getAccountNumber());
			baseDocument.setAmount(proposal.getPrice());
			baseDocument.setBank(request.getBank());
			baseDocument.setTransactionId(transaction.getTransactionId());
			baseDocument.setType(DocumentType.Base);
			baseDocument.setWalletId(wallet.getWalletId());
			baseDocument = influencerTransactionDocumentDao.save(baseDocument);
			//fee document
			InfluencerTransactionDocument feeDocument = new InfluencerTransactionDocument();
			feeDocument.setAccountName(request.getAccountName());
			feeDocument.setAccountNumber(request.getAccountNumber());
			feeDocument.setAmount(-proposal.getFee());
			feeDocument.setBank(request.getBank());
			feeDocument.setTransactionId(transaction.getTransactionId());
			feeDocument.setType(DocumentType.Fee);
			feeDocument.setWalletId(wallet.getWalletId());
			feeDocument = influencerTransactionDocumentDao.save(feeDocument);
			
			sum = sum + baseDocument.getAmount() + feeDocument.getAmount();
			
		}
		//transfer fee
		InfluencerTransactionDocument transferFeeDocument = new InfluencerTransactionDocument();
		transferFeeDocument.setAccountName(request.getAccountName());
		transferFeeDocument.setAccountNumber(request.getAccountNumber());
		transferFeeDocument.setAmount(-30.0);
		transferFeeDocument.setBank(request.getBank());
		transferFeeDocument.setTransactionId(transaction.getTransactionId());
		transferFeeDocument.setType(DocumentType.TransferFee);
		transferFeeDocument.setWalletId(wallet.getWalletId());
		transferFeeDocument = influencerTransactionDocumentDao.save(transferFeeDocument);
		sum += transferFeeDocument.getAmount();
		
		transaction.setAmount(sum);
		transaction.setTransactionNumber(EncodeUtil.encode(transaction.getTransactionId()));
		transaction = transactionDao.save(transaction);
		
		return transaction;
	}

}