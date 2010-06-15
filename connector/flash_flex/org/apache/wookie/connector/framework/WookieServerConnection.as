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

﻿package org.apache.wookie.connector.framework 
{
	/**
	 * A connection to a Wookie server. This maintains the necessary data for
	 * connecting to the server and provides utility methods for making common calls
	 * via the Wookie REST API.
	 */
	public class WookieServerConnection {
		/**
		 * URL pointing to wookie server or servlet for this connection.
		 */
		private var _url:String;
		/**
		 * API Key of the wookie server or servlet for this connection.
		 */
		private var _api_key:String;
		/**
		 * Shared data key for this connection.
		 */
		private var _shared_data_key:String;

		/**
		 * Create a connection to a Wookie server at a given URL.
		 * Each parameter is mandatory.
		 * 
		 * @param url the URL of the wookie server.
		 * @param apiKey the API key for the server.
		 * @param sharedDataKey the sharedDataKey for the server connection.
		 * @throws WookieConnectorException if there is a problem setting up this connection.
		 */
		public function WookieServerConnection(url:String, api_key:String, shared_data_key:String) {
			setURL(url);
			setApiKey(api_key);
			setSharedDataKey(shared_data_key);
		}

		/**
		 * Get the URL of the wookie server.
		 * 
		 * @return URL of the wookie server.
		 */
		public function getURL():String {
			return this._url;
		}

		/**
		 * Set the URL of the wookie server.
		 * No check on URL validity and URL syntax.
		 * 
		 * @param new_url URL to assign to this connection.
		 */
		public function setURL(new_url:String):void {
			var lastCharIdx:Number = new_url.length - 1;
			// force the new url to end in "/"
			if (0 < lastCharIdx) {
				if ("/" == new_url.charAt(lastCharIdx)) {
					new_url = new_url.slice(0, lastCharIdx);
				}
			}
			this._url = new_url;
		}

		/**
		 * Get the API key for this server.
		 * 
		 * @return API key for this server.
		 */
		public function getApiKey():String {
			return this._api_key;
		}

		/**
		 * Set the API key for this server.
		 * 
		 * @param new_api_key API key to assign to this server.
		 */
		public function setApiKey(new_api_key:String):void {
			this._api_key = new_api_key;
		}

		/**
		 * Get the shared data key for this server.
		 * 
		 * @return Shared data key for this server.
		 */
		public function getSharedDataKey():String {
			return this._shared_data_key;
		}

		/**
		 * Set the shared data key for this server.
		 *
		 * @param new_key Shared data key to assign to this server.
		 */
		public function setSharedDataKey(new_key:String):void {
			this._shared_data_key = new_key;
		}

		/**
		 * Get a string containing values of this connection. 
		 * 
		 * @return String containing values of this connection. 
		 */
		public function toString():String {
			var toStringResult:String = new String("Wookie Server Connection - ");
			toStringResult += "URL: " + this.getURL();
			toStringResult += "API Key: " + this.getApiKey();
			toStringResult += "Shared Data Key: " + this.getSharedDataKey();
			return toStringResult;
		}
	}

}
