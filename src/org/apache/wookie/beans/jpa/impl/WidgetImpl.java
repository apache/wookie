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

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.openjpa.persistence.ElementDependent;

import org.apache.wookie.beans.IDescription;
import org.apache.wookie.beans.IFeature;
import org.apache.wookie.beans.ILicense;
import org.apache.wookie.beans.IName;
import org.apache.wookie.beans.IPreferenceDefault;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetIcon;
import org.apache.wookie.beans.IWidgetType;
import org.apache.wookie.beans.jpa.InverseRelationshipCollection;

/**
 * WidgetImpl - JPA IWidget implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="Widget")
@Table(name="Widget")
@NamedQueries({@NamedQuery(name="WIDGET", query="SELECT w FROM Widget w WHERE w.guid = :guid"),
               @NamedQuery(name="DEFAULT_WIDGET", query="SELECT w FROM WidgetDefault wd JOIN wd.widget w WHERE wd.widgetContext = :widgetContext"),
               @NamedQuery(name="WIDGETS", query="SELECT w FROM Widget w JOIN w.widgetTypes wt WHERE wt.widgetContext = :widgetContext")})
public class WidgetImpl implements IWidget
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", nullable=false)
    private int id;

    @Version
    @Column(name="jpa_version")
    @SuppressWarnings("unused")
    private int jpaVersion;

    @Basic
    @Column(name="height")
    private Integer height;

    @Basic
    @Column(name="width")
    private Integer width;

    @OneToMany(mappedBy="widget", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<WidgetTypeImpl> widgetTypes;

    @Basic(optional=false)
    @Column(name="guid", nullable=false)
    private String guid;

    @Basic
    @Column(name="widget_author")
    private String widgetAuthor;

    @Basic
    @Column(name="widget_author_email")
    private String widgetAuthorEmail;

    @Basic
    @Column(name="widget_author_href")
    private String widgetAuthorHref;

    @Basic
    @Column(name="widget_version")
    private String version;

    @OneToMany(mappedBy="widget", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<FeatureImpl> features;

    @OneToMany(mappedBy="widget", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<WidgetIconImpl> widgetIcons;

    @OneToMany(mappedBy="widget", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<LicenseImpl> licenses;

    @OneToMany(mappedBy="widget", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<NameImpl> names;

    @OneToMany(mappedBy="widget", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<DescriptionImpl> descriptions;

    @OneToMany(mappedBy="widget", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<StartFileImpl> startFiles;

    @OneToMany(mappedBy="widget", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<PreferenceDefaultImpl> preferenceDefaults;

    @OneToMany(mappedBy="widget", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<SharedDataImpl> sharedData;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getDescriptions()
     */
    public Collection<IDescription> getDescriptions()
    {
        if (descriptions == null)
        {
            descriptions = new ArrayList<DescriptionImpl>();
        }
        return new InverseRelationshipCollection<WidgetImpl,DescriptionImpl,IDescription>(this, descriptions);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setDescriptions(java.util.Collection)
     */
    public void setDescriptions(Collection<IDescription> descriptions)
    {
        getDescriptions().clear();
        if (descriptions != null)
        {
            for (IDescription description : descriptions)
            {
                getDescriptions().add((DescriptionImpl)description);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getFeatures()
     */
    public Collection<IFeature> getFeatures()
    {
        if (features == null)
        {
            features = new ArrayList<FeatureImpl>();
        }
        return new InverseRelationshipCollection<WidgetImpl,FeatureImpl,IFeature>(this, features);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setFeatures(java.util.Collection)
     */
    public void setFeatures(Collection<IFeature> features)
    {
        getFeatures().clear();
        if (features != null)
        {
            for (IFeature feature : features)
            {
                getFeatures().add((FeatureImpl)feature);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getGuid()
     */
    public String getGuid()
    {
        return guid;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setGuid(java.lang.String)
     */
    public void setGuid(String guid)
    {
        this.guid = guid;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getHeight()
     */
    public Integer getHeight()
    {
        return height;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setHeight(java.lang.Integer)
     */
    public void setHeight(Integer height)
    {
        this.height = height;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
    public Object getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getLicenses()
     */
    public Collection<ILicense> getLicenses()
    {
        if (licenses == null)
        {
            licenses = new ArrayList<LicenseImpl>();
        }
        return new InverseRelationshipCollection<WidgetImpl,LicenseImpl,ILicense>(this, licenses);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setLicenses(java.util.Collection)
     */
    public void setLicenses(Collection<ILicense> licenses)
    {
        getLicenses().clear();
        if (licenses != null)
        {
            for (ILicense license : licenses)
            {
                getLicenses().add((LicenseImpl)license);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getNames()
     */
    public Collection<IName> getNames()
    {
        if (names == null)
        {
            names = new ArrayList<NameImpl>();
        }
        return new InverseRelationshipCollection<WidgetImpl,NameImpl,IName>(this, names);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setNames(java.util.Collection)
     */
    public void setNames(Collection<IName> names)
    {
        getNames().clear();
        if (names != null)
        {
            for (IName name : names)
            {
                getNames().add((NameImpl)name);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getPreferenceDefaults()
     */
    public Collection<IPreferenceDefault> getPreferenceDefaults()
    {
        if (preferenceDefaults == null)
        {
            preferenceDefaults = new ArrayList<PreferenceDefaultImpl>();
        }
        return new InverseRelationshipCollection<WidgetImpl,PreferenceDefaultImpl,IPreferenceDefault>(this, preferenceDefaults);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setPreferenceDefaults(java.util.Collection)
     */
    public void setPreferenceDefaults(Collection<IPreferenceDefault> preferenceDefaults)
    {
        getPreferenceDefaults().clear();
        if (preferenceDefaults != null)
        {
            for (IPreferenceDefault preferenceDefault : preferenceDefaults)
            {
                getPreferenceDefaults().add((PreferenceDefaultImpl)preferenceDefault);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getSharedData()
     */
    public Collection<ISharedData> getSharedData()
    {
        if (sharedData == null)
        {
            sharedData = new ArrayList<SharedDataImpl>();
        }
        return new InverseRelationshipCollection<WidgetImpl,SharedDataImpl,ISharedData>(this, sharedData);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setSharedData(java.util.Collection)
     */
    public void setSharedData(Collection<ISharedData> sharedData)
    {
        getSharedData().clear();
        if (sharedData != null)
        {
            for (ISharedData data : sharedData)
            {
                getSharedData().add((SharedDataImpl)data);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getStartFiles()
     */
    public Collection<IStartFile> getStartFiles()
    {
        if (startFiles == null)
        {
            startFiles = new ArrayList<StartFileImpl>();
        }
        return new InverseRelationshipCollection<WidgetImpl,StartFileImpl,IStartFile>(this, startFiles);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setStartFiles(java.util.Collection)
     */
    public void setStartFiles(Collection<IStartFile> startFiles)
    {
        getStartFiles().clear();
        if (startFiles != null)
        {
            for (IStartFile startFile : startFiles)
            {
                getStartFiles().add((StartFileImpl)startFile);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getVersion()
     */
    public String getVersion()
    {
        return version;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setVersion(java.lang.String)
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetAuthor()
     */
    public String getWidgetAuthor()
    {
        return widgetAuthor;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setWidgetAuthor(java.lang.String)
     */
    public void setWidgetAuthor(String widgetAuthor)
    {
        this.widgetAuthor = widgetAuthor;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetAuthorEmail()
     */
    public String getWidgetAuthorEmail()
    {
        return widgetAuthorEmail;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setWidgetAuthorEmail(java.lang.String)
     */
    public void setWidgetAuthorEmail(String widgetAuthorEmail)
    {
        this.widgetAuthorEmail = widgetAuthorEmail;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetAuthorHref()
     */
    public String getWidgetAuthorHref()
    {
        return widgetAuthorHref;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setWidgetAuthorHref(java.lang.String)
     */
    public void setWidgetAuthorHref(String widgetAuthorHref)
    {
        this.widgetAuthorHref = widgetAuthorHref;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetIcons()
     */
    public Collection<IWidgetIcon> getWidgetIcons()
    {
        if (widgetIcons == null)
        {
            widgetIcons = new ArrayList<WidgetIconImpl>();
        }
        return new InverseRelationshipCollection<WidgetImpl,WidgetIconImpl,IWidgetIcon>(this, widgetIcons);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setWidgetIcons(java.util.Collection)
     */
    public void setWidgetIcons(Collection<IWidgetIcon> widgetIcons)
    {
        getWidgetIcons().clear();
        if (widgetIcons != null)
        {
            for (IWidgetIcon widgetIcon : widgetIcons)
            {
                getWidgetIcons().add((WidgetIconImpl)widgetIcon);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetTypes()
     */
    public Collection<IWidgetType> getWidgetTypes()
    {
        if (widgetTypes == null)
        {
            widgetTypes = new ArrayList<WidgetTypeImpl>();
        }
        return new InverseRelationshipCollection<WidgetImpl,WidgetTypeImpl,IWidgetType>(this, widgetTypes);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setWidgetTypes(java.util.Collection)
     */
    public void setWidgetTypes(Collection<IWidgetType> widgetTypes)
    {
        getWidgetTypes().clear();
        if (widgetTypes != null)
        {
            for (IWidgetType widgetType : widgetTypes)
            {
                getWidgetTypes().add((WidgetTypeImpl)widgetType);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidth()
     */
    public Integer getWidth()
    {
        return width;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setWidth(java.lang.Integer)
     */
    public void setWidth(Integer width)
    {
        this.width = width;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getSharedData(java.lang.String)
     */
    public ISharedData [] getSharedData(String sharedDataKey)
    {
        return Utilities.getSharedData(this, sharedDataKey);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getSharedData(java.lang.String, java.lang.String)
     */
    public ISharedData getSharedData(String sharedDataKey, String name)
    {
        return Utilities.getSharedData(this, sharedDataKey, name);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetDescription()
     */
    public String getWidgetDescription()
    {
        return Utilities.getWidgetDescription(this, "en");
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetShortName()
     */
    public String getWidgetShortName()
    {
        return Utilities.getWidgetShortName(this, "en");
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetTitle()
     */
    public String getWidgetTitle()
    {
        return Utilities.getWidgetTitle(this, "en");
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetTitle(java.lang.String)
     */
    public String getWidgetTitle(String locale)
    {
        return Utilities.getWidgetTitle(this, locale);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getUrl()
     */
    public String getUrl()
    {
        return Utilities.getUrl(this, "en");
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetIconLocation()
     */
    public String getWidgetIconLocation()
    {
        return Utilities.getWidgetIconLocation(this, "en");
    }
}
