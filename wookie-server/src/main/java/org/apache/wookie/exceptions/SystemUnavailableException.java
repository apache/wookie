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
 * System unavailable 
 * @author Paul Sharples
 * @version $Id: SystemUnavailableException.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
 */
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
