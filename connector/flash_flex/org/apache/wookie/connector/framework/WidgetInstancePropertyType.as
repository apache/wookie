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
	 * The WidgetInstanceproperty_type class provides values to specify
	 * access of a property associated to a WidgetInstance.
	 * These values are read-only.
	 */
	public class WidgetInstancePropertyType {
		/**
		 * String specifing a public property access.
		 */
		private static var _PUBLIC:String = "setpublicproperty";
		/**
		 * String specifing a private property access.
		 */
		private static var _PRIVATE:String = "setprivateproperty";
		/**
		 * String specifing a personal property access.
		 */
		private static var _PERSONAL:String = "setpersonalproperty";

		/**
		 * Specifies that the property will be public.
		 * 
		 * @return String specifing a public property access.
		 */
		public static function get PUBLIC():String {
			return _PUBLIC;
		}

		/**
		 * Specifies that the property will be private.
		 * 
		 * @return String specifing a private property access.
		 */
		public static function get PRIVATE():String {
			return _PRIVATE;
		}

		/**
		 * Specifies that the property will be personal.
		 * 
		 * @return String specifing a personal property access.
		 */
		public static function get PERSONAL():String {
			return _PERSONAL;
		}

		/**
		 * Get an array of available property accesses.
		 * 
		 * @return Array of available property accesses.
		 */
		public static function get ALL():Array {
			var allTypes:Array = new Array();
			allTypes.push(_PUBLIC);
			allTypes.push(_PRIVATE);
			allTypes.push(_PERSONAL);
			return allTypes;
		}
	}

}
