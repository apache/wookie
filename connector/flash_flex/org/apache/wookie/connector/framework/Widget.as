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
	 * A client side representation of a widget.
	 * 
	 * @refactor this duplicates data stored in the Widget bean on the server side.
	 */
	public class Widget {
		/**
		 * Unique identifier for this widget.
		 */
		private var _identifier:String;
		/**
		 * Title for this widget.
		 */
		private var _title:String;
		/**
		 * Description for this widget.
		 */
		private var _description:String;
		/**
		 * URL pointing to an icon for this widget.
		 */
		private var _icon_url:String;

		/**
		 * Create a new client side representation of a widget.
		 * 
		 * @param identifier Identifier is mandatory.
		 * @param title Title is mandatory.
		 * @param description Description is mandatory.
		 * @param icon_url URL to an icon is optional.
		 */
		public function Widget(identifier:String, title:String, description:String, icon_url:String="") {
			this._identifier = identifier;
			this._title = title;
			this._description = description;
			this._icon_url = icon_url;
		}

		/**
		 * Get a unique identifier for this widget type.
		 * 
		 * @return Identifier of this widget. 
		 */
		public function getIdentifier():String {
			return this._identifier;
		}

		/**
		 * Get the human readable title of this widget.
		 * 
		 * @return Title of this widget. 
		 */
		public function getTitle():String {
			return this._title;
		}

		/**
		 * Get the description of the widget.
		 * 
		 * @return Description of this widget. 
		 */
		public function getDescription():String {
			return this._description;
		}

		/**
		 * Get the location of a logo for this widget.
		 * 
		 * @return URL pointing to a picture for this widget.
		 */
		public function getIconUrl():String {
			return this._icon_url;
		}
	}

}
