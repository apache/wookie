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
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.proxy.Policies;
import org.apache.wookie.proxy.Policy;
import org.apache.wookie.services.WidgetMetadataService;
import org.apache.wookie.util.WidgetFileUtils;
import org.apache.wookie.w3c.IAccess;
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
		
		//
		// Import the metadata into the repository service
		//
		IWidget widget = WidgetMetadataService.Factory.getInstance().importWidget(model, null);
		
		//
		// Create access requests
		//
		createAccessRequests(model, widget, grantAccessRequests);
		
		_logger.info("'"+model.getLocalName("en") +"' - " + "Widget was successfully imported into the system as "+widget.getLocalName("en"));
    
		return widget;	       
	}

	/**
	 * Create or update the access policies associated with a widget
	 * @param persistenceManager the persistence manager
	 * @param model the W3C model of the widget 
	 * @param widget the Wookie widget object
	 * @param grantAccessRequests whether access requests are granted by default
	 */
	private static void createAccessRequests(W3CWidget model, IWidget widget, boolean grantAccessRequests){
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

		//
		// Remove the metadata from the repository
		//
		WidgetMetadataService.Factory.getInstance().removeWidget(widget);

		//
		// remove any AccessRequests
		//
		try {
			Policies.getInstance().clearPolicies(widget.getIdentifier());
		} catch (ConfigurationException e) {
			_logger.error("Problem with properties configuration", e);
		}
		
		//
		// now remove the widget file resources
		//
		WidgetFileUtils.removeWidgetResources(resourcesPath, widget.getIdentifier());

		_logger.info("'"+widget.getLocalName("en")+"' - " + "Widget was successfully deleted from the system.");

		return true;
	} 
	
	/**
	 * Update a Widget with a new model
	 * @param model the updated widget model
	 * @param widget the existing widget
	 * @param grantAccessRequests set to true to grant any access requests defined by the model
	 */
	public static void update( W3CWidget model, IWidget widget,  boolean grantAccessRequests, File zipFile){
	    
		WidgetMetadataService.Factory.getInstance().updateWidget(widget, model);
		createAccessRequests(model, widget, grantAccessRequests);

		_logger.info("'"+model.getLocalName("en") +"' - " + "Widget was successfully updated in the system.");
	}

}
