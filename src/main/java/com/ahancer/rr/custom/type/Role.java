package com.ahancer.rr.custom.type;

public enum Role {
	Admin("Admin"),
	Brand("Brand"),
	Influencer("Influencer"),
	Bot("Bot"),
	Public("Public"),
	Partner("Partner");

	private String displayName;

	Role(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() { 
		return displayName; 
	}

	@Override 
	public String toString() { 
		return displayName; 
	}
}
