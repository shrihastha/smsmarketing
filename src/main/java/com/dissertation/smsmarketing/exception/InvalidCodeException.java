package com.dissertation.smsmarketing.exception;

public class InvalidCodeException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public InvalidCodeException(String exceptionMessage){
		super(exceptionMessage);
	}
}