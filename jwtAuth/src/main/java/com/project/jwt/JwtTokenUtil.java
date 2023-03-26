package com.project.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil {
	private static final long EXPRIRE_DURATION = 24 * 60 * 60 * 1000; //24 HOURS
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	
	@Value("${app.jwt.secret}")
	private String SECRET_KEY;
	
	public String generateAccessToken(User user)
	{
		return Jwts.builder()
				.setSubject(String.format("%s %s", user.getId(),user.getEmail()))
				.setIssuer("JWT APP")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPRIRE_DURATION))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				.compact();
	}
	
	public boolean validateAccessToken(String token)
	{
		try
		{
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			return true;
		}
		catch(ExpiredJwtException e)
		{
			LOGGER.error("JWT expired",e.getMessage());
		}
		catch(IllegalArgumentException e)
		{
			LOGGER.error("Token is null or empty",e.getMessage());
		}
		catch(MalformedJwtException e)
		{
			LOGGER.error("JWT is invalid",e.getMessage());
		}
		catch(UnsupportedJwtException e)
		{
			LOGGER.error("JWT is not supported",e.getMessage());
		}
		catch(SignatureException e)
		{
			LOGGER.error("Signature validation failed",e.getMessage());
		}
		return false;
	}
	
	public String getSubject(String token)
	{
		return parseClaims(token).getSubject();
	}
	
	public Claims parseClaims(String token)
	{
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}
	
	
}
