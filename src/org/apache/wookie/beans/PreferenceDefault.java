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

import java.util.Map;

/** 
 * @author Scott Wilson
 * @version $Id: PreferenceDefault.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $ 
 */
public class PreferenceDefault extends AbstractKeyBean<PreferenceDefault> {

	private static final long serialVersionUID = 3036952538484789860L;

	private Widget widget;
	private String preference;
	private String value;
	private boolean readOnly;
	
	public Widget getWidget(){
		return widget;
	}
	
	public void setWidget(Widget w){
		widget = w;
	}
	
	/**
	 * @return the preference
	 */
	public String getPreference() {
		return preference;
	}
	/**
	 * @param preference the preference to set
	 */
	public void setPreference(String preference) {
		this.preference = preference;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/// Active record methods
	public static PreferenceDefault findById(Object id){
		return (PreferenceDefault) findById(PreferenceDefault.class, id);
	}
	
	public static PreferenceDefault[] findByValue(String key, Object value) {
		return (PreferenceDefault[]) findByValue(PreferenceDefault.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static PreferenceDefault[] findByValues(Map map) {
		return (PreferenceDefault[]) findByValues(PreferenceDefault.class, map);
	}
	
	public static PreferenceDefault[] findAll(){
		return (PreferenceDefault[]) findAll(PreferenceDefault.class);
	}

}
