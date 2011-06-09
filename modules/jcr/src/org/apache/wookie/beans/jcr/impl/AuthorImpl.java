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
package org.apache.wookie.beans.jcr.impl;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.apache.wookie.beans.IAuthor;
import org.apache.wookie.beans.jcr.IIdElement;

/**
 * JCR Implementation of Author bean
 */
@Node(jcrType="wookie:author", extend=LocalizedBeanImpl.class, discriminator=false)
public class AuthorImpl  extends LocalizedBeanImpl implements IAuthor, IIdElement{
	
    @Field(id=true, jcrName="wookie:elementId")
    private long elementId = -1;
    
    @Field(jcrName="wookie:author")
    private String author;
    
    @Field(jcrName="wookie:email")
    private String email;

    @Field(jcrName="wookie:href")
    private String href;
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IIdElement#getElementId()
     */
    public long getElementId()
    {
        return elementId;
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IIdElement#setElementId(long)
     */
    public void setElementId(long elementId)
    {
        this.elementId = elementId;
    }

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IAuthor#getAuthor()
	 */
	public String getAuthor() {
		return this.author;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IAuthor#getEmail()
	 */
	public String getEmail() {
		return this.email;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IAuthor#getHref()
	 */
	public String getHref() {
		return this.href;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IAuthor#setAuthor(java.lang.String)
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IAuthor#setEmail(java.lang.String)
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IAuthor#setHref(java.lang.String)
	 */
	public void setHref(String href) {
		this.href = href;
	}
}
