package com.ahancer.rr.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CampaignResourceId implements java.io.Serializable {

	private static final long serialVersionUID = 6043666580459930750L;
	
	@Column(name = "campaignId", nullable = false)
	private Long campaignId;
	
	@Column(name = "resourceId", nullable = false)
	private Long resourceId;

	public CampaignResourceId() {
	}

	public CampaignResourceId(long campaignId, long resourceId) {
		this.campaignId = campaignId;
		this.resourceId = resourceId;
	}
	
	public Long getCampaignId() {
		return this.campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	
	public Long getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CampaignResourceId))
			return false;
		CampaignResourceId castOther = (CampaignResourceId) other;

		return (this.getCampaignId().compareTo(castOther.getCampaignId()) == 0)
				&& (this.getResourceId().compareTo(castOther.getResourceId()) == 0);
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) (long) this.getCampaignId();
		result = 37 * result + (int) (long) this.getResourceId();
		return result;
	}

}
