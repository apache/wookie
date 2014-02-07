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
package org.apache.wookie.services.impl;

import java.util.HashMap;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.services.WidgetMetadataService;
import org.apache.wookie.w3c.W3CWidget;

/**
 * Default implementation of a widget metadata service. This is backed by a hashmap
 * in memory.
 */
public class DefaultWidgetMetadataService implements WidgetMetadataService {
	
	private HashMap<String, IWidget> widgets;
	
	public DefaultWidgetMetadataService(){
		this.widgets = new HashMap<String, IWidget>();
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.services.WidgetMetadataService#removeWidget(org.apache.wookie.beans.IWidget)
	 */
	@Override
	public void removeWidget(IWidget widget) {
		widgets.remove(widget.getIdentifier());
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.services.WidgetMetadataService#updateWidget(java.lang.String, org.apache.wookie.beans.IWidget)
	 */
	@Override
	public void updateWidget(IWidget widget, W3CWidget manifest) {
		IWidget newWidget = createWidget(manifest, widget.getPackagePath());
		widgets.put(widget.getIdentifier(), newWidget);
	}

	@Override
	public IWidget importWidget(W3CWidget manifest, String packagePath) {
		IWidget widget = createWidget(manifest, packagePath);
		widgets.put(widget.getIdentifier(), widget);
		return widget;
	}

	@Override
	public IWidget getWidget(String identifier) {
		return widgets.get(identifier);
	}

	@Override
	public IWidget[] getAllWidgets() {
		return widgets.values().toArray(new IWidget[widgets.size()]);		
	}
	
	private IWidget createWidget(W3CWidget manifest, String packagePath) {
		DefaultWidgetImpl widget = new DefaultWidgetImpl();
		widget.setPackagePath(packagePath);
		widget.setAuthor(manifest.getAuthor());
		widget.setContentList(manifest.getContentList());
		widget.setDefaultLocale(manifest.getDefaultLocale());
		widget.setDescriptions(manifest.getDescriptions());
		widget.setDir(manifest.getDir());
		widget.setFeatures(manifest.getFeatures());
		widget.setHeight(manifest.getHeight());
		widget.setIcons(manifest.getIcons());
		widget.setIdentifier(manifest.getIdentifier());
		widget.setLang(manifest.getLang());
		widget.setLicenses(manifest.getLicenses());
		widget.setNames(manifest.getNames());
		widget.setPreferences(manifest.getPreferences());
		widget.setUpdateLocation(manifest.getUpdateLocation());
		widget.setVersion(manifest.getVersion());
		widget.setWidth(manifest.getWidth());
		
		return widget;
	}

}
