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
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.w3c.*;

/**
 * WidgetImpl - JPA IWidget implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="Widget")
@Table(name="Widget")
@NamedQueries({@NamedQuery(name="WIDGET", query="SELECT w FROM Widget w WHERE w.guid = :guid")})
public class WidgetImpl extends LocalizedBeanImpl implements IWidget
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
    @Column(name="package_path")
    private String packagePath;

    @Basic
    @Column(name="height")
    private Integer height;

    @Basic
    @Column(name="width")
    private Integer width;

    @Basic
    @Column(name="update_location")
    private String updateLocation;
    
    @Basic(optional=false)
    @Column(name="guid", nullable=false)
    private String guid;

    @Basic
    @Column(name="default_locale")
    private String defaultLocale;

    @Basic
    @Column(name="widget_version")
    private String version;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="widget_id")
    private List<FeatureImpl> features;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="widget_id")
    private List<WidgetIconImpl> widgetIcons;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="widget_id")
    private List<LicenseImpl> licenses;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="widget_id")
    private List<NameImpl> names;
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="widget_id")
    private List<AuthorImpl> authors;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="widget_id")
    private List<DescriptionImpl> descriptions;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="widget_id")
    private Collection<StartFileImpl> startFiles;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="widget_id")
    private List<PreferenceDefaultImpl> preferenceDefaults;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getDescriptions()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<IDescription> getDescriptions()
    {
        if (descriptions == null)
        {
            descriptions = new ArrayList<DescriptionImpl>();
        }
        return (ArrayList<IDescription>)(List)(descriptions);  
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
	 * @see org.apache.wookie.beans.IWidget#getUpdateLocation()
	 */
	public String getUpdateLocation() {
		return this.updateLocation;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidget#setUpdateLocation(java.lang.String)
	 */
	public void setUpdateLocation(String location) {
		this.updateLocation = location;
	}

	/* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getFeatures()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<IFeature> getFeatures()
    {
        if (features == null)
        {
            features = new ArrayList<FeatureImpl>();
        }
        return (ArrayList<IFeature>)(List)(features);
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
    public String getIdentifier()
    {
        return guid;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setGuid(java.lang.String)
     */
    public void setIdentifier(String guid)
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<ILicense> getLicenses()
    {
        if (licenses == null)
        {
            licenses = new ArrayList<LicenseImpl>();
        }
        return (ArrayList<ILicense>)(List)(licenses);
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<IName> getNames()
    {
        if (names == null)
        {
            names = new ArrayList<NameImpl>();
        }
        return (List<IName>)(List)names;
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
     * @see org.apache.wookie.beans.IWidget#getAuthor()
     */
    public IAuthor getAuthor() {
      if (getAuthors().size() != 0)
        return (IAuthor) getAuthors().iterator().next();
      return null;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<IAuthor> getAuthors(){
      if (authors == null)
      {
        authors = new ArrayList<AuthorImpl>();
      }
      return (ArrayList<IAuthor>)(List)(authors);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setAuthor(org.apache.wookie.beans.IAuthor)
     */
    public void setAuthor(IAuthor author) {
      getAuthors().clear();
      getAuthors().add((AuthorImpl) author);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getPreferenceDefaults()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<IPreference> getPreferences()
    {
        if (preferenceDefaults == null)
        {
            preferenceDefaults = new ArrayList<PreferenceDefaultImpl>();
        }
        return (ArrayList<IPreference>)(List)(preferenceDefaults);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setPreferenceDefaults(java.util.Collection)
     */
    public void setPreferences(Collection<IPreference> preferenceDefaults)
    {
        getPreferences().clear();
        if (preferenceDefaults != null)
        {
            for (IPreference preferenceDefault : preferenceDefaults)
            {
                getPreferences().add((PreferenceDefaultImpl)preferenceDefault);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getStartFiles()
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<IContent> getContentList()
    {
        if (startFiles == null)
        {
            startFiles = new ArrayList<StartFileImpl>();
        }
        return (ArrayList<IContent>)(List)startFiles;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setStartFiles(java.util.Collection)
     */
    public void setContentList(Collection<IContent> startFiles)
    {
        getContentList().clear();
        if (startFiles != null)
        {
            for (IContent startFile : startFiles)
            {
                getContentList().add((IContent)startFile);
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
     * @see org.apache.wookie.beans.IWidget#getWidgetIcons()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<IIcon> getIcons()
    {
        if (widgetIcons == null)
        {
            widgetIcons = new ArrayList<WidgetIconImpl>();
        }
        return (ArrayList<IIcon>)(List)(widgetIcons);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setWidgetIcons(java.util.Collection)
     */
    public void setIcons(Collection<IIcon> widgetIcons)
    {
        getIcons().clear();
        if (widgetIcons != null)
        {
            for (IIcon widgetIcon : widgetIcons)
            {
                getIcons().add((WidgetIconImpl)widgetIcon);
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
     * @see org.apache.wookie.beans.IWidget#getWidgetTitle(java.lang.String)
     */
    public String getLocalName(String locale)
    {
        return Utilities.getWidgetTitle(this, locale);
    }


	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidget#getPackagePath()
	 */
	public String getPackagePath() {
		return packagePath;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidget#setPackagePath(java.lang.String)
	 */
	public void setPackagePath(String path) {
		packagePath = path;
		
	}

  /* (non-Javadoc)
   * @see org.apache.wookie.beans.IWidget#getDefaultLocale()
   */
  public String getDefaultLocale() {
    return this.defaultLocale;
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.beans.IWidget#setDefaultLocale(java.lang.String)
   */
  public void setDefaultLocale(String locale) {
    this.defaultLocale = locale;
  }

/* (non-Javadoc)
 * @see org.apache.wookie.w3c.W3CWidget#getAccessList()
 */
public List<IAccess> getAccessList() {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.apache.wookie.w3c.W3CWidget#getViewModes()
 */
public String getViewModes() {
	// TODO Auto-generated method stub
	return null;
}
	
}
