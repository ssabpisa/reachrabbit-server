package com.ahancer.rr.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.request.AuthenticationRequest;
import com.ahancer.rr.request.OAuthenticationRequest;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.OAuthenticationResponse;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.services.FacebookService;
import com.ahancer.rr.services.InstagramService;
import com.ahancer.rr.services.YoutubeService;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/auth")
public class AuthenticationController extends AbstractController {
	
	//private static final Logger logger = Logger.getLogger(AuthenticationController.class);
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private FacebookService facebookService;
	@Autowired
	private YoutubeService youtubeService;
	@Autowired
	private InstagramService instagramService;
	@Autowired
	private AuthenticationService authenticationService;
	@ApiOperation(value = "Authenthication by brand")
	@RequestMapping(value = "/login" ,method = RequestMethod.POST)
	public AuthenticationResponse brandAuthenticationRequest(@Valid @RequestBody AuthenticationRequest authenticationRequest) 
			throws Exception {
		AuthenticationResponse authen = authenticationService.brandAuthentication(authenticationRequest.getEmail(), authenticationRequest.getPassword(), request.getRemoteAddr());
		return authen;
	}
	@ApiOperation(value = "Authenthication by influencer using facebook")
	@RequestMapping(value = "/facebook" ,method = RequestMethod.POST)
	public OAuthenticationResponse facebookAuthenticationRequest(@Valid @RequestBody OAuthenticationRequest oauthenticationRequest) throws Exception {
		return facebookService.authenticate(
				facebookService.getAccessToken(oauthenticationRequest.getCode()
						, oauthenticationRequest.getRedirectUri()), this.request.getRemoteAddr());
	}
	@ApiOperation(value = "Authenthication by influencer using instagram")
	@RequestMapping(value = "/instagram" ,method = RequestMethod.POST)
	public OAuthenticationResponse instagramAuthenticationRequest(@Valid @RequestBody OAuthenticationRequest oauthenticationRequest) throws Exception {
		return instagramService.authenticate(
				instagramService.getAccessToken(oauthenticationRequest.getCode()
						, oauthenticationRequest.getRedirectUri()), this.request.getRemoteAddr());
	}
	@ApiOperation(value = "Authenthication by influencer using google")
	@RequestMapping(value = "/google" ,method = RequestMethod.POST)
	public OAuthenticationResponse youtubeAuthenticationRequest(@Valid @RequestBody OAuthenticationRequest oauthenticationRequest) throws Exception {
		return youtubeService.authentication(
				youtubeService.getAccessToken(oauthenticationRequest.getCode()
						, oauthenticationRequest.getRedirectUri()), this.request.getRemoteAddr());
	}
	@ApiOperation(value = "Check instagram token")
	@RequestMapping(value = "/instagram/check" ,method = RequestMethod.GET)
	public Boolean instagramCheck() throws Exception {
		return instagramService.checkAdminToken();
	}	
	@ApiOperation(value = "Refresh instagram token")
	@RequestMapping(value = "/instagram/refresh" ,method = RequestMethod.POST)
	public void instagramRefresh(@Valid @RequestBody OAuthenticationRequest oauthenticationRequest) throws Exception {
		instagramService.refreshAdminToken(instagramService.getAccessToken(oauthenticationRequest.getCode(), oauthenticationRequest.getRedirectUri()));
	}
	@ApiOperation(value = "Authenthication by admin")
	@RequestMapping(value = "/admin" ,method = RequestMethod.POST)
	public AuthenticationResponse adminAuthenticationRequest(@Valid @RequestBody AuthenticationRequest authenticationRequest) 
			throws Exception {
		AuthenticationResponse authen = authenticationService.adminAuthentication(authenticationRequest.getEmail(), authenticationRequest.getPassword(),request.getRemoteAddr());
		return authen;
	}
	@ApiOperation(value = "Authenthication by god influencer")
	@RequestMapping(value = "/influencer" ,method = RequestMethod.POST)
	public AuthenticationResponse influencerAuthenticationRequest(@Valid @RequestBody AuthenticationRequest authenticationRequest) 
			throws Exception {
		AuthenticationResponse authen = authenticationService.influencerEmailAuthentication(authenticationRequest.getEmail(), authenticationRequest.getPassword(),request.getRemoteAddr());
		return authen;
	}
}
