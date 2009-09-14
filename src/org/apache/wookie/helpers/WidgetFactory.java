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

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.Preference;
import org.apache.wookie.beans.PreferenceDefault;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.server.LocaleHandler;
import org.apache.wookie.util.opensocial.OpenSocialUtils;

/**
 * A class to create widget instances
 * @author Paul Sharples
 * @author Scott Wilson
 * @version $Id
 *
 */
public class WidgetFactory{

	static Logger _logger = Logger.getLogger(WidgetFactory.class.getName());
	
	/**
	 * keep this here so that all subclasses can access it
	 */
	protected Messages localizedMessages;
	
	boolean showProcess = false;

	public WidgetFactory(Messages localizedMessages) {
		this.localizedMessages = localizedMessages;	
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.manager.IWidgetServiceManager#addNewWidgetInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.apache.wookie.beans.Widget, java.lang.String, java.lang.String)
	 */
	public WidgetInstance addNewWidgetInstance(String api_key, String userId, String sharedDataKey, Widget widget, String nonce, String idKey, Configuration properties) {		
		WidgetInstance widgetInstance = new WidgetInstance();
		widgetInstance.setUserId(userId);
		widgetInstance.setSharedDataKey(sharedDataKey);
		widgetInstance.setIdKey(idKey);
		widgetInstance.setNonce(nonce);
		widgetInstance.setApiKey(api_key);
		// set the defaults widget for this type			
		widgetInstance.setWidget(widget);						
		widgetInstance.setHidden(false);
		widgetInstance.setShown(true);
		widgetInstance.setUpdated(false);
		widgetInstance.setLocked(false);

		// Setup opensocial token if needed
		widgetInstance.setOpensocialToken(""); //$NON-NLS-1$
		if (properties.getBoolean("opensocial.enable")){ //$NON-NLS-1$
			try {
				if (properties.getString("opensocial.token").equals("secure")){ //$NON-NLS-1$ //$NON-NLS-2$
					widgetInstance.setOpensocialToken(OpenSocialUtils.createEncryptedToken(widgetInstance,properties.getString("opensocial.key"), localizedMessages)); //$NON-NLS-1$
				} 
				else {
					widgetInstance.setOpensocialToken(OpenSocialUtils.createPlainToken(widgetInstance, localizedMessages));					
				}
			} catch (Exception e) {
				_logger.error(e.getMessage());
			}
		}

		// Save
		widgetInstance.save();

		// add in the sharedDataKey as a preference so that a widget can know
		// what sharedData event to listen to later
		setPreference(widgetInstance, "sharedDataKey", sharedDataKey, true);//$NON-NLS-1$

		// add in widget defaults
		PreferenceDefault[] prefs = PreferenceDefault.findByValue("widget", widget);	
		if (prefs == null) return null;
		for (PreferenceDefault pref: prefs){
			setPreference(widgetInstance, pref.getPreference(), pref.getValue(),pref.isReadOnly());
		}	
		return widgetInstance;
	}

	/**
	 * Initialize a preference for the instance
	 * @param instance
	 * @param key
	 * @param value
	 */
	private void setPreference(WidgetInstance widgetInstance, String key, String value, boolean readOnly){
		Preference pref = new Preference();
		pref.setWidgetInstance(widgetInstance);
		pref.setDkey(key);				
		pref.setDvalue(value);
		pref.setReadOnly(readOnly);
		pref.save();	
	}

}

