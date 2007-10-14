/**
 * 
 */
package org.tencompetence.widgetservice.exceptions;

/**
 * @author paul
 *
 */
public class InvalidWidgetCallException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2582972720134703644L;

	/**
	 * 
	 */
	public InvalidWidgetCallException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public InvalidWidgetCallException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public InvalidWidgetCallException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidWidgetCallException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
