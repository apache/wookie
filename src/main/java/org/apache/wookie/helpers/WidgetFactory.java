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
 * limitations under the License.
 */
package org.apache.wookie.helpers;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.proxy.Policies;
import org.apache.wookie.proxy.Policy;
import org.apache.wookie.util.WidgetFileUtils;
import org.apache.wookie.w3c.IAccess;
import org.apache.wookie.w3c.IAuthor;
import org.apache.wookie.w3c.IContent;
import org.apache.wookie.w3c.IName;
import org.apache.wookie.w3c.IFeature;
import org.apache.wookie.w3c.IDescription;
import org.apache.wookie.w3c.IParam;
import org.apache.wookie.w3c.IIcon;
import org.apache.wookie.w3c.ILicense;
import org.apache.wookie.w3c.W3CWidget;

/**
 * Factory for creating and destroying Widgets
 */
public class WidgetFactory {
	static Logger _logger = Logger.getLogger(WidgetFactory.class.getName());

	
	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @param file the .wgt file the Widget was loaded from
	 * @param grantAccessRequests whether to automatically grant access requests for the widget
	 * @return the widget
	 */
	public static IWidget addNewWidget(W3CWidget model, File file, boolean grantAccessRequests) {
		IWidget widget = addNewWidget(model, grantAccessRequests);
		widget.setPackagePath(file.getPath());
		return widget;
	}

	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @return the widget
	 */
	public static IWidget addNewWidget(W3CWidget model) {
		return addNewWidget(model,false);
	}


	/**
	 * Constructs a new Widget and persists it and all dependent objects
	 * @param model the model for the widget
	 * @param grantAccessRequests whether to grant access requests created for the widget
	 * @return the widget
	 */
	public static IWidget addNewWidget(W3CWidget model, boolean grantAccessRequests) {
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget widget = createWidget(persistenceManager, model);
		createAuthor(persistenceManager, model, widget);
		createStartFiles(persistenceManager, model,widget);
		createNames(persistenceManager, model,widget);
		createDescriptions(persistenceManager, model,widget);
		createIcons(persistenceManager, model, widget);
		createLicenses(persistenceManager, model,widget);		
		createPreferences(persistenceManager, model,widget);
		createFeatures(persistenceManager, model,widget);
        persistenceManager.save(widget);
		createAccessRequests(persistenceManager, model, widget, grantAccessRequests);
		
    _logger.info("'"+model.getLocalName("en") +"' - " + "Widget was successfully imported into the system as "+widget.getLocalName("en"));
    
		return widget;	       
	}

	private static IWidget createWidget(IPersistenceManager persistenceManager, W3CWidget model){
		IWidget widget;
		widget = persistenceManager.newInstance(IWidget.class);		
		widget.setDir(model.getDir());
		widget.setLang(model.getLang());
		widget.setDefaultLocale(model.getDefaultLocale());
		widget.setIdentifier(model.getIdentifier());
		widget.setHeight(model.getHeight());
		widget.setWidth(model.getWidth());
		widget.setVersion(model.getVersion());
		widget.setUpdateLocation(model.getUpdateLocation());
		return widget;
	}
	
	private static void createAuthor(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
	   if (model.getAuthor() != null){
	      IAuthor author = persistenceManager.newInstance(IAuthor.class);
	      author.setAuthorName(model.getAuthor().getAuthorName());
	      author.setEmail(model.getAuthor().getEmail());
	      author.setHref(model.getAuthor().getHref());
	      author.setDir(model.getAuthor().getDir());
	      author.setLang(model.getAuthor().getLang());
	      widget.setAuthor(author);
	    }
	}

	private static void createStartFiles(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for (IContent page:model.getContentList()){
			IContent start = persistenceManager.newInstance(IContent.class);
			start.setCharSet(page.getCharSet());
			start.setLang(page.getLang());
			start.setSrc(page.getSrc());
            widget.getContentList().add(start);
		}
	}

