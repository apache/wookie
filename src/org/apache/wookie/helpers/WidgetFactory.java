/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.apache.wookie.helpers;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.Preference;
import org.apache.wookie.beans.PreferenceDefault;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.controller.WidgetInstancesController;
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

		// add in basic widget data as preferences
		//TODO setPreference(widgetInstance, "viewMode", String.valueOf(widget)); //$NON-NLS-1$
		setPreference(widgetInstance, "locale", LocaleHandler.getInstance().getDefaultLocale().getLanguage()); //$NON-NLS-1$
		setPreference(widgetInstance, "identifier", String.valueOf(widget.getGuid()));	//$NON-NLS-1$
		setPreference(widgetInstance, "authorInfo", String.valueOf(widget.getWidgetAuthor()));	//$NON-NLS-1$
		//TODO setPreference(widgetInstance, "authorEmail", String.valueOf(widget.getWidth()));//$NON-NLS-1$
		//TODO setPreference(widgetInstance, "authorHref", String.valueOf(widget.getHeight()));			//$NON-NLS-1$
		setPreference(widgetInstance, "name", String.valueOf(widget.getWidgetTitle()));//$NON-NLS-1$
		setPreference(widgetInstance, "description", String.valueOf(widget.getWidgetDescription()));//$NON-NLS-1$	
		setPreference(widgetInstance, "version", widget.getVersion());//$NON-NLS-1$
		setPreference(widgetInstance, "width", String.valueOf(widget.getWidth()));//$NON-NLS-1$
		setPreference(widgetInstance, "height", String.valueOf(widget.getHeight()));//$NON-NLS-1$

		// add in the sharedDataKey as a preference so that a widget can know
		// what sharedData event to listen to later
		setPreference(widgetInstance, "sharedDataKey", sharedDataKey);//$NON-NLS-1$

		// add in widget defaults
		PreferenceDefault[] prefs = PreferenceDefault.findByValue("widget", widget);	
		if (prefs == null) return null;
		for (PreferenceDefault pref: prefs){
			setPreference(widgetInstance, pref.getPreference(), pref.getValue());
		}	
		return widgetInstance;
	}

	/**
	 * Initialize a preference for the instance
	 * @param instance
	 * @param key
	 * @param value
	 */
	private void setPreference(WidgetInstance widgetInstance, String key, String value){
		Preference pref = new Preference();
		pref.setWidgetInstance(widgetInstance);
		pref.setDkey(key);				
		pref.setDvalue(value);
		pref.save();	
	}

}

