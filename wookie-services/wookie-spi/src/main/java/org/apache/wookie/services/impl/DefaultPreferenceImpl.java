package org.apache.wookie.services.impl;

import org.apache.wookie.w3c.IPreference;
import org.apache.wookie.w3c.impl.PreferenceEntity;

public class DefaultPreferenceImpl extends PreferenceEntity implements IPreference{

	public DefaultPreferenceImpl(String name, String value, boolean readOnly){
		this.setName(name);
		this.setValue(value);
		this.setReadOnly(readOnly);
	}
	
}
