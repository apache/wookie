/**
 * 
 */
package org.tencompetence.widgetservice.beans;

import java.util.Map;

/**
 * @author scott
 *
 */
public class ApiKey extends AbstractKeyBean<ApiKey> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @return the key
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param key the key to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	private String value;
	private String email;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/// Active record methods
	public static ApiKey findById(Object id){
		return (ApiKey) findById(ApiKey.class, id);
	}
	
	public static ApiKey[] findByValue(String key, Object value) {
		return (ApiKey[]) findByValue(ApiKey.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static ApiKey[] findByValues(Map map) {
		return (ApiKey[]) findByValues(ApiKey.class, map);
	}
	
	public static ApiKey[] findAll(){
		return (ApiKey[]) findAll(ApiKey.class);
	}

}
