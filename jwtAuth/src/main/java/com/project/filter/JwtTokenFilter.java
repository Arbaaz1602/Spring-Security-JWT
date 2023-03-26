package com.project.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.entity.User;
import com.project.jwt.JwtTokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		
	}
	
	private boolean hasAuthorizationBearer(HttpServletRequest request)
	{
		String header = request.getHeader("Authorization");
		
		if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer"))
			return false;
		
		return true;
	}
	
	private String getAccessToken(HttpServletRequest request)
	{
		String header = request.getHeader("Authorization");
		String token = header.split(" ")[1].trim();
		return token;
	}
	
	public void setAuthenticationContext(String token, HttpServletRequest request)
	{
		UserDetails userDetails = getUserDetails(token);
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,null);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	public UserDetails getUserDetails(String token)
	{
		User userDetails = new User();
		
		String[] jwtSubject = jwtTokenUtil.getSubject(token).split(",");
		
		userDetails.setId(Integer.parseInt(jwtSubject[0]));
		userDetails.setEmail(jwtSubject[1]);
		
		return userDetails;
	}
}
