/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.wookie.exceptions;

/**
 * Invalid call to a widget 
 * @author Paul Sharples
 * @version $Id: InvalidWidgetCallException.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
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
	}

	/**
	 * @param message
	 */
	public InvalidWidgetCallException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidWidgetCallException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidWidgetCallException(String message, Throwable cause) {
		super(message, cause);
	}

}