	private static void createNames(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for (IName name:model.getNames()){
			IName widgetName = persistenceManager.newInstance(IName.class);
			widgetName.setLang(name.getLang());
			widgetName.setDir(name.getDir());
			widgetName.setName(name.getName());
			widgetName.setShort(name.getShort());
            widget.getNames().add(widgetName);
            persistenceManager.save(widget);
		}
	}

	private static void createDescriptions(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for (IDescription desc:model.getDescriptions()){
			IDescription widgetDesc = persistenceManager.newInstance(IDescription.class);
			widgetDesc.setDescription(desc.getDescription());
			widgetDesc.setLang(desc.getLang());
			widgetDesc.setDir(desc.getDir());
            widget.getDescriptions().add(widgetDesc);
		} 
	}

	private static void createIcons(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(IIcon icon: model.getIcons()){
            IIcon widgetIcon = persistenceManager.newInstance(IIcon.class);
            widgetIcon.setSrc(icon.getSrc());
            widgetIcon.setHeight(icon.getHeight());
            widgetIcon.setWidth(icon.getWidth());
            widgetIcon.setLang(icon.getLang());
            widget.getIcons().add(widgetIcon);
		}
	}

	private static void createLicenses(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(ILicense licenseModel: model.getLicenses()){
            ILicense license = persistenceManager.newInstance(ILicense.class);
            license.setLicenseText(licenseModel.getLicenseText());
            license.setHref(licenseModel.getHref());
            license.setLang(licenseModel.getLang());
            license.setDir(licenseModel.getDir());
            widget.getLicenses().add(license);
		}
	}

	private static void createPreferences(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(org.apache.wookie.w3c.IPreference pref : model.getPreferences()){
			org.apache.wookie.w3c.IPreference preferenceDefault = persistenceManager.newInstance(org.apache.wookie.w3c.IPreference.class);
			preferenceDefault.setName(pref.getName());
			preferenceDefault.setValue(pref.getValue());
			preferenceDefault.setReadOnly(pref.isReadOnly());
            widget.getPreferences().add(preferenceDefault);
		}
	}

	private static void createFeatures(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(IFeature ofeature: model.getFeatures()){
            IFeature feature = persistenceManager.newInstance(IFeature.class);
			feature.setName(ofeature.getName());
			feature.setRequired(ofeature.isRequired());
            widget.getFeatures().add(feature);
			// now attach all parameters to this feature.
			for(org.apache.wookie.w3c.IParam oparam : ofeature.getParameters()){
	            IParam param = persistenceManager.newInstance(IParam.class);
				param.setName(oparam.getName());
				param.setValue(oparam.getValue());
	            feature.getParameters().add(param);
			}
		}
	}

	/**
	 * Create or update the access policies associated with a widget
	 * @param persistenceManager the persistence manager
	 * @param model the W3C model of the widget 
	 * @param widget the Wookie widget object
	 * @param grantAccessRequests whether access requests are granted by default
	 */
	private static void createAccessRequests(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget, boolean grantAccessRequests){
	  try {
      //
      // Remove any existing access policies
      //
      Policies.getInstance().clearPolicies(widget.getIdentifier());

      //
      // Create access policies for each access request in the widget model
      //
      for(IAccess access:model.getAccessList()){
        Policy policy = new Policy();
        policy.setOrigin(access.getOrigin());
        policy.setScope(widget.getIdentifier());
        policy.setDirective("DENY");
        if (grantAccessRequests){
          policy.setDirective("ALLOW");
          _logger.info("access policy granted for "+widget.getLocalName("en")+" to access "+policy.getOrigin());
        }
        Policies.getInstance().addPolicy(policy);
      }
    } catch (ConfigurationException e) {
      _logger.error("problem with policies configuration", e);
    } catch (Exception e) {
      _logger.error("problem setting policies", e);
    }
	}

