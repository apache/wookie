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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.wookie.w3c.IContent;

/**
 * StartFileImpl - JPA IStartFile implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="StartFile")
@Table(name="StartFile")
public class StartFileImpl implements IContent
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

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#getCharset()
     */
    public String getCharSet()
    {
        return charset;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#setCharset(java.lang.String)
     */
    public void setCharSet(String charset)
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
    public String getSrc()
    {
        return url;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#setUrl(java.lang.String)
     */
    public void setSrc(String url)
    {
        this.url = url;
    }

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.IDirectional#getDir()
	 */
	public String getDir() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.IDirectional#setDir(java.lang.String)
	 */
	public void setDir(String dir) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.IDirectional#isValid()
	 */
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.IContent#getType()
	 */
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.IContent#setType(java.lang.String)
	 */
	public void setType(String type) {
		// TODO Auto-generated method stub
		
	}
}
