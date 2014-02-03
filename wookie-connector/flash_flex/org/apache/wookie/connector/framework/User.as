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
	 * A user represents a possible user of a widget. This class provides a standard way
	 * of representing users in plugins for host environments.
	 */
	public class User {
		/**
		 * Login name.
		 */
		private var _login_name:String;
		/**
		 * Full name.
		 */
		private var _screen_name:String;
		/**
		 * URL pointing to a thumbnail.
		 */
		private var _thumbnail_url:String;

		/**
		 * Create a new user.
		 * No check are done on URL validity.
		 * 
		 * @param loginName Login name of user is mandatory.
		 * @param screenName Full name of user is optional.
		 * @param thumbnailUrl URL pointing to a thumbnail of user is optional.
		 */
		public function User(login_name:String, screen_name:String="", thumbnail_url:String="") {
			setLoginName(login_name);
			setScreenName(screen_name);
			setThumbnailUrl(thumbnail_url);
		}

		/**
		 * Get the login name for this user.
		 * 
		 * @return Login name of this user.
		 */
		public function getLoginName():String {
			return _login_name;
		}

		/**
		 * Set the login name for this user. This is the value that is used by the user to register on the
		 * system, it is guaranteed to be unique.
		 * 
		 * @param new_login_name Login name to assign to this user.
		 */
		public function setLoginName(new_login_name:String):void {
			this._login_name = new_login_name;
		}

		/**
		 * Get the screen name for this user. This is the name that is intended to be displayed on
		 * screen. In many cases it will be the same as the login name.
		 * 
		 * @return Full name of this user.
		 */
		public function getScreenName():String {
			return _screen_name;
		}

		/**
		 * Set the screen name for this user. this is the value that should be displayed on screen.
		 * In many cases it will be the same as the login name.
		 * 
		 * @param new_screen_name Full name to assign to this user.
		 */
		public function setScreenName(new_screen_name:String):void {
			this._screen_name = new_screen_name;
		}

		/**
		 * Get the URL for a thumbnail representing this user.
		 * 
		 * @return User thumbnail icon url.
		 */
		public function getThumbnailUrl():String {
			return _thumbnail_url;
		}

		/**
		 * Set the URL for a thumbnail representing this user.
		 * No check are done on URL validity.
		 * 
		 * @param new_thumbnail_url URL pointing to a thumbnail to assign to this user.
		 */
		public function setThumbnailUrl(new_thumbnail_url:String):void {
			this._thumbnail_url = new_thumbnail_url;
		}
	}

}
