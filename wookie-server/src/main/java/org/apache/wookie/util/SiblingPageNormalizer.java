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
package org.apache.wookie.util;

import java.util.HashMap;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.helpers.SharedDataHelper;
import org.directwebremoting.impl.DefaultPageNormalizer;

/**
 * An implementation of PageNormalizer that supports the identification
 * of sibling widget instances by the use of the API key and Shared Data Key
 * properties of widget instances. This is used to enable notifications sent
 * by widget instances to only be propagated to their siblings
 * @author scott
 *
 */
public class SiblingPageNormalizer extends DefaultPageNormalizer implements
org.directwebremoting.extend.PageNormalizer {

	private HashMap<String, String> parmsMap;

	public SiblingPageNormalizer() {
		super();
	}

	/**
	 * Return the normalized (sibling-friendly) URI for a given widget instance
	 * @param instance the instance
	 * @return the normalized URI of the widget instance
	 */
	public String getNormalizedPage(IWidgetInstance instance){
		
		return super.normalizePage(IWidget.Utilities.getUrl(instance.getWidget(), instance.getLang()))+"?"+instance.getApiKey()+"="+SharedDataHelper.getInternalSharedDataKey(instance);
	}

	/**
	 * Note this method is called VERY often and needs to be fast
	 */
	/* (non-Javadoc)
	 * @see org.directwebremoting.impl.DefaultPageNormalizer#normalizePage(java.lang.String)
	 */
	@Override
	public String normalizePage(String page) {
		setNormalizeIncludesQueryString(true);
		if (!page.contains("?")) return super.normalizePage(page);
		String[] pages = page.split("\\?");
		if (pages.length < 2) return super.normalizePage(page);
		initMap(pages[1]);
		if (!parmsMap.containsKey("idkey")) return super.normalizePage(page);

		// If the page contains an IdKey, Lets and make sure we include the
		// API key and Shared Data Key: in combination with
		// the Widget URL it uniquely identifies sibling instances
		
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey((String)parmsMap.get("idkey"));
		// Invalid instance
		if(widgetInstance==null) return super.normalizePage(page);

		// Strip off query and add on API key and shared data key
		setNormalizeIncludesQueryString(false);
		page = super.normalizePage(page);
		page += "?"+widgetInstance.getApiKey()+"="+SharedDataHelper.getInternalSharedDataKey(widgetInstance);
		setNormalizeIncludesQueryString(true);
		return page;
	}

	private void initMap(String search) {
		parmsMap = new HashMap<String,String>();
		String params[] = search.split("&");

		for (String param : params) {
			String temp[] = param.split("=");
			try {
				parmsMap.put(temp[0].toLowerCase(), java.net.URLDecoder.decode(temp[1], "UTF-8"));
			} catch (Exception e) {
				//swallow errors
			}
		}
	}


}
