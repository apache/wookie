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
	import flash.events.ErrorEvent;
	import flash.events.Event;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	/**
	 * Load a widget instance given an url request and a widget identifier.
	 * This works like URLLoader excepting that it provides a WidgetInstance
	 * object in the field data or assign it null and dispatch an ErrorEvent.
	 */
	public class WidgetInstanceLoader extends URLLoader {
		/**
		 * Identifier of the widget that will be loaded.
		 */
		private var _widget_id:String;
		
		/**
		 * Create a WidgetInstance object from the received XML data and
		 * the widget identifier store in this widget instance loader.
		 * Dispatch an ErrorEvent of type ErrorEvent.ERROR, if it can't
		 * retrieve data from the XML.
		 * 
		 * @param event Event containing XML data describing a widget instance.
		 */
		private function _buildWidgetInstanceHandler(event:Event):void {
			var widgetInstancesLoader:URLLoader = URLLoader(event.target);
			if (event.target == this) {
				try {
					var xmlWidgetInstance:XML = XML(widgetInstancesLoader.data.toString());
					var wdgtInstUrl:String = xmlWidgetInstance.url;
					var wdgtInstTitle:String = xmlWidgetInstance.title;
					var wdgtInstHeight:String = xmlWidgetInstance.height;
					var wdgtInstWidth:String = xmlWidgetInstance.width;
					var widgetInstance:WidgetInstance = new WidgetInstance(wdgtInstUrl, this._widget_id, wdgtInstTitle, wdgtInstHeight, wdgtInstWidth);
					this.data = widgetInstance;
				} catch(err:Error) {
					this.data = null;
					event.preventDefault();
					event.stopPropagation();
					this.dispatchEvent(new ErrorEvent(ErrorEvent.ERROR, true, true, "Unable to create a WidgetInstanec object from XML."));
				}
			}
		}

		/**
		 * Create a new loader to download a client representation of a widget instance.
		 * 
		 * @param widget_id Identifier of the widget to load.
		 * @param request URL request returning a widget instance under a XML format.
		 */
		public function WidgetInstanceLoader(widget_id:String = null, request:URLRequest = null) {
			// initialise fields
			super(null);
			setWidgetId(widget_id);
			// initialise event handler
			super.addEventListener(Event.COMPLETE, _buildWidgetInstanceHandler, false, int.MAX_VALUE);
			// maintain URLLoader constructor behavior
			if (null != request) {
				this.load(request);
			}
		}
		
		/**
		 * Get the identifier of the widget to load.
		 * 
		 * @return The identifier of the widget to load. 
		 */
		public function getWidgetId():String {
			return this._widget_id;
		}
		
		/**
		 * Set a new identifier for the widget to load.
		 * 
		 * @param new_widget_id The identifier to associated with the widget to load.
		 */
		public function setWidgetId(new_widget_id:String):void {
			this._widget_id = new_widget_id;
		}
	}

}
