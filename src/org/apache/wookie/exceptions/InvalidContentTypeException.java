/**
 * 
 */
package org.apache.wookie.exceptions;

/**
 * Thrown when a widget manifest sets an invalid content type
 * @author scott
 *
 */
public class InvalidContentTypeException extends BadManifestException {

	private static final long serialVersionUID = 1L;

	public InvalidContentTypeException() {
		super();
	}

	public InvalidContentTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidContentTypeException(String message) {
		super(message);
	}

	public InvalidContentTypeException(Throwable cause) {
		super(cause);
	}

}
