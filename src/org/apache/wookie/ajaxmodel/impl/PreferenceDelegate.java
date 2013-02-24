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
 * Delegate for Preference implementations; this is used  instead of the Preference Bean implementation
 * in cases where we simply want to work with a preference object outside a transaction context, for example
 * when the Ajax implementation needs to create a clone of the object for marshalling into JSON.
 */
public class PreferenceDelegate implements IPreference {
	
	private String dkey;
	private String dvalue;
	private boolean readOnly;
	
	/**
	 * Default constructor
	 */
	public PreferenceDelegate(){
	}
	
	/**
	 * Construct a delegate from a Preference bean 
	 * @param pref
	 */
	public PreferenceDelegate(IPreference pref){
		this.dkey = pref.getName();
		this.dvalue = pref.getValue();
		this.readOnly = pref.isReadOnly();
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#getDkey()
	 */
	public String getName() {
		return dkey;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#getDvalue()
	 */
	public String getValue() {
		return dvalue;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#isReadOnly()
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#setDkey(java.lang.String)
	 */
	public void setName(String dkey) {
		this.dkey = dkey;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#setDvalue(java.lang.String)
	 */
	public void setValue(String dvalue) {
		this.dvalue = dvalue;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * This method is required for the IPreference interface, but in the case of this
	 * delegate isn't meaningful as this object is not persisted, and
	 * is only used in a transient fashion.
	 */
	public Object getId() {
		return null;
	}

}
