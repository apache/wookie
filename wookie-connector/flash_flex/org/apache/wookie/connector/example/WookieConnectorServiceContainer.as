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

package org.apache.wookie.connector.example
{
	import mx.containers.VBox;
	
	import org.apache.wookie.connector.framework.User;
	import org.apache.wookie.connector.framework.WookieServerConnection;
	
	/**
	 * Base class of user interfaces for Wookie service(s). 
	 */
	public class WookieConnectorServiceContainer extends VBox {
		private var _users:Array;
		private var _current_user:User;
		private var _connection:WookieServerConnection;
		
		public function WookieConnectorServiceContainer(connection:WookieServerConnection=null) {
			super();
			this._users = [];
			this._connection = connection;
		}
		
		public function get users ():Array {
			return this._users;
		}
		
		public function set users (new_users:Array):void {
			this._users = new_users;
		}
		
		public function get current_user ():User {
			return this._current_user;
		}
		
		public function set current_user (new_current_user:User):void {
			this._current_user = new_current_user;
		}
		
		public function get connection ():WookieServerConnection {
			return this._connection;
		}
		
		public function set connection (new_connection:WookieServerConnection):void {
			this._connection = new_connection;
		}
	}
}
