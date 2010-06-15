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
	import mx.collections.ArrayCollection;
	
	/**
	 * A collection of known widget instances.
	 */
	public class WidgetInstances extends ArrayCollection {

		/**
		 * Record an instance of the given widget instance.
		 * 
		 * @param xml description of the instance as returned by the widget server when the widget was instantiated.
		 * @return the identifier for this instance
		 */
		public function addInstance(instance:WidgetInstance):void {
			super.addItem({id:instance.getId(), obj:instance});
		}
		
		/**
		 * Replace widgets instance sharing the same identifier
		 * or add an instance, if none has been found. 
		 * 
		 * @param instance The widget instance to put in this container.
		 */
		public function replaceOrAddInstance(instance:WidgetInstance):void {
			var has_been_changed:Boolean = false;
			for (var avl_wdgt_idx:Object in super.source) {
				if (super.source[avl_wdgt_idx].id == instance.getId()) {
					super.source[avl_wdgt_idx].obj = instance;
					has_been_changed = true;
				}
			}
			if(!has_been_changed) {
				super.addItem({id:instance.getId(), obj:instance});
			}
		}
		
		/**
		 * Get an instance according to its identifier or null.
		 * 
		 * @param id Identifier of the widget instance to return.
		 * @return First widget instance identified with id or null,
		 * if nothing has been found. 
		 */
		public function getInstance(id:String):WidgetInstance {
			for (var avl_wdgt:Object in super.source) {
				if (avl_wdgt.id == id) {
					return avl_wdgt.obj;
				}
			}
			return null;
		}
		
	}

}
