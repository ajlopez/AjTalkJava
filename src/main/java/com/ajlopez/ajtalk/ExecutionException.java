package com.ajlopez.ajtalk;

public class ExecutionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6658421435084758830L;

	public ExecutionException(String message) {
		super(message);
	}

	public ExecutionException(Exception ex) {
		super(ex);
	}
}
