package com.ahancer.rr.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.User;
import com.ahancer.rr.request.AuthenticationRequest;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.utils.JwtUtil;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private JwtUtil jwt;
	
	@RequestMapping(value = "/login" ,method = RequestMethod.POST)
	public ResponseEntity<?> brandAuthenticationRequest(@Valid @RequestBody AuthenticationRequest authenticationRequest, Device device) 
			throws Exception {
		
		User user = authenticationService.brandAuthentication(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		if(null == user) {
			throw new ResponseException("error.unauthorize",HttpStatus.UNAUTHORIZED);
		}else {
			String token = jwt.generateToken(user.getUserId());
			AuthenticationResponse response = new AuthenticationResponse(token);
			return ResponseEntity.ok(response);
		}
	}
	
	@RequestMapping(value = "/facebook" ,method = RequestMethod.POST)
	public ResponseEntity<?> facebookAuthenticationRequest(Device device) {
		return ResponseEntity.ok("");
	}
	
	@RequestMapping(value = "/instagram" ,method = RequestMethod.POST)
	public ResponseEntity<?> instagramAuthenticationRequest(Device device) {
		return ResponseEntity.ok("");
	}
	
	@RequestMapping(value = "/youtube" ,method = RequestMethod.POST)
	public ResponseEntity<?> youtubemAuthenticationRequest(Device device) {
		return ResponseEntity.ok("");
	}


}
