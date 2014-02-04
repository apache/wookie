/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.auth;

/**
 * Exception thrown when an Auth Token supplied by a widget
 * is invalid, for example where it is incorrectly encrypted,
 * where the content is incorrect (e.g. no API key) or if
 * the token has expired.
 *
 */
public class InvalidAuthTokenException extends Exception{

	public static final int EXPIRED = 0;
	public static final int INVALID_CONTENT = 1;
	public static final int INVALID_ENCRYPTION = 1;	
	
	private int reason;
	
	public InvalidAuthTokenException(int reason){
		this.reason = reason;
	}
	
	/**
	 * Get the reason why the token is invalid
	 * @return
	 */
	public int getReason(){
		return reason;
	}
	
	private static final long serialVersionUID = -2998867462005878209L;	
	
}
