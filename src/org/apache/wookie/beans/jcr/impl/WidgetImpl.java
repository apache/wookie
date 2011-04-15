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

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import org.apache.wookie.beans.IDescription;
import org.apache.wookie.beans.IFeature;
import org.apache.wookie.beans.ILicense;
import org.apache.wookie.beans.IName;
import org.apache.wookie.beans.IPreferenceDefault;
import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetIcon;
import org.apache.wookie.beans.IWidgetType;
import org.apache.wookie.beans.jcr.IPathBean;
import org.apache.wookie.beans.jcr.IUuidBean;
import org.apache.wookie.beans.jcr.IdCollection;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;

/**
 * WidgetImpl - JCR OCM IWidget implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:widget", jcrMixinTypes="mix:referenceable", discriminator=false)
public class WidgetImpl extends LocalizedBeanImpl implements IWidget, IPathBean, IUuidBean
{
    @Field(path=true)
    private String path;
    
    @Field(uuid=true)
    private String uuid;
    
    @Field(jcrName="wookie:packagePath")
    private String packagePath;
    
    @Field(jcrName="wookie:height")
    private Integer height;
    
    @Field(jcrName="wookie:width")
    private Integer width;
    
    @Field(jcrName="wookie:guid")
    private String guid;

    @Field(jcrName="wookie:widgetAuthor")
    private String widgetAuthor;

    @Field(jcrName="wookie:widgetAuthorEmail")
    private String widgetAuthorEmail;

    @Field(jcrName="wookie:widgetAuthorHref")
    private String widgetAuthorHref;

    @Field(jcrName="wookie:widgetVersion")
    private String version;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:widgetTypes", elementClassName=WidgetTypeImpl.class)
    private Collection<WidgetTypeImpl> widgetTypeImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:features", elementClassName=FeatureImpl.class)
    private Collection<FeatureImpl> featureImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:widgetIcons", elementClassName=WidgetIconImpl.class)
    private Collection<WidgetIconImpl> widgetIconImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:licenses", elementClassName=LicenseImpl.class)
    private Collection<LicenseImpl> licenseImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:names", elementClassName=NameImpl.class)
    private Collection<NameImpl> nameImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:descriptions", elementClassName=DescriptionImpl.class)
    private Collection<DescriptionImpl> descriptionImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:startFiles", elementClassName=StartFileImpl.class)
    private Collection<StartFileImpl> startFileImpls;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:preferenceDefaults", elementClassName=PreferenceDefaultImpl.class)
    private Collection<PreferenceDefaultImpl> preferenceDefaultImpls;
    
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
    public Collection<IDescription> getDescriptions()
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

    /**
     * Set description implementations collection.
     * 
     * @param descriptionImpls description implementations collection
     */
    public void setDescriptionImpls(Collection<DescriptionImpl> descriptionImpls)
    {
        this.descriptionImpls = descriptionImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getFeatures()
     */
    public Collection<IFeature> getFeatures()
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
    public void setFeatureImpls(Collection<FeatureImpl> featureImpls)
    {
        this.featureImpls = featureImpls;
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
    public String getId()
    {
        return path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getLicenses()
     */
    public Collection<ILicense> getLicenses()
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
    public void setLicenseImpls(Collection<LicenseImpl> licenseImpls)
    {
        this.licenseImpls = licenseImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getNames()
     */
    public Collection<IName> getNames()
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
    public void setNameImpls(Collection<NameImpl> nameImpls)
    {
        this.nameImpls = nameImpls;
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
    public Collection<IPreferenceDefault> getPreferenceDefaults()
    {
        if (preferenceDefaultImpls == null)
        {
            preferenceDefaultImpls = new ArrayList<PreferenceDefaultImpl>();
        }
        return new IdCollection<PreferenceDefaultImpl,IPreferenceDefault>(preferenceDefaultImpls);
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
    public void setPreferenceDefaultImpls(Collection<PreferenceDefaultImpl> preferenceDefaultImpls)
    {
        this.preferenceDefaultImpls = preferenceDefaultImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getStartFiles()
     */
    public Collection<IStartFile> getStartFiles()
    {
        if (startFileImpls == null)
        {
            startFileImpls = new ArrayList<StartFileImpl>();
        }
        return new IdCollection<StartFileImpl,IStartFile>(startFileImpls);
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
    public void setStartFileImpls(Collection<StartFileImpl> startFileImpls)
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
        if (widgetIconImpls == null)
        {
            widgetIconImpls = new ArrayList<WidgetIconImpl>();
        }
        return new IdCollection<WidgetIconImpl,IWidgetIcon>(widgetIconImpls);
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
    public void setWidgetIconImpls(Collection<WidgetIconImpl> widgetIconImpls)
    {
        this.widgetIconImpls = widgetIconImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidget#getWidgetTypes()
     */
    public Collection<IWidgetType> getWidgetTypes()
    {
        if (widgetTypeImpls == null)
        {
            widgetTypeImpls = new ArrayList<WidgetTypeImpl>();
        }
        return new IdCollection<WidgetTypeImpl,IWidgetType>(widgetTypeImpls);
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

    /**
     * Get widget type implementations collection.
     * 
     * @return widget type implementations collection
     */
    public Collection<WidgetTypeImpl> getWidgetTypeImpls()
    {
        return widgetTypeImpls;
    }

    /**
     * Set widget type implementations collection.
     * 
     * @param widgetTypeImpls widget type implementations collection
     */
    public void setWidgetTypeImpls(Collection<WidgetTypeImpl> widgetTypeImpls)
    {
        this.widgetTypeImpls = widgetTypeImpls;
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
