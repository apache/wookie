/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.apache.wookie.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * A shared data entity
 * 
 * @author Paul Sharples
 * @version $Id: SharedData.java,v 1.1 2009-07-22 09:31:30 scottwilson Exp $
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
