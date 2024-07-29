package com.demo.factory.exception;

public class ModelFileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ModelFileException(){
		super();
	}

	public ModelFileException(String message){
		super(message);
	}
	
}
