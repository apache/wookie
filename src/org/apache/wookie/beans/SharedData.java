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
 * A shared data entity
 * 
 * @author Paul Sharples
 * @version $Id: SharedData.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 * 
 */
public class SharedData extends AbstractKeyBean<SharedData> {

	private static final long serialVersionUID = 1L;

	public SharedData() {
	}

	private String sharedDataKey;
	private String dkey;
	private String dvalue;
	private String widgetGuid;

	public String getDkey() {
		return dkey;
	}

	public void setDkey(String dkey) {
		this.dkey = dkey;
	}

	public String getDvalue() {
		return dvalue;
	}

	public String getSharedDataKey() {
		return sharedDataKey;
	}

	public void setSharedDataKey(String sharedDataKey) {
		this.sharedDataKey = sharedDataKey;
	}

	/**
	 * @return the widget_guid
	 */
	public String getWidgetGuid() {
		return widgetGuid;
	}

	/**
	 * @param widgetGuid the widget_guid to set
	 */
	public void setWidgetGuid(String widgetGuid) {
		this.widgetGuid = widgetGuid;
	}

	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}
	
	/// Active record methods
	public static SharedData findById(Object id){
		return (SharedData) findById(SharedData.class, id);
	}
	
	public static SharedData[] findByValue(String key, Object value) {
		return (SharedData[]) findByValue(SharedData.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static SharedData[] findByValues(Map map) {
		return (SharedData[]) findByValues(SharedData.class, map);
	}
	
	public static SharedData[] findAll(){
		return (SharedData[]) findAll(SharedData.class);
	}
	
	// Special queries
	public static synchronized SharedData[] findSharedDataForInstance(WidgetInstance instance){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());
		map.put("widgetGuid", instance.getWidget().getGuid());
		return SharedData.findByValues(map);
	}
	public static synchronized SharedData findSharedDataForInstance(WidgetInstance instance, String key){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());
		map.put("widgetGuid", instance.getWidget().getGuid());
		map.put("dkey", key);
		SharedData[] sharedData = findByValues(map);
		if (sharedData == null||sharedData.length != 1) return null;
		return sharedData[0];
	}
	public static boolean clone(String sharedDataKey, String widgetId, String cloneKey){
		boolean ok = true;
		// get data
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("sharedDataKey",sharedDataKey);
		map.put("widgetGuid", widgetId);
		SharedData[] sharedData = findByValues(map);
		// clone data
		for (SharedData entry: sharedData){
			SharedData clonedEntry = new SharedData();
			clonedEntry.setDkey(entry.getDkey());
			clonedEntry.setDvalue(entry.getDvalue());
			clonedEntry.setSharedDataKey(cloneKey);
			clonedEntry.setWidgetGuid(entry.getWidgetGuid());
			boolean saved = clonedEntry.save();
			if (!saved) ok = false;
		}
		return ok;
	}

}
