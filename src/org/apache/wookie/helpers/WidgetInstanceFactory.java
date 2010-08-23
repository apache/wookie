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
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.IPreferenceDefault;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.util.HashGenerator;
import org.apache.wookie.util.opensocial.OpenSocialUtils;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.apache.wookie.w3c.util.RandomGUID;

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
	public static IWidgetInstance defaultInstance(String locale){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance instance = persistenceManager.newInstance(IWidgetInstance.class);
		instance.setWidget(persistenceManager.findWidgetDefaultByType("unsupported")); //$NON-NLS-1$
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
	public IWidgetInstance newInstance(String apiKey, String userId, String sharedDataKey, String serviceType, String widgetId, String lang){
		try {
			IWidget widget;
			IWidgetInstance widgetInstance;
			// Widget ID or Widget Type?
	        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
			if (widgetId != null){
				widget = persistenceManager.findWidgetByGuid(widgetId);
			} 
			else {
				// does this type of widget exist?
				widget = persistenceManager.findWidgetDefaultByType(serviceType);				
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
			
			widgetInstance = addNewWidgetInstance(persistenceManager, apiKey, userId, sharedDataKey, widget, nonce, hashKey, properties, lang);
			return widgetInstance;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Create a new widget instance object, populate its default values, and save it.
	 * @param persistenceManager
	 * @param api_key
	 * @param userId
	 * @param sharedDataKey
	 * @param widget
	 * @param nonce
	 * @param idKey
	 * @param properties
	 * @return
	 */
	private IWidgetInstance addNewWidgetInstance(IPersistenceManager persistenceManager, String api_key, String userId, String sharedDataKey, IWidget widget, String nonce, String idKey, Configuration properties, String lang) {		
		IWidgetInstance widgetInstance = persistenceManager.newInstance(IWidgetInstance.class);
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

		// add in the sharedDataKey as a preference so that a widget can know
		// what sharedData event to listen to later
		setPreference(persistenceManager, widgetInstance, "sharedDataKey", sharedDataKey, true);//$NON-NLS-1$

		// add in widget defaults
		for (IPreferenceDefault pref: widget.getPreferenceDefaults()){
			setPreference(persistenceManager, widgetInstance, pref.getPreference(), pref.getValue(),pref.isReadOnly());
		}	

		// Save
		persistenceManager.save(widgetInstance);

		return widgetInstance;
	}

	/**
	 * Initialize a preference for the instance
     * @param persistenceManager
	 * @param instance
	 * @param key
	 * @param value
	 */
	private void setPreference(IPersistenceManager persistenceManager, IWidgetInstance widgetInstance, String key, String value, boolean readOnly){
		IPreference pref = persistenceManager.newInstance(IPreference.class);
		pref.setDkey(key);				
		pref.setDvalue(value);
		pref.setReadOnly(readOnly);
		widgetInstance.getPreferences().add(pref);
	}
	
	/**
	 * Destroy a widget instance and all references to it
	 * @param instance
	 */
	public static void destroy(IWidgetInstance instance){
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		persistenceManager.delete(instance);
	}

}

