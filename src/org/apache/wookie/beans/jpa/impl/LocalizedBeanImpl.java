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
import javax.persistence.MappedSuperclass;

import org.apache.wookie.w3c.IDirectional;
import org.apache.wookie.w3c.ILocalized;

/**
 * LocalizedBeanImpl - JPA ILocalizedBeanImpl implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@MappedSuperclass
public abstract class LocalizedBeanImpl implements ILocalized, IDirectional 
{ 
	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.IDirectional#isValid()
	 */
	public boolean isValid() {
		return true;
	}

	@Basic 
    @Column(name="dir")
    private String dir;

    @Basic
    @Column(name="lang")
    private String lang;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ILocalizedBean#getDir()
     */
    public String getDir()
    {
        return dir;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ILocalizedBean#setDir(java.lang.String)
     */
    public void setDir(String dir)
    {
        this.dir = dir;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ILocalizedBean#getLang()
     */
    public String getLang()
    {
        return lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ILocalizedBean#setLang(java.lang.String)
     */
    public void setLang(String lang)
    {
        this.lang = lang;
    }
}
