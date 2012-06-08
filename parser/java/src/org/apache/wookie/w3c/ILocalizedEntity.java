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
package org.apache.wookie.w3c;

/**
 * An element that supports localization with both a language tag and text direction
 */
public interface ILocalizedEntity extends IElement, ILocalizedElement {

	/**
	 * Get the direction of text, which can be one of "ltr", "rtl", "lro", or "rlo". The default value is "ltr".
	 * @return the direction of the text
	 */
	public String getDir();
	
	/**
	 * Set the direction of text, which can be one of "ltr", "rtl", "lro", or "rlo". The default value is "ltr".
	 * @param dir the direction to set
	 */
	public void setDir(String dir);
	
	/**
	 * Checks whether this object has valid localization settings; specifically that the lang property of this
	 * object is a valid language-tag according to BCP-47 
	 * @return true if the locale for this object is aeither  valid language tag or null, otherwise false.
	 */
	public boolean isValid();

}
