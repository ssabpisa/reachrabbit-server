package com.ahancer.rr.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity(name="proposalMessage")
public class ProposalMessage extends AbstractModel implements Serializable  {

	private static final long serialVersionUID = 2277636873977709793L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long messageId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="proposalId",nullable=false)
	private Proposal proposal;
	

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId",nullable=false)
	private User user;
	
	@Column(name="message")
	@Lob
	private String message;
	
	@Column(name="isInfluencerRead")
	private Boolean isInfluencerRead;
	
	@Column(name="isBrandRead")
	private Boolean isBrandRead;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="proposalMessageResource",
			joinColumns=@JoinColumn(name="messageId", referencedColumnName="messageId"),
			inverseJoinColumns=@JoinColumn(name="resourceId", referencedColumnName="resourceId"))
	private Set<Resource> resources = new HashSet<Resource>(0);
	
	@Transient
	@JsonSerialize
	@JsonDeserialize
	private Date time;
	
	public ProposalMessage(){
		this.setTime(this.getCreatedAt());
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}
	
	public Boolean getIsInfluencerRead() {
		return isInfluencerRead;
	}

	public void setIsInfluencerRead(Boolean isInfluencerRead) {
		this.isInfluencerRead = isInfluencerRead;
	}

	public Boolean getIsBrandRead() {
		return isBrandRead;
	}

	public void setIsBrandRead(Boolean isBrandRead) {
		this.isBrandRead = isBrandRead;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}
