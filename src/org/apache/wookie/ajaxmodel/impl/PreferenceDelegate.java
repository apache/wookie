package org.apache.wookie.ajaxmodel.impl;

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
import org.apache.wookie.beans.IPreference;

/**
 * Delegate for Preference implementations; this is used for DWR marshalling
 */
public class PreferenceDelegate implements IPreference {
	
	private String dkey;
	private String dvalue;
	private boolean readOnly;
	
	public PreferenceDelegate(){
	}
	
	public PreferenceDelegate(IPreference pref){
		this.dkey = pref.getDkey();
		this.dvalue = pref.getDvalue();
		this.readOnly = pref.isReadOnly();
	}

	public String getDkey() {
		return dkey;
	}

	public String getDvalue() {
		return dvalue;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setDkey(String dkey) {
		this.dkey = dkey;
	}

	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Object getId() {
		// TODO 
		return null;
	}

}
