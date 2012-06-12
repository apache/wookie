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

import org.apache.wookie.w3c.IIcon;

/**
 * WidgetIconImpl - JPA IWidgetIcon implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="WidgetIcon")
@Table(name="WidgetIcon")
public class WidgetIconImpl implements IIcon
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
    @Column(name="src")
    private String src;

    @Basic
    @Column(name="height")
    private Integer height;

    @Basic
    @Column(name="width")
    private Integer width;

    @Basic
    @Column(name="lang")
    private String lang;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#getHeight()
     */
    public Integer getHeight()
    {
        return height;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#setHeight(java.lang.Integer)
     */
    public void setHeight(Integer height)
    {
        this.height = height;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#getLang()
     */
    public String getLang()
    {
        return lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#setLang(java.lang.String)
     */
    public void setLang(String lang)
    {
        this.lang = lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#getSrc()
     */
    public String getSrc()
    {
        return src;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#setSrc(java.lang.String)
     */
    public void setSrc(String src)
    {
        this.src = src;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#getWidth()
     */
    public Integer getWidth()
    {
        return width;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#setWidth(java.lang.Integer)
     */
    public void setWidth(Integer width)
    {
        this.width = width;
    }
}
