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

package org.apache.wookie.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * A preference entity
 * @author Paul Sharples
 * @version $Id: Preference.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 *
 */
public class Preference extends AbstractKeyBean<Preference> {

	private static final long serialVersionUID = 1L;

	public Preference() {
	}

	private WidgetInstance widgetInstance;
	private String dkey;
	private String dvalue;

	public WidgetInstance getWidgetInstance() {
		return widgetInstance;
	}

	public void setWidgetInstance(WidgetInstance widgetInstance) {
		this.widgetInstance = widgetInstance;
	}

	public String getDkey() {
		return dkey;
	}

	public void setDkey(String dkey) {
		this.dkey = dkey;
	}

	public String getDvalue() {
		return dvalue;
	}

	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}
	
	/// Active record methods
	public static Preference findById(Object id){
		return (Preference) findById(Preference.class, id);
	}
	
	public static Preference[] findByValue(String key, Object value) {
		return (Preference[]) findByValue(Preference.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static Preference[] findByValues(Map map) {
		return (Preference[]) findByValues(Preference.class, map);
	}
	
	public static Preference[] findAll(){
		return (Preference[]) findAll(Preference.class);
	}
	
	// Special queries
	public static Preference findPreferenceForInstance(WidgetInstance instance, String key){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("widgetInstance", instance);
		map.put("dkey", key);
		Preference[] preferences = findByValues(map);
		if (preferences == null||preferences.length != 1) return null;
		return preferences[0];
	}
	
	public static Preference[] findPreferencesForInstance(WidgetInstance instance){
		return Preference.findByValue("widgetInstance", instance);	
	}

}