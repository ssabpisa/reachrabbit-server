package com.ahancer.rr.custom.type;

public enum ProposalStatus {
	WaitForReview("Wait for Review"),
	NeedRevision("Need Revision"),
	Reject("Reject"),
	Approve("Approve");
	
	private String displayName;

	ProposalStatus(String displayName) {
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
