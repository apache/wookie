package org.apache.wookie.services.impl;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.w3c.impl.PreferenceEntity;

public class DefaultPreferenceImpl extends PreferenceEntity implements IPreference{
	
	private String id;

	public DefaultPreferenceImpl(String name, String value, boolean readOnly){
		this.setName(name);
		this.setValue(value);
		this.setReadOnly(readOnly);
	}
	
	public DefaultPreferenceImpl(String id, String name, String value, boolean readOnly){
		this.id = id;
		this.setName(name);
		this.setValue(value);
		this.setReadOnly(readOnly);
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IBean#getId()
	 */
	@Override
	public Object getId() {
		return id;
	}
	
}
