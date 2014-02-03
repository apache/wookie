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

﻿package org.apache.wookie.connector.framework {

	/**
	 * And exception that represents a problem connecting with or communicating with
	 * a Wookie server or the host environment for plugins.
	 */
	public class WookieConnectorException extends Error {

		/**
		 * Create a new WookieConnectorException object.
		 * 
		 * @param message String associated with the Error object; this parameter is optional.
		 * @param id Reference number to associate with the specific error message. 
		 */
		public function WookieConnectorException(message:* = "", id:* = 0) {
			super(message, id);
		}

	}

}
