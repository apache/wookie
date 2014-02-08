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
package org.apache.wookie.services.impl;

import org.apache.wookie.beans.ISharedData;

public class DefaultSharedDataImpl implements ISharedData {
	
	private String key;
	private String value;
	
	public DefaultSharedDataImpl(String key, String value){
		this.key = key;
		this.value = value;
	}

	@Override
	public Object getId() {
		return null;
	}

	@Override
	public String getDkey() {
		return key;
	}

	@Override
	public void setDkey(String key) {
		this.key = key;
	}

	@Override
	public String getDvalue() {
		return value;
	}

	@Override
	public void setDvalue(String value) {
		this.value = value;
	}

	@Override
	public String getSharedDataKey() {
		return null;
	}

	@Override
	public void setSharedDataKey(String sharedDataKey) {		
	}
}
