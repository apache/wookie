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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;

/**
 * WidgetDefaultImpl - JPA IWidgetDefault implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="WidgetDefault")
@Table(name="WidgetDefault")
public class WidgetDefaultImpl implements IWidgetDefault
{
    @Id
    @Column(name="widgetContext", nullable=false)
    private String widgetContext;

    @Version
    @Column(name="jpa_version")
    @SuppressWarnings("unused")
    private int jpaVersion;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="widgetId", referencedColumnName="id", nullable=false)
    private WidgetImpl widget;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
    public Object getId()
    {
        return widgetContext;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetDefault#getWidget()
     */
    public IWidget getWidget()
    {
        return widget;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetDefault#setWidget(org.apache.wookie.beans.IWidget)
     */
    public void setWidget(IWidget widget)
    {
        this.widget = (WidgetImpl)widget;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetDefault#getWidgetContext()
     */
    public String getWidgetContext()
    {
        return widgetContext;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetDefault#setWidgetContext(java.lang.String)
     */
    public void setWidgetContext(String widgetContext)
    {
        this.widgetContext = widgetContext;
    }
}
