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

package org.apache.wookie.beans.jpa.impl;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.wookie.beans.IAuthor;
import org.apache.wookie.beans.jpa.IInverseRelationship;

/**
 * AuthorImpl - JPA IAuthor implementation.
 */
@Entity(name="Author")
@Table(name="Author")
public class AuthorImpl extends LocalizedBeanImpl implements IAuthor, IInverseRelationship<WidgetImpl>
{
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name="id", nullable=false)
  @SuppressWarnings("unused")
  private int id;

    @Version
    @Column(name="jpa_version")
    @SuppressWarnings("unused")
    private int jpaVersion;

    @Basic
    @Column(name="author")
    private String author;

    @Basic
    @Column(name="email")
    private String email;
    
    @Basic
    @Column(name="href")
    private String href;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="widget_id", referencedColumnName="id")
    @SuppressWarnings("unused")
    private WidgetImpl widget;

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

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jpa.IInverseRelationship#updateInverseRelationship(java.lang.Object)
     */
    public void updateInverseRelationship(WidgetImpl owningObject) {
      widget = owningObject;
    }
}
