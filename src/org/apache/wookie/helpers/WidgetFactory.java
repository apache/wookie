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

import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wookie.beans.AccessRequest;
import org.apache.wookie.beans.Description;
import org.apache.wookie.beans.Feature;
import org.apache.wookie.beans.License;
import org.apache.wookie.beans.Name;
import org.apache.wookie.beans.Param;
import org.apache.wookie.beans.PreferenceDefault;
import org.apache.wookie.beans.StartFile;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetDefault;
import org.apache.wookie.beans.WidgetIcon;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.beans.WidgetType;
import org.apache.wookie.manifestmodel.IAccessEntity;
import org.apache.wookie.manifestmodel.IContentEntity;
import org.apache.wookie.manifestmodel.IDescriptionEntity;
import org.apache.wookie.manifestmodel.IFeatureEntity;
import org.apache.wookie.manifestmodel.IIconEntity;
import org.apache.wookie.manifestmodel.ILicenseEntity;
import org.apache.wookie.manifestmodel.IManifestModel;
import org.apache.wookie.manifestmodel.INameEntity;
import org.apache.wookie.manifestmodel.IParamEntity;
import org.apache.wookie.manifestmodel.IPreferenceEntity;

/**
 * Factory for creating and destroying Widgets
 */
public class WidgetFactory {
	static Logger _logger = Logger.getLogger(WidgetFactory.class.getName());

	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @param grantAccessRequests whether to automatically grant access requests for the widget
	 * @return the widget
	 */
	public static Widget addNewWidget(IManifestModel model, boolean grantAccessRequests) {
		return addNewWidget(model,null, grantAccessRequests);
	}

	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @return the widget
	 */
	public static Widget addNewWidget(IManifestModel model) {
		return addNewWidget(model,null,false);
	}

	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @param widgetTypes the types to allocate the widget to
	 * @return the widget
	 */
	public static Widget addNewWidget(IManifestModel model,String[] widgetTypes) {
		return addNewWidget(model,widgetTypes,false);
	}	


	/**
	 * Constructs a new Widget and persists it and all dependent objects
	 * @param model the model for the widget
	 * @param widgetTypes the types to set for the widget
	 * @param grantAccessRequests whether to grant access requests created for the widget
	 * @return the widget
	 */
	public static Widget addNewWidget(IManifestModel model, String[] widgetTypes, boolean grantAccessRequests) {	
		Widget widget = createWidget(model);
		createTypes(widgetTypes, widget);
		createStartFiles(model,widget);
		createNames(model,widget);
		createDescriptions(model,widget);
		createIcons(model, widget);
		createLicenses(model,widget);		
		createPreferences(model,widget);
		createFeatures(model,widget);
		createAccessRequests(model, widget, grantAccessRequests);
		return widget;	       
	}

	private static Widget createWidget(IManifestModel model){
		Widget widget;
		widget = new Widget();												
		widget.setWidgetAuthor(model.getAuthor());
		widget.setWidgetAuthorEmail(model.getAuthorEmail());
		widget.setWidgetAuthorHref(model.getAuthorHref());
		widget.setGuid(model.getIdentifier());
		widget.setHeight(model.getHeight());
		widget.setWidth(model.getWidth());
		widget.setVersion(model.getVersion());
		widget.save();	
		return widget;
	}

	@SuppressWarnings("unchecked")
	private static void createTypes(String[] widgetTypes, Widget widget){
		WidgetType widgetType;
		if (widgetTypes!=null){
			for(int i=0;i<widgetTypes.length;i++){
				widgetType = new WidgetType();
				widgetType.setWidgetContext(widgetTypes[i]);
				widgetType.setWidget(widget);
				widget.getWidgetTypes().add(widgetType);
				widgetType.save();
			}
		}
	}

	private static void createStartFiles(IManifestModel model, Widget widget){
		for (IContentEntity page:model.getContentList()){
			StartFile start = new StartFile();
			start.setCharset(page.getCharSet());
			start.setLang(page.getLang());
			start.setUrl(page.getSrc());
			start.setWidget(widget);
			start.save();
		}
	}

	private static void createNames(IManifestModel model, Widget widget){
		for (INameEntity name:model.getNames()){
			Name widgetName = new Name();
			widgetName.setLang(name.getLang());
			widgetName.setDir(name.getDir());
			widgetName.setName(name.getName());
			widgetName.setShortName(name.getShort());
			widgetName.setWidget(widget);
			widgetName.save();
		}
	}

