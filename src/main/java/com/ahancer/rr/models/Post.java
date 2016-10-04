package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="post")
public class Post extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = -5556110403033071026L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long postId;
	
	@Column(name="socialPostId",length=255,nullable=false)
	private String socialPostId;
	
	@Column(name="proposalId",nullable=false)
	private Long proposalId;
	
	@JsonIgnore
	@MapsId("proposalId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="proposalId",nullable=false)
	private Proposal proposal;
	
	@Column(name="mediaId",nullable=false)
	private String mediaId;
	
	@JsonIgnore
	@MapsId("mediaId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="mediaId",nullable=false)
	private Media media;
	
	@Column(name="likeCount")
	private Long likeCount;
	
	@Column(name="shareCount")
	private Long shareCount;
	
	@Column(name="commentCount")
	private Long commentCount;
	
	@Column(name="viewCount")
	private Long viewCount;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getSocialPostId() {
		return socialPostId;
	}

	public void setSocialPostId(String socialPostId) {
		this.socialPostId = socialPostId;
	}

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public Long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}

	public Long getShareCount() {
		return shareCount;
	}

	public void setShareCount(Long shareCount) {
		this.shareCount = shareCount;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}
	
	
	
}