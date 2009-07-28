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
 * @version $Id: ApiKey.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
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
