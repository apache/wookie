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

import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.Participant;
import org.apache.wookie.beans.Preference;
import org.apache.wookie.beans.PreferenceDefault;
import org.apache.wookie.beans.SharedData;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.util.HashGenerator;
import org.apache.wookie.util.LocalizationUtils;
import org.apache.wookie.util.RandomGUID;
import org.apache.wookie.util.opensocial.OpenSocialUtils;

/**
 * A class to create widget instances
 * @author Paul Sharples
 * @author Scott Wilson
 * @version $Id
 *
 */
public class WidgetInstanceFactory{

	static Logger _logger = Logger.getLogger(WidgetInstanceFactory.class.getName());
	
	private Messages localizedMessages;
	private HttpSession session;
	
	boolean showProcess = false;
	
	/**
	 * Return a handle on a Widget Factory localized for the session
	 * @param session
	 * @param localizedMessages
	 * @return
	 */
	public static WidgetInstanceFactory getWidgetFactory(HttpSession session, Messages localizedMessages){
		WidgetInstanceFactory factory = (WidgetInstanceFactory)session.getAttribute(WidgetInstanceFactory.class.getName());				
		if(factory == null){
			factory = new WidgetInstanceFactory();
			factory.localizedMessages = localizedMessages;
			factory.session = session;
			session.setAttribute(WidgetInstanceFactory.class.getName(), factory);
		}
		return factory;
	}
	
	/**
	 * Return the "default widget" instance
	 * @return
	 */
	public static WidgetInstance defaultInstance(String locale){
		WidgetInstance instance = new WidgetInstance();
		instance.setWidget(Widget.findDefaultByType("unsupported")); //$NON-NLS-1$
		instance.setIdKey("0000");
		instance.setLang(locale);
		instance.setOpensocialToken("");
		return instance;
	}
	
	/**
	 * Create a new widget instance with the given parameters
	 * @param session
	 * @param apiKey
	 * @param userId
	 * @param sharedDataKey
	 * @param serviceType
	 * @param widgetId
	 * @param localizedMessages
	 * @return
	 */
	public WidgetInstance newInstance(String apiKey, String userId, String sharedDataKey, String serviceType, String widgetId, String lang){
		try {
			Widget widget;
			WidgetInstance widgetInstance;
			// Widget ID or Widget Type?
			if (widgetId != null){
				widget = Widget.findByGuid(widgetId);
			} 
			else {
				// does this type of widget exist?
				widget = Widget.findDefaultByType(serviceType);				
			}
			// Unsupported
			if (widget == null) return null;

			// generate a nonce
			RandomGUID r = new RandomGUID();
			String nonce = "nonce-" + r.toString();				 //$NON-NLS-1$

			// now use SHA hash on the nonce				
			String hashKey = HashGenerator.getInstance().encrypt(nonce);	

			// get rid of any chars that might upset a url...
			hashKey = hashKey.replaceAll("=", ".eq."); //$NON-NLS-1$ //$NON-NLS-2$
			hashKey = hashKey.replaceAll("\\?", ".qu."); //$NON-NLS-1$ //$NON-NLS-2$
			hashKey = hashKey.replaceAll("&", ".am."); //$NON-NLS-1$ //$NON-NLS-2$
			hashKey = hashKey.replaceAll("\\+", ".pl."); //$NON-NLS-1$ //$NON-NLS-2$
            hashKey = hashKey.replaceAll("/", ".sl."); //$NON-NLS-1$ //$NON-NLS-2$

			Configuration properties = (Configuration) session.getServletContext().getAttribute("opensocial"); //$NON-NLS-1$
			
			widgetInstance = addNewWidgetInstance(apiKey, userId, sharedDataKey, widget, nonce, hashKey, properties, lang);
			return widgetInstance;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Create a new widget instance object, populate its default values, and save it.
	 * @param api_key
	 * @param userId
	 * @param sharedDataKey
	 * @param widget
	 * @param nonce
	 * @param idKey
	 * @param properties
	 * @return
	 */
	private WidgetInstance addNewWidgetInstance(String api_key, String userId, String sharedDataKey, Widget widget, String nonce, String idKey, Configuration properties, String lang) {		
		WidgetInstance widgetInstance = new WidgetInstance();
		widgetInstance.setUserId(userId);
		widgetInstance.setSharedDataKey(sharedDataKey);
		widgetInstance.setIdKey(idKey);
		widgetInstance.setNonce(nonce);
		widgetInstance.setApiKey(api_key);
		if (LocalizationUtils.isValidLanguageTag(lang)) widgetInstance.setLang(lang);
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
	
	/**
	 * Destroy a widget instance and all references to it
	 * @param instance
	 */
	public static void destroy(WidgetInstance instance){
		SharedData.delete(SharedData.findByValue("widgetInstance", instance));
		Preference.delete(Preference.findByValue("widgetInstance", instance));
		Participant.delete(Participant.findByValue("widgetInstance", instance));
		instance.delete();
	}

}