	/**
	 * Destroy a widget and all dependent objects and references
	 * @param widget the widget to destroy
	 * @return true if the widget is destroyed successfully
	 */
	public static boolean destroy(IWidget widget, String resourcesPath){
	  

		if(widget==null) return false;
		
		String widgetGuid = widget.getIdentifier();
		String widgetName = widget.getLocalName("en");
		
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		
		// remove any widget instances for this widget
		IWidgetInstance[] instances = persistenceManager.findByValue(IWidgetInstance.class, "widget", widget);	
		for(IWidgetInstance instance : instances){
			
			// Delete all participants and shared data associated with any instances
			//
			// Note that we don't call this within WidgetInstanceFactory.destroy() as 
			// if called in a different context (to remove just one instance) it would 
			// have the side effect of deleting participants and shared data from other instances,
			// not just the one being deleted.
			//
			// Note also that we have to use the instance as the hook for removing participants as there is no
			// specific query for getting participants for a widget.
			//						
			IParticipant[] participants = new SharedContext(instance).getParticipants();
			persistenceManager.delete(participants);
	        ISharedData[] sharedData = new SharedContext(instance).getSharedData();
	        persistenceManager.delete(sharedData);
	        
			// remove any preferences
			IPreference[] preferences = instance.getPreferences().toArray(new IPreference[instance.getPreferences().size()]);// persistenceManager.findByValue(IPreference.class, "widgetInstance", instance);
			persistenceManager.delete(preferences);
			
			// remove the instance
			WidgetInstanceFactory.destroy(instance);
			
		}

		// remove any AccessRequests
    try {
      Policies.getInstance().clearPolicies(widget.getIdentifier());
    } catch (ConfigurationException e) {
      _logger.error("Problem with properties configuration", e);
    }
        
		// remove the widget db entry itself
		persistenceManager.delete(widget);
		// now remove the widget file resources
		WidgetFileUtils.removeWidgetResources(resourcesPath, widgetGuid);
		
    _logger.info("'"+widgetName+"' - " + "Widget was successfully deleted from the system.");
    
		return true;
	} 
	
	/**
	 * Update a Widget with a new model
	 * @param model the updated widget model
	 * @param widget the existing widget
	 * @param grantAccessRequests set to true to grant any access requests defined by the model
	 */
	public static void update( W3CWidget model, IWidget widget,  boolean grantAccessRequests, File zipFile){
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	   
		widget.setDir(model.getDir());
        widget.setLang(model.getLang());
        widget.setDefaultLocale(model.getDefaultLocale());
		
        //
        // Don't override the identifier - see WOOKIE-383
        //
        //widget.setIdentifier(model.getIdentifier());
        
		widget.setHeight(model.getHeight());
		widget.setWidth(model.getWidth());
		widget.setVersion(model.getVersion());
		widget.setUpdateLocation(model.getUpdateLocation());
		
		// Clear old values
		widget.setContentList(null);
		widget.setNames(null);
		widget.setDescriptions(null);
		widget.setLicenses(null);
		widget.setFeatures(null);	
		widget.setIcons(null);
		widget.setPreferences(null);
		// We set this here to ensure widgets already imported in to
		// a 0.9.0 version of wookie get this value set. See WOOKIE-256
	    if(zipFile != null){
	    	widget.setPackagePath(zipFile.getPath());
	    }
		// Set with updated values
		createAuthor(persistenceManager, model,widget);
		createStartFiles(persistenceManager, model,widget);
		createNames(persistenceManager, model,widget);
		createDescriptions(persistenceManager, model,widget);
		createIcons(persistenceManager, model, widget);
		createLicenses(persistenceManager, model,widget);		
		createPreferences(persistenceManager, model,widget);
		createFeatures(persistenceManager, model,widget);
        persistenceManager.save(widget);
		createAccessRequests(persistenceManager, model, widget, grantAccessRequests);

		_logger.info("'"+model.getLocalName("en") +"' - " + "Widget was successfully updated in the system.");
	}

}
