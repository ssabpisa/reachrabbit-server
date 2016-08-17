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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ahancer.rr.custom.type.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name="influencer")
public class Influencer extends AbstractModel implements Serializable{

	private static final long serialVersionUID = -2725911649000100273L;
	
	@Id
	@Column(name="influencerId",unique = true, nullable = false)
	private Long influencerId;

	@MapsId("influencerId")
	@OneToOne
    @JoinColumn(name = "influencerId")
	@JsonBackReference
	private User user;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name="web",length=255)
	private String web;

	@Lob
	@Column(name="about")
	private String about;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="birthday")
	private Date birthday;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "influencer",cascade=CascadeType.ALL)
	private Set<InfluencerMedia> influencerMedias = new HashSet<InfluencerMedia>(0);
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinTable(
			name="InfluencerCategory",
			joinColumns=@JoinColumn(name="influencerId", referencedColumnName="influencerId"),
			inverseJoinColumns=@JoinColumn(name="categoryId", referencedColumnName="categoryId"))
	private Set<Category> categories = new HashSet<Category>(0);

	public Influencer() {

	}

	public Long getInfluencerId() {
		return influencerId;
	}

	public void setInfluencerId(Long influencerId) {
		this.influencerId = influencerId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<InfluencerMedia> getInfluencerMedias() {
		return influencerMedias;
	}

	public void setInfluencerMedias(Set<InfluencerMedia> influencerMedias) {
		this.influencerMedias = influencerMedias;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

}
