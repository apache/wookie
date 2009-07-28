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
 * A whitelist entity
 * 
 * @author Paul Sharples
 * @version $Id: Whitelist.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 * 
 */
public class Whitelist extends AbstractKeyBean<Whitelist> {
	
	private static final long serialVersionUID = 1L;
	
	private String fUrl;

	public String getfUrl() {
		return fUrl;
	}

	public void setfUrl(String url) {
		fUrl = url;
	}
	
	/// Active record methods
	public static Whitelist findById(Object id){
		return (Whitelist) findById(Whitelist.class, id);
	}

	public static Whitelist[] findByValue(String key, Object value) {
		return (Whitelist[]) findByValue(Whitelist.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static Whitelist[] findByValues(Map map) {
		return (Whitelist[]) findByValues(Whitelist.class, map);
	}
	
	public static Whitelist[] findAll(){
		return (Whitelist[]) findAll(Whitelist.class);
	}

}
