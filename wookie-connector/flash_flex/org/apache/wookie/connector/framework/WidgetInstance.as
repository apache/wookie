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
	 * An instance of a widget for use on the client.
	 * 
	 * @refactor this class duplicates code in the widget bean on the server side
	 */
	public class WidgetInstance {
		/**
		 * URL pointing to the widget instance on the server side for this widget instance.
		 */
		private var _url:String;
		/**
		 * The unique identifier of the widget instance on the server side.
		 */
		private var _id:String;
		/**
		 * Title of the widget instance on the server side.
		 */
		private var _title:String;
		/**
		 * Height of the widget instance on the server side.
		 */
		private var _height:String;
		/**
		 * Width of the widget instance on the server side.
		 */
		private var _width:String;

		/**
		 * Create a new client representation of a widget instance.
		 * 
		 * @param url URL pointing to the widget instance on the server side.
		 * @param id The unique identifier of the widget instance on the server side.
		 * @param title Title of the widget instance on the server side.
		 * @param height Height of the widget instance on the server side.
		 * @param width Width of the widget instance on the server side.
		 */
		public function WidgetInstance(url:String, id:String, title:String, height:String, width:String) {
			setId(id);
			setUrl(url);
			setTitle(title);
			setHeight(height);
			setWidth(width);
		}

		/**
		 * Get URL of this widget instance.
		 * 
		 * @return URL of this widget instance.
		 */
		public function getUrl():String {
			return this._url;
		}

		/**
		 * Set a new URL for this widget instance.
		 * 
		 * @param new_url URL pointing to a widget instance to assign to this widget instance.
		 */
		public function setUrl(new_url:String):void {
			this._url = new_url;
		}

		/**
		 * Get the unique identifier of this widget instance.
		 * 
		 * @return The unique identifier of this widget instance.
		 */
		public function getId():String {
			return this._id;
		}

		/**
		 * Set a new identifier for this widget instance.
		 * 
		 * @param new_id Identifier to assign to this widget instance.
		 */
		public function setId(new_id:String):void {
		this._id = new_id;
		}

		/**
		 * Get title of this widget instance.
		 * 
		 * @return Title of this widget instance.
		 */
		public function getTitle():String {
			return this._title;
		}

		/**
		 * Set a new title for this widget instance.
		 * 
		 * @param new_title Title to assign to this widget instance.
		 */
		public function setTitle(new_title:String):void {
			this._title = new_title;
		}

		/**
		 * Get height of this widget instance.
		 * 
		 * @return Height of this widget instance.
		 */
		public function getHeight():String {
			return this._height;
		}

		/**
		 * Set a new height for this widget instance.
		 * 
		 * @param new_height Height to assign to this widget instance.
		 */
		public function setHeight(new_height:String):void {
			this._height = new_height;
		}

		/**
		 * Get width of this widget instance.
		 * 
		 * @return Width of this widget instance.
		 */
		public function getWidth():String {
			return this._width;
		}

		/**
		 * Set a new width for this widget instance.
		 * 
		 * @param new_width Width to assign to this widget instance.
		 */
		public function setWidth(new_width:String):void {
			this._width = new_width;
		}
	}

}
