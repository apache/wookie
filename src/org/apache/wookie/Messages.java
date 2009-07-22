package org.apache.wookie;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
			
	private ResourceBundle resourceBundle;
	
	public Messages(ResourceBundle rb) {
		resourceBundle = rb;
	}
	
	public String getString(String key) {
		try {
			return resourceBundle.getString(key);
		} 
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
