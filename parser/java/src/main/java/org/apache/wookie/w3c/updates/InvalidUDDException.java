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
package org.apache.wookie.w3c.updates;

/**
 * An exception thrown when an Update Description Document is not valid
 */
public class InvalidUDDException extends Exception  {

	private static final long serialVersionUID = -6638297159167656522L;
	
	public InvalidUDDException(String message) {
		super(message);
	}

}
