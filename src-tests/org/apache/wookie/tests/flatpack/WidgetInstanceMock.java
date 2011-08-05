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
package org.apache.wookie.tests.flatpack;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IToken;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;

/**
 * Mock used for simulating a Widget Instance in tests
 */
public class WidgetInstanceMock implements IWidgetInstance {
	
	private Object id;
	private String lang;
	private IWidget widget;
	private String shareddatakey;
	private boolean hidden;
	private String idkey;
	private String apikey;
	private String ostoken;
	private String userid;
	
	private ArrayList<ISharedData> sharedData;
	private ArrayList<IPreference> preferences;
	
	public WidgetInstanceMock(){
		sharedData = new ArrayList<ISharedData>();
		preferences = new ArrayList<IPreference>();
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IBean#getId()
	 */
	public Object getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.ILocalizedElement#getLang()
	 */
	public String getLang() {
		return lang;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getWidget()
	 */
	public IWidget getWidget() {
		return widget;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setWidget(org.apache.wookie.beans.IWidget)
	 */
	public void setWidget(IWidget widget) {
		this.widget = widget;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setLang(java.lang.String)
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getSharedDataKey()
	 */
	public String getSharedDataKey() {
		return shareddatakey;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setSharedDataKey(java.lang.String)
	 */
	public void setSharedDataKey(String sharedDataKey) {
		shareddatakey = sharedDataKey;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#isHidden()
	 */
	public boolean isHidden() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setHidden(boolean)
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getIdKey()
	 */
	public String getIdKey() {
		return idkey;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setIdKey(java.lang.String)
	 */
	public void setIdKey(String idkey) {
		this.idkey = idkey;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getApiKey()
	 */
	public String getApiKey() {
		return apikey;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setApiKey(java.lang.String)
	 */
	public void setApiKey(String apikey) {
		this.apikey = apikey;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getOpensocialToken()
	 */
	public String getOpensocialToken() {
		return ostoken;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setOpensocialToken(java.lang.String)
	 */
	public void setOpensocialToken(String opensocialToken) {
		this.ostoken = opensocialToken;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getNonce()
	 */
	public String getNonce() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setNonce(java.lang.String)
	 */
	public void setNonce(String nonce) {
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#isShown()
	 */
	public boolean isShown() {
		return !hidden;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setShown(boolean)
	 */
	public void setShown(boolean shown) {
		this.hidden = !shown;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#isUpdated()
	 */
	public boolean isUpdated() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setUpdated(boolean)
	 */
	public void setUpdated(boolean updated) {
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getUserId()
	 */
	public String getUserId() {
		return userid;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setUserId(java.lang.String)
	 */
	public void setUserId(String userId) {
		userid = userId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#isLocked()
	 */
	public boolean isLocked() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getPreferences()
	 */
	public Collection<IPreference> getPreferences() {
		return preferences;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setPreferences(java.util.Collection)
	 */
	public void setPreferences(Collection<IPreference> preferences) {
		this.preferences.clear();
		this.preferences.addAll(preferences);
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getTokens()
	 */
	public Collection<IToken> getTokens() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#setTokens(java.util.Collection)
	 */
	public void setTokens(Collection<IToken> tokens) {
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getSharedData()
	 */
	public ISharedData[] getSharedData() {
		return this.sharedData.toArray(new ISharedData[sharedData.size()]);
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getSharedData(java.lang.String)
	 */
	public ISharedData getSharedData(String name) {
		for (ISharedData data:this.sharedData){
			if (data.getDkey().equals(name)) return data;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.beans.IWidgetInstance#getPreference(java.lang.String)
	 */
	public IPreference getPreference(String key) {
		for (IPreference pref: this.preferences){
			if (pref.getDkey().equals(key)){
				return pref;
			}
		}
		return null;
	}

}
