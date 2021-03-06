package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ahancer.rr.custom.type.TransactionStatus;
import com.ahancer.rr.custom.type.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name="transaction")
@SequenceGenerator(name="transactionSeq", initialValue=10000000, allocationSize=1)
public class Transaction implements Serializable {
	
	private static final long serialVersionUID = 7564704836479151058L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="transactionSeq")
	private Long transactionId;
	
	@Column(name="transactionNumber",length=6)
	private String transactionNumber;
	
	@Column(name="userId", nullable = false)
	private Long userId;
	
	@JsonIgnore
	@MapsId("userId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;
	
	@Column(name="amount",scale=10,precision=3)
	private Double amount;
	
	@Column(name="status",length=20)
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	
	@Column(name="type",length=20)
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiredAt",updatable=false)
	private Date expiredAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "completedAt")
	private Date completedAt;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL,mappedBy="transaction")
	@PrimaryKeyJoinColumn
	@JsonManagedReference(value="transaction-brand")
	private Set<BrandTransactionDocument> brandTransactionDocument = new HashSet<BrandTransactionDocument>(0);
	
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL,mappedBy="transaction")
	@PrimaryKeyJoinColumn
	@JsonManagedReference(value="transaction-influencer")
	private Set<InfluencerTransactionDocument> influencerTransactionDocument = new HashSet<InfluencerTransactionDocument>(0);
	
	
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL,mappedBy="transaction")
	@PrimaryKeyJoinColumn
	@JsonManagedReference(value="transaction-referral")
	private Set<ReferralTransactionDocument> referralTransactionDocument = new HashSet<ReferralTransactionDocument>(0);
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="slipId")
	private Resource slip;
	
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt",updatable=false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedAt")
	private Date updatedAt;
	
	public Transaction(){
		
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public Set<BrandTransactionDocument> getBrandTransactionDocument() {
		return brandTransactionDocument;
	}

	public void setBrandTransactionDocument(Set<BrandTransactionDocument> brandTransactionDocument) {
		this.brandTransactionDocument = brandTransactionDocument;
	}

	public Set<InfluencerTransactionDocument> getInfluencerTransactionDocument() {
		return influencerTransactionDocument;
	}

	public void setInfluencerTransactionDocument(Set<InfluencerTransactionDocument> influencerTransactionDocument) {
		this.influencerTransactionDocument = influencerTransactionDocument;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public Date getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	public Resource getSlip() {
		return slip;
	}
	
	public Set<ReferralTransactionDocument> getReferralTransactionDocument() {
		return referralTransactionDocument;
	}

	public void setReferralTransactionDocument(Set<ReferralTransactionDocument> referralTransactionDocument) {
		this.referralTransactionDocument = referralTransactionDocument;
	}

	public void setSlip(Resource slip) {
		this.slip = slip;
	}

	@PrePersist
	protected void onCreate() {
		updatedAt = createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Date();
	}
	
	
}
