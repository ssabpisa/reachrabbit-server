package com.ahancer.rr.models;
// Generated Aug 11, 2016 3:08:55 PM by Hibernate Tools 5.1.0.Beta1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * InfluencerMediaId generated by hbm2java
 */
@Embeddable
public class InfluencerMediaId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6974949989812188922L;
	private long influencerId;
	private String mediaId;

	public InfluencerMediaId() {
	}

	public InfluencerMediaId(long influencerId, String mediaId) {
		this.influencerId = influencerId;
		this.mediaId = mediaId;
	}

	@Column(name = "influencerId", nullable = false)
	public long getInfluencerId() {
		return this.influencerId;
	}

	public void setInfluencerId(long influencerId) {
		this.influencerId = influencerId;
	}

	@Column(name = "mediaId", nullable = false)
	public String getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof InfluencerMediaId))
			return false;
		InfluencerMediaId castOther = (InfluencerMediaId) other;

		return (this.getInfluencerId() == castOther.getInfluencerId())
				&& ((this.getMediaId() == castOther.getMediaId()) || (this.getMediaId() != null
						&& castOther.getMediaId() != null && this.getMediaId().equals(castOther.getMediaId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getInfluencerId();
		result = 37 * result + (getMediaId() == null ? 0 : this.getMediaId().hashCode());
		return result;
	}

}