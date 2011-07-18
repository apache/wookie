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

package org.apache.wookie.ajaxmodel;

import java.util.Map;
import java.util.List;

import org.apache.wookie.beans.IPreference;


/**
 * The Widget API: functions required to support the W3C Widget Interface specification
 *
 */
public interface IWidgetAPI {	
	
	/**
	 * Returns the set of preferences as a map of [key][value] objects
	 * @param id_key
	 * @return
	 */
	public List<IPreference> preferences(String id_key);
	
	/**
	 * Returns the widget metadata
	 * @param id_key
	 * @return
	 */
	public Map<String, String> metadata(String id_key);
	
  /**
   * Sets a string preference value in the DB, obtained
   * from the given "key" 
   * @param id_key - the unique instance id key for a widget instance
   * @param key - key for the value to change
   * @param key - the value to change to
   * @return - a string value marking status or an error message
   */
  public String setPreferenceForKey(String id_key, String key, String value);
	
}
