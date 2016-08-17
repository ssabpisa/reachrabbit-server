package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.CampaignProposal;

public interface CampaignProposalDao extends CrudRepository<CampaignProposal, Long> {

	public Page<CampaignProposal> findAll(Pageable pageable);

	@Query("SELECT COUNT(cp) FROM campaignProposal cp WHERE cp.influencer.influencerId=:influencerId AND cp.campaign.campaignId=:campaignId")
	public int countByInfluencerAndCampaign(@Param("influencerId") Long influencerId, @Param("campaignId") Long campaignId);
}
