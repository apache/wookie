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
package org.apache.wookie.tests.flatpack;

import org.apache.wookie.beans.IPreference;

/**
 * Mock used for testing preferences
 * @author scottbw@apache.org
 *
 */
public class PreferenceMock implements IPreference {
	
	private Object id;
	private String key;
	private String value;
	private boolean isReadOnly;

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IBean#getId()
	 */
	public Object getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#getDkey()
	 */
	public String getDkey() {

		return key;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#setDkey(java.lang.String)
	 */
	public void setDkey(String dkey) {
		key = dkey;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#getDvalue()
	 */
	public String getDvalue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#setDvalue(java.lang.String)
	 */
	public void setDvalue(String dvalue) {
		value = dvalue;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#isReadOnly()
	 */
	public boolean isReadOnly() {
		return isReadOnly;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IPreference#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) {
		isReadOnly = readOnly;
	}

}
