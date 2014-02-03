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

package org.apache.wookie.beans.jcr.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import org.apache.wookie.w3c.IAccess;
import org.apache.wookie.w3c.IAuthor;
import org.apache.wookie.w3c.IDescription;
import org.apache.wookie.w3c.IFeature;
import org.apache.wookie.w3c.ILicense;
import org.apache.wookie.w3c.IName;
import org.apache.wookie.w3c.IPreference;
import org.apache.wookie.w3c.IContent;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.w3c.IIcon;
import org.apache.wookie.beans.jcr.IPathBean;
import org.apache.wookie.beans.jcr.IUuidBean;
import org.apache.wookie.beans.jcr.IdCollection;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;

/**
 * WidgetImpl - JCR OCM IWidget implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:widget", jcrMixinTypes="mix:referenceable", discriminator=false)
public class WidgetImpl extends LocalizedBeanImpl implements IWidget, IPathBean, IUuidBean
{
  
    @Field(jcrName="wookie:defaultLocale")
    private String defaultLocale;
    
    @Field(path=true)
    private String path;
    
    @Field(uuid=true)
    private String uuid;
    
    @Field(jcrName="wookie:packagePath")
    private String packagePath;

    @Field(jcrName="wookie:updateLocation")
    private String updateLocation;
    
    @Field(jcrName="wookie:height")
    private Integer height;
    
    @Field(jcrName="wookie:width")
    private Integer width;
    
    @Field(jcrName="wookie:guid")
    private String guid;

    @Field(jcrName="wookie:widgetVersion")
    private String version;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:features", elementClassName=FeatureImpl.class)
    private List<FeatureImpl> featureImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:widgetIcons", elementClassName=WidgetIconImpl.class)
    private List<WidgetIconImpl> widgetIconImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:licenses", elementClassName=LicenseImpl.class)
    private List<LicenseImpl> licenseImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:names", elementClassName=NameImpl.class)
    private List<NameImpl> nameImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:authors", elementClassName=NameImpl.class)
    private List<AuthorImpl> authorImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:descriptions", elementClassName=DescriptionImpl.class)
    private List<DescriptionImpl> descriptionImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:startFiles", elementClassName=StartFileImpl.class)
    private List<StartFileImpl> startFileImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:preferenceDefaults", elementClassName=PreferenceDefaultImpl.class)
    private List<PreferenceDefaultImpl> preferenceDefaultImpls;
    
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
     * @see org.apache.wookie.beans.IWidget#getDescriptions()
     */
    public List<IDescription> getDescriptions()
    {
        if (descriptionImpls == null)
        {
            descriptionImpls = new ArrayList<DescriptionImpl>();
        }
        return new IdCollection<DescriptionImpl,IDescription>(descriptionImpls);
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

    /**
     * Get description implementations collection.
     * 
     * @return description implementations collection
     */
    public Collection<DescriptionImpl> getDescriptionImpls()
    {
        return descriptionImpls;
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

	/**
     * Set description implementations collection.
     * 
     * @param descriptionImpls description implementations collection
     */
    public void setDescriptionImpls(List<DescriptionImpl> descriptionImpls)
    {
        this.descriptionImpls = descriptionImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getFeatures()
     */
    public List<IFeature> getFeatures()
    {
        if (featureImpls == null)
        {
            featureImpls = new ArrayList<FeatureImpl>();
        }
        return new IdCollection<FeatureImpl,IFeature>(featureImpls);
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

    /**
     * Get feature implementations collection.
     * 
     * @return feature implementations collection
     */
    public Collection<FeatureImpl> getFeatureImpls()
    {
        return featureImpls;
    }

    /**
     * Set feature implementations collection.
     * 
     * @param featureImpls feature implementations collection
     */
    public void setFeatureImpls(List<FeatureImpl> featureImpls)
    {
        this.featureImpls = featureImpls;
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
    public String getId()
    {
        return path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getLicenses()
     */
    public List<ILicense> getLicenses()
    {
        if (licenseImpls == null)
        {
            licenseImpls = new ArrayList<LicenseImpl>();
        }
        return new IdCollection<LicenseImpl,ILicense>(licenseImpls);
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

    /**
     * Get license implementations collection.
     * 
     * @return license implementations collection
     */
    public Collection<LicenseImpl> getLicenseImpls()
    {
        return licenseImpls;
    }

    /**
     * Set license implementations collection.
     * 
     * @param licenseImpls license implementations collection
     */
    public void setLicenseImpls(List<LicenseImpl> licenseImpls)
    {
        this.licenseImpls = licenseImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getNames()
     */
    public List<IName> getNames()
    {
        if (nameImpls == null)
        {
            nameImpls = new ArrayList<NameImpl>();
        }
        return new IdCollection<NameImpl,IName>(nameImpls);
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

    /**
     * Get name implementations collection.
     * 
     * @return name implementations collection
     */
    public Collection<NameImpl> getNameImpls()
    {
        return nameImpls;
    }

    /**
     * Set name implementations collection.
     * 
     * @param nameImpls name implementations collection
     */
    public void setNameImpls(List<NameImpl> nameImpls)
    {
        this.nameImpls = nameImpls;
    }
    
    public List<IAuthor> getAuthors()
    {
        if (authorImpls == null)
        {
          authorImpls = new ArrayList<AuthorImpl>();
        }
        return new IdCollection<AuthorImpl,IAuthor>(authorImpls);
    }

    public void setAuthors(Collection<IAuthor> authors)
    {
        getAuthors().clear();
        if (authors != null)
        {
            for (IAuthor author : authors)
            {
                getAuthors().add((AuthorImpl)author);
            }
        }
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getAuthor()
     */
    public IAuthor getAuthor(){
      if (getAuthors().size() == 0) return null;
      return getAuthors().iterator().next();
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#setAuthor(org.apache.wookie.beans.IAuthor)
     */
    public void setAuthor(IAuthor author) {
      getAuthors().clear();
      getAuthors().add(author);
    }
    
    public IAuthor getOrCreateAuthor(){
      if(getAuthor() == null){
        IAuthor author = PersistenceManagerFactory.getPersistenceManager().newInstance(IAuthor.class);
        setAuthor(author);
      }
      return getAuthor();
    }

    /**
     * Get author implementations collection.
     */
    public Collection<AuthorImpl> getAuthorImpls()
    {
        return authorImpls;
    }

    /**
     * Set author implementations collection.
     */
    public void setAuthorImpls(List<AuthorImpl> authorImpls)
    {
        this.authorImpls = authorImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IPathBean#getNodePath()
     */
    public String getNodePath()
    {
        return path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IPathBean#setNodePath(java.lang.String)
     */
    public String setNodePath(String nodeRootPath)
    {
        if (path == null)
        {
            path = nodeRootPath+"/"+JCRPersistenceManager.escapeJCRName(guid);
        }
        return path;
    }

    /**
     * Get raw persistent JCR absolute node path.
     * 
     * @return absolute path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Set raw persistent JCR absolute node path.
     * 
     * @param path absolute path
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getPreferenceDefaults()
     */
    public List<IPreference> getPreferences()
    {
        if (preferenceDefaultImpls == null)
        {
            preferenceDefaultImpls = new ArrayList<PreferenceDefaultImpl>();
        }
        return new IdCollection<PreferenceDefaultImpl,IPreference>(preferenceDefaultImpls);
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

    /**
     * Get preference default implementations collection.
     * 
     * @return preference default implementations collection
     */
    public Collection<PreferenceDefaultImpl> getPreferenceDefaultImpls()
    {
        return preferenceDefaultImpls;
    }

    /**
     * Set preference default implementations collection.
     * 
     * @param preferenceDefaultImpls preference default implementations collection
     */
    public void setPreferenceDefaultImpls(List<PreferenceDefaultImpl> preferenceDefaultImpls)
    {
        this.preferenceDefaultImpls = preferenceDefaultImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getStartFiles()
     */
    public List<IContent> getContentList()
    {
        if (startFileImpls == null)
        {
            startFileImpls = new ArrayList<StartFileImpl>();
        }
        return new IdCollection<StartFileImpl,IContent>(startFileImpls);
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
                getContentList().add((StartFileImpl)startFile);
            }
        }
    }

    /**
     * Get start file implementations collection.
     * 
     * @return start file implementations collection
     */
    public Collection<StartFileImpl> getStartFileImpls()
    {
        return startFileImpls;
    }

    /**
     * Set start file implementations collection.
     * 
     * @param startFileImpls start file implementations collection
     */
    public void setStartFileImpls(List<StartFileImpl> startFileImpls)
    {
        this.startFileImpls = startFileImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IUuidBean#getUuid()
     */
    public String getUuid()
    {
        return uuid;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IUuidBean#setUuid(java.lang.String)
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
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
    public List<IIcon> getIcons()
    {
        if (widgetIconImpls == null)
        {
            widgetIconImpls = new ArrayList<WidgetIconImpl>();
        }
        return new IdCollection<WidgetIconImpl,IIcon>(widgetIconImpls);
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

    /**
     * Get widget icon implementations collection.
     * 
     * @return widget icon implementations collection
     */
    public Collection<WidgetIconImpl> getWidgetIconImpls()
    {
        return widgetIconImpls;
    }

    /**
     * Set widget icon implementations collection.
     * 
     * @param widgetIconImpls widget icon implementations collection
     */
    public void setWidgetIconImpls(List<WidgetIconImpl> widgetIconImpls)
    {
        this.widgetIconImpls = widgetIconImpls;
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
    
    public String getDefaultLocale(){
      return this.defaultLocale;
    }
    
    public void setDefaultLocale(String locale){
      this.defaultLocale = locale;
    }

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#getAccessList()
	 */
	public List<IAccess> getAccessList() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#getViewModes()
	 */
	public String getViewModes() {
		return null;
	}
}
