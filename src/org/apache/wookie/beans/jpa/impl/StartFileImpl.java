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

import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.jpa.IInverseRelationship;

/**
 * StartFileImpl - JPA IStartFile implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="StartFile")
@Table(name="StartFile")
public class StartFileImpl implements IStartFile, IInverseRelationship<WidgetImpl>
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
    @Column(name="url")
    private String url;

    @Basic
    @Column(name="charset")
    private String charset;

    @Basic
    @Column(name="lang")
    private String lang;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="widget_id", referencedColumnName="id")
    @SuppressWarnings("unused")
    private WidgetImpl widget;
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IInverseRelationship#updateInverseRelationship(java.lang.Object)
     */
    public void updateInverseRelationship(WidgetImpl owningObject)
    {
        widget = owningObject;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#getCharset()
     */
    public String getCharset()
    {
        return charset;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#setCharset(java.lang.String)
     */
    public void setCharset(String charset)
    {
        this.charset = charset;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#getLang()
     */
    public String getLang()
    {
        return lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#setLang(java.lang.String)
     */
    public void setLang(String lang)
    {
        this.lang = lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#getUrl()
     */
    public String getUrl()
    {
        return url;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#setUrl(java.lang.String)
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
}
