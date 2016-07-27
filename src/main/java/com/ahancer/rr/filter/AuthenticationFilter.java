package com.ahancer.rr.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ahancer.rr.models.User;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.utils.JwtUtil;

@Component
@Order(2)
public class AuthenticationFilter implements Filter {

	@Value("${reachrabbit.token.header}")
	private String tokenHeader;
	
	@Value("${reachrabbit.attribute.user}")
	private String userAttribute;

	@Autowired
	private JwtUtil jwt;

	@Autowired
	private AuthenticationService authenticationService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest)req;
		String path = request.getRequestURI();
		if (path.startsWith("/auth/") || path.startsWith("/doc") || path.startsWith("/swagger-resources") || path.startsWith("/configuration") || path.startsWith("/v2")) {
			chain.doFilter(req, res);
		} else {
			try {
				String token = request.getHeader(tokenHeader);
				Long userId = jwt.getUserId(token);
				User user = authenticationService.getUserById(userId);
				if(null != user) {
					request.setAttribute(userAttribute, user);
					chain.doFilter(req, res);
				} else {
					response.sendError(401);
				}
			}catch (Exception e) {
				response.sendError(401);
			}
		}
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {

	}
	
	@Override
	public void destroy() {

	}

}
