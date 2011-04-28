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
package org.apache.wookie.w3c.updates;

import java.io.IOException;

import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;

/**
 * Utility methods for working with updates
 */
public class UpdateUtils {
	
	/**
	 * Gets an update description document for a widget 
	 * Returns null if there is no update available, or the Update Description Document is not valid
	 * @param widget the widget to check for updates
	 * @return an UpdateDescriptionDocument for an update to the widget, or null if there is no update available
	 */
	public static UpdateDescriptionDocument checkForUpdate(W3CWidget widget){
		return checkForUpdate(widget.getUpdate(), widget.getVersion());
	}
	
	/**
	 * Gets an update description document for a widget using the specified href and version. 
	 * Returns null if there is no update available, or the Update Description Document is not valid
	 * @param href the URL to use to obtain update information for the widget
	 * @param version the current version of the widget
	 * @return an UpdateDescriptionDocument for an update to the widget, or null if there is no update available
	 */
	public static UpdateDescriptionDocument checkForUpdate(String href, String version){
		try {
			// If there is no version for the widget, we must return Null
			if (version == null) return null;
			UpdateDescriptionDocument update = new UpdateDescriptionDocument(href);
			if (update.getVersionTag().equals(version)) return null;
			return update;
		} catch (Exception e) {
			return null;
		}
	}

	
	/**
	 * Gets an updated widget of a widget. Note that by default this allows "unsafe" downloads over plain HTTP connections.
	 * @param factory
	 * @param widget the widget to update
	 * @return the latest version of the widget, or null if there is no updated version available, or the updated widget isn't valid
	 */
	public static W3CWidget getUpdate(W3CWidgetFactory factory, W3CWidget widget){
		try {
			return getUpdate(factory,  widget.getIdentifier(), widget.getUpdate(), widget.getVersion(), false);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Gets the latest version of a widget
	 * @param factory
	 * @param href the location of the widget Update Description Document
	 * @param version the version of the currently installed widget
	 * @param onlyUseHttps only get updates using HTTPS, ignore plain HTTP updates
	 * @return the latest version of the widget, or null if the current version is up to date
	 * @throws Exception 
	 * @throws IOException 
	 * @throws BadManifestException 
	 * @throws BadWidgetZipFileException 
	 * @throws InvalidContentTypeException 
	 */
	public static W3CWidget getUpdate(W3CWidgetFactory factory, String identifier, String href, String version, boolean onlyUseHttps) throws InvalidContentTypeException, BadWidgetZipFileException, BadManifestException, IOException, Exception{
		UpdateDescriptionDocument udd = checkForUpdate(href, version);
		if (udd == null) return null;
		if (onlyUseHttps && !udd.getUpdateSource().getProtocol().equalsIgnoreCase("https")) return  null;
		W3CWidget updatedWidget = factory.parse(udd.getUpdateSource(), false, identifier);
		return updatedWidget;
	}

}
