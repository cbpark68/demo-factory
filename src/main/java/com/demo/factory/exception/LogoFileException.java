package com.demo.factory.exception;

public class LogoFileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LogoFileException(){
		super();
	}

	public LogoFileException(String message){
		super(message);
	}
	
}
