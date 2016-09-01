package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.ahancer.rr.custom.type.DocumentType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name="influencerTransactionDocument")
public class InfluencerTransactionDocument extends AbstractModel implements Serializable {

	private static final long serialVersionUID = -8423456738305930567L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long documentId;
	
	@Column(name="transactionId", nullable = false)
	private Long transactionId;

	@MapsId("transactionId")
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "transactionId")
	@JsonManagedReference(value="transaction-influencer")
	private Transaction transaction;
	
	@Column(name="walletId",nullable=false)
	private Long walletId;

	@MapsId("walletId")
	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.MERGE)
	@JoinColumn(name="walletId")
	private Wallet wallet;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="slipId")
	private Resource slip;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="bankId")
	private Bank bank;
	
	@Column(name="accountName",length=255)
	private String accountName;
	
	@Column(name="accountNumber",length=255)
	private String accountNumber;
	
	@Column(name="amount",scale=10,precision=3)
	private Double amount;
	
	@Column(name="type",length=20)
	@Enumerated(EnumType.STRING)
	private DocumentType type;
	
	public InfluencerTransactionDocument(){
		
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public Resource getSlip() {
		return slip;
	}

	public void setSlip(Resource slip) {
		this.slip = slip;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}
}
