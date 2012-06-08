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
 *  limitations under the License.
 */
package org.apache.wookie.beans;

/**
 * IAuthor - information about the author of the widget
 */
public interface IAuthor extends ILocalizedBean {
	
	/**
	 * Get the details of the author, e.g. name or names of people and/or organisations
	 * @return the author details, or null if there are no author details
	 */
	public String getAuthorName();
	
	/**
	 * Get the email address of the author
	 * @return the author email address, or null if there is no author email address
	 */
	public String getEmail();
	
	/**
	 * Get the URL of the author details, e.g. the author's blog or website address
	 * @return the author's URL, or null if there is no author URL
	 */
	public String getHref();
	
	/**
	 * Set the author details
	 * @param author tha author details to set
	 */
	public void setAuthorName(String author);
	
	/**
	 * Set the author's email
	 * @param email the author email to set
	 */
	public void setEmail(String email);
	
	/**
	 * Set the author's Href
	 * @param href the author href to set
	 */
	public void setHref(String href);

}