	private static void createDescriptions(IManifestModel model, Widget widget){
		for (IDescriptionEntity desc:model.getDescriptions()){
			Description widgetDesc = new Description();
			widgetDesc.setContent(desc.getDescription());
			widgetDesc.setLang(desc.getLang());
			widgetDesc.setDir(desc.getDir());
			widgetDesc.setWidget(widget);
			widgetDesc.save();
		}
	}

	private static void createIcons(IManifestModel model, Widget widget){
		for(IIconEntity icon: model.getIconsList()){
			WidgetIcon widgetIcon = new WidgetIcon(icon.getSrc(),icon.getHeight(),icon.getWidth(),icon.getLang(), widget);
			widgetIcon.save();
		}
	}

	private static void createLicenses(IManifestModel model, Widget widget){
		for(ILicenseEntity licenseModel: model.getLicensesList()){
			License license = new License(licenseModel.getLicenseText(),licenseModel.getHref(), licenseModel.getLang(), licenseModel.getDir(), widget);
			license.save();
		}
	}

	private static void createPreferences(IManifestModel model, Widget widget){
		for(IPreferenceEntity prefEntity : model.getPrefences()){
			PreferenceDefault prefenceDefault = new PreferenceDefault();
			prefenceDefault.setPreference(prefEntity.getName());
			prefenceDefault.setValue(prefEntity.getValue());
			prefenceDefault.setReadOnly(prefEntity.isReadOnly());
			prefenceDefault.setWidget(widget);
			prefenceDefault.save();
		}
	}

	private static void createFeatures(IManifestModel model, Widget widget){
		for(IFeatureEntity featureEntity: model.getFeatures()){
			Feature feature = new Feature();
			feature.setFeatureName(featureEntity.getName());
			feature.setRequired(featureEntity.isRequired());
			feature.setWidget(widget);
			feature.save();			
			// now attach all parameters to this feature.
			for(IParamEntity paramEntity : featureEntity.getParams()){
				Param param = new Param();
				param.setParameterName(paramEntity.getName());
				param.setParameterValue(paramEntity.getValue());
				param.setParentFeature(feature);
				param.save();
			}
		}
	}

	private static void createAccessRequests(IManifestModel model, Widget widget, boolean grantAccessRequests){
		for(IAccessEntity accessEntity:model.getAccessList()){
			AccessRequest acc = new AccessRequest();
			acc.setOrigin(accessEntity.getOrigin());
			acc.setSubdomains(accessEntity.hasSubDomains());
			acc.setWidget(widget);
			acc.setGranted(grantAccessRequests);
			if (grantAccessRequests){
				_logger.info("access policy granted for "+widget.getWidgetTitle("en")+" to access "+acc.getOrigin());
			}
			acc.save();
		}
	}

	/**
	 * Destroy a widget and all dependent objects and references
	 * @param id the id of the widget
	 * @return true if the widget is destroyed successfully
	 */
	public static boolean destroy(int id){
		Widget widget = Widget.findById(Integer.valueOf(id));
		return destroy(widget);
	}

	/**
	 * Destroy a widget and all dependent objects and references
	 * @param widget the widget to destroy
	 * @return true if the widget is destroyed successfully
	 */
	public static boolean destroy(Widget widget){

		if(widget==null) return false;
		
		// remove any defaults for this widget
		WidgetDefault[] widgetDefault = WidgetDefault.findByValue("widgetId", widget.getId());
		if (widgetDefault.length == 1) widgetDefault[0].delete();
		
		// remove any widget instances for this widget
		WidgetInstance[] instances = WidgetInstance.findByValue("widget", widget);	
		for(WidgetInstance instance : instances){
			WidgetInstanceFactory.destroy(instance);
		}
		
		// remove any widget types for this widget
		Set<?> types = widget.getWidgetTypes();
		WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
		for(int j=0;j<widgetTypes.length;++j){	
			widgetTypes[j].delete();
		}

		// remove PreferenceDefaults
		PreferenceDefault.delete(PreferenceDefault.findByValue("widget", widget));

		// remove Features
		for(Feature feature :Feature.findByValue("widget", widget)){
			Param.delete(Param.findByValue("parentFeature", feature));
			feature.delete();
		}
		
		// remove any AccessRequests
		AccessRequest.delete(AccessRequest.findByValue("widget", widget));

		// remove the widget itself
		widget.delete();
		return true;
	} 

}
