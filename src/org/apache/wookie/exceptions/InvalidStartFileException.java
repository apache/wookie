/**
 * 
 */
package org.apache.wookie.exceptions;

/**
 * Thrown when a widget has no valid start file
 * @author scott
 *
 */
public class InvalidStartFileException extends BadManifestException {

	private static final long serialVersionUID = 1L;

	public InvalidStartFileException() {
		super();
	}

	public InvalidStartFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidStartFileException(String message) {
		super(message);
	}

	public InvalidStartFileException(Throwable cause) {
		super(cause);
	}

}
