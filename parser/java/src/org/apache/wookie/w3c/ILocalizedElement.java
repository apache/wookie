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
 * An element that can be localized by language tag
 * @see ILocalizedEntity
 */
public interface ILocalizedElement {
	
	/**
	 * Get the language tag for the element. This should conform to BCP47.
	 * @return the language tag, or null if no language tag has been set for the element
	 */
	public String getLang();
	
	/**
	 * Set the language tag for the element. 
	 * @param lang a language tag to set; this should conform to BCP47.
	 */
	public void setLang(String lang);

}
