package org.tencompetence.widgetservice.exceptions;

public class SystemUnavailableException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public SystemUnavailableException() {
		super();
	}

	public SystemUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemUnavailableException(String message) {
		super(message);
	}

	public SystemUnavailableException(Throwable cause) {
		super(cause);
	}


}
