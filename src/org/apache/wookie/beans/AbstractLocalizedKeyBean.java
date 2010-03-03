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

import org.apache.wookie.w3c.ILocalizedElement;

public abstract class AbstractLocalizedKeyBean<T> extends AbstractKeyBean<T> implements ILocalizedElement {
	
	private static final long serialVersionUID = -6974753058632187787L;
	
	/**
	 * a Language string conforming to BCP47
	 */
	protected String lang;
	/**
	 * Text direction conforming to http://www.w3.org/TR/2007/REC-its-20070403/
	 */
	protected String dir;
	
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}

}
