package com.ahancer.rr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ahancer.rr.models.User;

@Component
public abstract class AbstractController {
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("${reachrabbit.attribute.user}")
	private String userAttribute;
	
	final public User getUserRequest() { 
		Object user = request.getAttribute(userAttribute);
		if(user instanceof User){
			return (User)user;
		} else {
			return null;
		}
	}
}
