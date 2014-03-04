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

package org.apache.wookie.auth;

import org.apache.wookie.server.security.ApiKey;
import org.apache.wookie.w3c.util.RandomGUID;

/**
 * An AuthToken used to pass contextual information about an instance of a
 * widget, including the viewer, widget identifier, shared data context, and API
 * key
 * 
 * This implementation is based on the Apache Shindig basic token model
 */
public class AuthToken {

	private ApiKey apiKey;
	private String widgetId;
	private String viewerId;
	private String contextId;
	private String lang;
	private boolean singleUse = false;
	private String nonce;

	public static final int DEFAULT_MAX_TOKEN_TTL = 3600; // 1 hour
	private static final long CLOCK_SKEW_ALLOWANCE = 180; // allow three minutes for clock skew
	private Long expiresAt;
	private int tokenTTL;
	
	/**
	 * Default constructor
	 */
	private AuthToken(){
	}
	
	/**
	 * Create a new single-use AuthToken from an existing
	 * authtoken. This type of token can only be used to 
	 * request a new token.
	 * @return the authtoken
	 */
	public static AuthToken SINGLE_USE_TOKEN(AuthToken oldToken){
		AuthToken authToken = new AuthToken();
		authToken.setExpires(300); // 5 minutes
		authToken.setSingleUse(true);
		authToken.nonce = new RandomGUID(true).toString();
		authToken.setApiKey(oldToken.getApiKeyInstance());
		authToken.setContextId(oldToken.getContextId());
		authToken.setWidgetId(oldToken.getWidgetId());
		authToken.setViewerId(oldToken.getViewerId());
		authToken.setLang(oldToken.getLang());
		return authToken;
	}

	/**
	 * Create a new AuthToken with a 5 minute lifespon.
	 * @return the authtoken
	 */
	public static AuthToken SHORT_LIFESPAN_TOKEN(){
		AuthToken authToken = new AuthToken();
		authToken.nonce = new RandomGUID(true).toString();
		authToken.setExpires(300); // 5 minutes
		return authToken;
	}
	
	/**
	 * Create a new AuthToken with a 5 minute lifespon from an existing
	 * authtoken
	 * @return the authtoken
	 */
	public static AuthToken SHORT_LIFESPAN_TOKEN(AuthToken oldToken){
		AuthToken authToken = new AuthToken();
		authToken.nonce = new RandomGUID(true).toString();
		authToken.setExpires(300); // 5 minutes
		authToken.setApiKey(oldToken.getApiKeyInstance());
		authToken.setContextId(oldToken.getContextId());
		authToken.setWidgetId(oldToken.getWidgetId());
		authToken.setViewerId(oldToken.getViewerId());
		authToken.setLang(oldToken.getLang());
		return authToken;
	}
	
	/**
	 * Create a new AuthToken with a standard lifespon
	 * @return the authtoken
	 */
	public static AuthToken STANDARD_LIFESPAN_TOKEN(){
		AuthToken authToken = new AuthToken();
		authToken.nonce = new RandomGUID(true).toString();
		authToken.setExpires();
		return authToken;
	}
	
	/**
	 * Create a new AuthToken with a standard lifespon from an existing
	 * authtoken
	 * @return the authtoken
	 */
	public static AuthToken STANDARD_LIFESPAN_TOKEN(AuthToken oldToken){
		AuthToken authToken = new AuthToken();
		authToken.nonce = new RandomGUID(true).toString();
		authToken.setExpires();
		authToken.setApiKey(oldToken.getApiKeyInstance());
		authToken.setContextId(oldToken.getContextId());
		authToken.setWidgetId(oldToken.getWidgetId());
		authToken.setViewerId(oldToken.getViewerId());
		authToken.setLang(oldToken.getLang());
		return authToken;
	}
	
	/**
	 * @return The time in seconds since epoc that this token expires or
	 *         <code>null</code> if unknown or indeterminate.
	 */
	Long getExpiresAt(){
		return expiresAt;
	}

	/**
	 * @return true if the token is no longer valid.
	 */
	boolean isExpired(){
		Long expiresAt = getExpiresAt();
		if (expiresAt != null) {
			long maxTime = expiresAt + CLOCK_SKEW_ALLOWANCE;
			long now = System.currentTimeMillis() / 1000;

			if (!(now < maxTime)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the apiKey value
	 */
	public String getApiKey() {
		return apiKey.getValue();
	}
	
	/**
	 * @return the apiKey object
	 */
	public ApiKey getApiKeyInstance() {
		return apiKey;
	}

	/**
	 * @param apiKey
	 *            the apiKey to set
	 */
	public void setApiKey(ApiKey apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the widgetId
	 */
	public String getWidgetId() {
		return widgetId;
	}

	/**
	 * @param widgetId
	 *            the widgetId to set
	 */
	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	/**
	 * @return the viewerId
	 */
	public String getViewerId() {
		return viewerId;
	}

	/**
	 * @param viewerId
	 *            the viewerId to set
	 */
	public void setViewerId(String viewerId) {
		this.viewerId = viewerId;
	}

	/**
	 * @return the contextId
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * @param contextId
	 *            the contextId to set
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang
	 *            the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}


	/**
	 * Compute and set the expiration time for this token using the default TTL.
	 *
	 * @return This security token.
	 * @see #setExpires(int)
	 */
	protected void setExpires() {
		setExpires(DEFAULT_MAX_TOKEN_TTL);
	}

	/**
	 * Compute and set the expiration time for this token using the provided TTL.
	 *
	 * @param tokenTTL the time to live (in seconds) of the token
	 * @return This security token.
	 */
	protected void setExpires(int tokenTTL) {
		this.tokenTTL = tokenTTL;
		setExpiresAt( (System.currentTimeMillis() / 1000) + getMaxTokenTTL());
	}

	/**
	 * Set the expiration time for this token.
	 *
	 * @param expiresAt When this token expires, in seconds since epoch.
	 * @return This security token.
	 */
	protected void setExpiresAt(Long expiresAt) {
		this.expiresAt = expiresAt;
	}

	/**
	 * Returns the maximum allowable time (in seconds) for this token to live. 
	 *
	 * @return Maximum allowable time in seconds for a token to live.
	 */
	protected int getMaxTokenTTL() {
		return this.tokenTTL;
	}
	
	/**
	 * Returns whether this token is a single-use token
	 * @return the singleUse
	 */
	public boolean isSingleUse() {
		return singleUse;
	}

	/**
	 * Set the token as single-use
	 * @param singleUse the singleUse to set
	 */
	public void setSingleUse(boolean singleUse) {
		this.singleUse = singleUse;
	}

	/**
	 * @return the nonce
	 */
	public String getNonce() {
		return nonce;
	}

	/**
	 * @param nonce the nonce to set
	 */
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String token = "<token>";
		token += "<apikey>"+apiKey.getValue()+"</apikey>";
		token += "<widget>"+widgetId + "</widget>";
		token += "<viewer>"+viewerId + "</viewer>";
		token += "<context>" + contextId + "</context>";
		token += "</token>";
		return token;
	}
	
	

}
