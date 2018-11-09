package com.minute.application.app.config;

public class AuthException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public AuthException(String msg) {
		super(msg);
	}

}
