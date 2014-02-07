/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package org.apache.wookie.services;

import java.util.ServiceLoader;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.services.impl.DefaultWidgetMetadataService;
import org.apache.wookie.w3c.W3CWidget;

/**
 * This is the service provider interface for handling widget metadata. This can be backed with a repository or
 * database of metadata.
 */
public interface WidgetMetadataService {
	
	/**
	 * Import metadata from a manifest
	 * @param manifest the manifest
	 * @param packagePath the path to the .wgt package the manifest was extracted from
	 * @return the widget implementation created from the manifest
	 */
	public abstract IWidget importWidget(W3CWidget manifest, String packagePath);
	
	public abstract IWidget getWidget(String identifier);
	
	public abstract IWidget[] getAllWidgets();
		
	public abstract void removeWidget(IWidget widget);
	
	public abstract void updateWidget(IWidget widget, W3CWidget manifest);
		
	public static class Factory {
		
		private static WidgetMetadataService provider;
		
	    public static WidgetMetadataService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	//
	    	if (provider == null){
	    		ServiceLoader<WidgetMetadataService> ldr = ServiceLoader.load(WidgetMetadataService.class);
	    		for (WidgetMetadataService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = new DefaultWidgetMetadataService();
	    	}
	    	
	    	return provider;
	    }
	}

}
