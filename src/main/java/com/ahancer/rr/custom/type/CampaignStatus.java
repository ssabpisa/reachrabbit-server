package com.ahancer.rr.custom.type;

public enum CampaignStatus {
	Draft("Draft"),
	Open("Open"),
	Close("Close"),
	//WaitForPayment("Wait for Payment"),
	//Production("Production"),
	Complete("Complete"),
	Cancel("Cancel");
	
	private String displayName;

	CampaignStatus(String displayName) {
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
