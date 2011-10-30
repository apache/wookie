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

import org.apache.log4j.Logger;
import org.apache.wookie.beans.IAccessRequest;
import org.apache.wookie.beans.IAuthor;
import org.apache.wookie.beans.IDescription;
import org.apache.wookie.beans.IFeature;
import org.apache.wookie.beans.ILicense;
import org.apache.wookie.beans.IName;
import org.apache.wookie.beans.IParam;
import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.IPreferenceDefault;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;
import org.apache.wookie.beans.IWidgetIcon;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.IWidgetType;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.w3c.IAccessEntity;
import org.apache.wookie.w3c.IContentEntity;
import org.apache.wookie.w3c.IDescriptionEntity;
import org.apache.wookie.w3c.IFeatureEntity;
import org.apache.wookie.w3c.IIconEntity;
import org.apache.wookie.w3c.ILicenseEntity;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.INameEntity;
import org.apache.wookie.w3c.IParamEntity;
import org.apache.wookie.w3c.IPreferenceEntity;

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
	public static IWidget addNewWidget(W3CWidget model,String[] widgetTypes, File file, boolean grantAccessRequests) {
		IWidget widget = addNewWidget(model,widgetTypes, grantAccessRequests);
		widget.setPackagePath(file.getPath());
		return widget;
	}
	
	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @param grantAccessRequests whether to automatically grant access requests for the widget
	 * @return the widget
	 */
	public static IWidget addNewWidget(W3CWidget model, boolean grantAccessRequests) {
		return addNewWidget(model,null, grantAccessRequests);
	}

	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @return the widget
	 */
	public static IWidget addNewWidget(W3CWidget model) {
		return addNewWidget(model,null,false);
	}

	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @param widgetTypes the types to allocate the widget to
	 * @return the widget
	 */
	public static IWidget addNewWidget(W3CWidget model,String[] widgetTypes) {
		return addNewWidget(model,widgetTypes,false);
	}	


	/**
	 * Constructs a new Widget and persists it and all dependent objects
	 * @param model the model for the widget
	 * @param widgetTypes the types to set for the widget
	 * @param grantAccessRequests whether to grant access requests created for the widget
	 * @return the widget
	 */
	public static IWidget addNewWidget(W3CWidget model, String[] widgetTypes, boolean grantAccessRequests) {
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget widget = createWidget(persistenceManager, model);
		createAuthor(persistenceManager, model, widget);
		createTypes(persistenceManager, widgetTypes, widget);
		createStartFiles(persistenceManager, model,widget);
		createNames(persistenceManager, model,widget);
		createDescriptions(persistenceManager, model,widget);
		createIcons(persistenceManager, model, widget);
		createLicenses(persistenceManager, model,widget);		
		createPreferences(persistenceManager, model,widget);
		createFeatures(persistenceManager, model,widget);
        persistenceManager.save(widget);
		createAccessRequests(persistenceManager, model, widget, grantAccessRequests);
		return widget;	       
	}

	private static IWidget createWidget(IPersistenceManager persistenceManager, W3CWidget model){
		IWidget widget;
		widget = persistenceManager.newInstance(IWidget.class);		
		widget.setDir(model.getDir());
		widget.setLang(model.getLang());
		widget.setDefaultLocale(model.getDefaultLocale());
		widget.setGuid(model.getIdentifier());
		widget.setHeight(model.getHeight());
		widget.setWidth(model.getWidth());
		widget.setVersion(model.getVersion());
		widget.setUpdateLocation(model.getUpdate());
		return widget;
	}
	
	private static void createAuthor(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
	   if (model.getAuthor() != null){
	      IAuthor author = persistenceManager.newInstance(IAuthor.class);
	      author.setAuthor(model.getAuthor().getAuthorName());
	      author.setEmail(model.getAuthor().getEmail());
	      author.setHref(model.getAuthor().getHref());
	      author.setDir(model.getAuthor().getDir());
	      author.setLang(model.getAuthor().getLang());
	      widget.setAuthor(author);
	    }
	}

	private static void createTypes(IPersistenceManager persistenceManager, String[] widgetTypes, IWidget widget){
		IWidgetType widgetType;
		if (widgetTypes!=null){
			for(int i=0;i<widgetTypes.length;i++){
				widgetType = persistenceManager.newInstance(IWidgetType.class);
				widgetType.setWidgetContext(widgetTypes[i]);
				widget.getWidgetTypes().add(widgetType);
			}
		}
	}

	private static void createStartFiles(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for (IContentEntity page:model.getContentList()){
			IStartFile start = persistenceManager.newInstance(IStartFile.class);
			start.setCharset(page.getCharSet());
			start.setLang(page.getLang());
			start.setUrl(page.getSrc());
            widget.getStartFiles().add(start);
		}
	}

	private static void createNames(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for (INameEntity name:model.getNames()){
			IName widgetName = persistenceManager.newInstance(IName.class);
			widgetName.setLang(name.getLang());
			widgetName.setDir(name.getDir());
			widgetName.setName(name.getName());
			widgetName.setShortName(name.getShort());
            widget.getNames().add(widgetName);
		}
	}

	private static void createDescriptions(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for (IDescriptionEntity desc:model.getDescriptions()){
			IDescription widgetDesc = persistenceManager.newInstance(IDescription.class);
			widgetDesc.setContent(desc.getDescription());
			widgetDesc.setLang(desc.getLang());
			widgetDesc.setDir(desc.getDir());
            widget.getDescriptions().add(widgetDesc);
		}
	}

	private static void createIcons(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(IIconEntity icon: model.getIconsList()){
            IWidgetIcon widgetIcon = persistenceManager.newInstance(IWidgetIcon.class);
            widgetIcon.setSrc(icon.getSrc());
            widgetIcon.setHeight(icon.getHeight());
            widgetIcon.setWidth(icon.getWidth());
            widgetIcon.setLang(icon.getLang());
            widget.getWidgetIcons().add(widgetIcon);
		}
	}

	private static void createLicenses(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(ILicenseEntity licenseModel: model.getLicensesList()){
            ILicense license = persistenceManager.newInstance(ILicense.class);
            license.setText(licenseModel.getLicenseText());
            license.setHref(licenseModel.getHref());
            license.setLang(licenseModel.getLang());
            license.setDir(licenseModel.getDir());
            widget.getLicenses().add(license);
		}
	}

	private static void createPreferences(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(IPreferenceEntity prefEntity : model.getPrefences()){
            IPreferenceDefault preferenceDefault = persistenceManager.newInstance(IPreferenceDefault.class);
			preferenceDefault.setPreference(prefEntity.getName());
			preferenceDefault.setValue(prefEntity.getValue());
			preferenceDefault.setReadOnly(prefEntity.isReadOnly());
            widget.getPreferenceDefaults().add(preferenceDefault);
		}
	}

	private static void createFeatures(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(IFeatureEntity featureEntity: model.getFeatures()){
            IFeature feature = persistenceManager.newInstance(IFeature.class);
			feature.setFeatureName(featureEntity.getName());
			feature.setRequired(featureEntity.isRequired());
            widget.getFeatures().add(feature);
			// now attach all parameters to this feature.
			for(IParamEntity paramEntity : featureEntity.getParams()){
	            IParam param = persistenceManager.newInstance(IParam.class);
				param.setParameterName(paramEntity.getName());
				param.setParameterValue(paramEntity.getValue());
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
	  //
	  // Remove any existing access policies
	  //
	  persistenceManager.delete(persistenceManager.findApplicableAccessRequests(widget));
	  
	  //
	  // Create access policies for each access request in the widget model
	  //
		for(IAccessEntity accessEntity:model.getAccessList()){
            IAccessRequest acc = persistenceManager.newInstance(IAccessRequest.class);
			acc.setOrigin(accessEntity.getOrigin());
			acc.setSubdomains(accessEntity.hasSubDomains());
			acc.setGranted(grantAccessRequests);
			acc.setWidget(widget);
			if (grantAccessRequests){
				_logger.info("access policy granted for "+widget.getWidgetTitle("en")+" to access "+acc.getOrigin());
			}
			persistenceManager.save(acc);
		}
	}

	/**
	 * Destroy a widget and all dependent objects and references
	 * @param widget the widget to destroy
	 * @return true if the widget is destroyed successfully
	 */
	public static boolean destroy(IWidget widget){

		if(widget==null) return false;
		
		// remove any defaults for this widget
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetDefault[] widgetDefault = persistenceManager.findByValue(IWidgetDefault.class, "widget", widget);
		if (widgetDefault.length == 1) persistenceManager.delete(widgetDefault[0]);
		
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
        IAccessRequest[] accessRequests = persistenceManager.findApplicableAccessRequests(widget);
        persistenceManager.delete(accessRequests);
        
		// remove the widget itself
		persistenceManager.delete(widget);
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
		widget.setGuid(model.getIdentifier());
		widget.setHeight(model.getHeight());
		widget.setWidth(model.getWidth());
		widget.setVersion(model.getVersion());
		widget.setUpdateLocation(model.getUpdate());
		
		// Clear old values
		widget.setStartFiles(null);
		widget.setNames(null);
		widget.setDescriptions(null);
		widget.setLicenses(null);
		widget.setFeatures(null);	
		widget.setWidgetIcons(null);
		widget.setPreferenceDefaults(null);
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
	}

}
