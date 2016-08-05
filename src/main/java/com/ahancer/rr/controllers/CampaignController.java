package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.User;
import com.ahancer.rr.services.CampaignService;

@RestController
@RequestMapping("/campaigns")
public class CampaignController extends AbstractController{
	@Autowired
	private CampaignService campaignService;
	
	@RequestMapping(method=RequestMethod.GET)
	public Page<Campaign> getAllCampaign(Pageable pageRequest) throws Exception{
		return campaignService.findAllByBrand(this.getUserRequest().getBrand(), pageRequest);
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.GET)
	public Campaign getOneCampaign(@PathVariable Long campaignId) throws Exception{
		return campaignService.findCampaignById(campaignId);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@Authorization(Role.Brand)
	public Campaign createCampaign(@RequestBody Campaign campaign) throws Exception{
		User user = this.getUserRequest();
		return campaignService.createCampaignByBrand(campaign, user.getBrand());
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.PUT)
	public Campaign updateCampaign(@PathVariable Long campaignId,@RequestBody Campaign campaign) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
	@RequestMapping(value="/{campaignId}",method=RequestMethod.DELETE)
	public void deleteCampaign(@PathVariable Long campaignId) throws Exception{
		throw new ResponseException("error.notimplement");
	}
	
}
