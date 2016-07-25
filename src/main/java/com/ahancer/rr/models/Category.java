package com.ahancer.rr.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="category")
public class Category {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long categoryId;
	
	@Column(name="categoryName",length=255,nullable=false)
	private String categoryName;
	
	@Column(name="isActive",nullable=false)
	private Boolean isActive;
	
	public Category() {
		isActive = true;
	}

}