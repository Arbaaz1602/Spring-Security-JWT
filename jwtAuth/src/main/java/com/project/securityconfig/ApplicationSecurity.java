package com.project.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.filter.JwtTokenFilter;
import com.project.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
public class ApplicationSecurity {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenFilter jwtTokenFilter;
	
	@Bean
	public UserDetailsService userDetailsService()
	{
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// TODO Auto-generated method stub
				return userRepository.findByEmail(username)
						.orElseThrow(()-> new UsernameNotFoundException("User "+username+" not found"));
			}
		};
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
	}
	
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		/*
		 * http.authorizeRequests()
		 * .requestMatchers("/auth/login","/docs/***","/users").permitAll()
		 * .anyRequest().authenticated();
		 */
		
		http.authorizeHttpRequests((auth) -> {
			auth.requestMatchers("/auth/login","/docs/***","/user").permitAll()
			.anyRequest().authenticated();
		});
		
		
		http.exceptionHandling()
		.authenticationEntryPoint((request,response,ex) ->{
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,ex.getMessage());
		});
		
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
