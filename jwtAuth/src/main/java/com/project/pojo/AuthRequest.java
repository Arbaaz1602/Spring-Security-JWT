package com.project.pojo;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class AuthRequest {
	
	@NotNull @Email @Length (min = 5,max = 50)
	private String email;
	
	@NotNull @Length (min = 5, max = 10)
	private String password;

	public AuthRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthRequest(@NotNull @Email @Length(min = 5, max = 50) String email,
			@NotNull @Length(min = 5, max = 10) String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
