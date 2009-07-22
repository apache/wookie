/**
 * 
 */
package org.apache.wookie.beans;


/**
 * @author scott
 *
 */
public class Token extends AbstractKeyBean<Token> {
	
	private static final long serialVersionUID = 7599997391757753291L;
	
	private String requestUrl;
	private String accessUrl;
	private String authzUrl;
	private String requestToken;
	private String accessToken;
	private String tokenSecret;
	private WidgetInstance widgetInstance;
	
	/**
	 * @return the requestUrl
	 */
	public String getRequestUrl() {
		return requestUrl;
	}
	/**
	 * @param requestUrl the requestUrl to set
	 */
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	/**
	 * @return the accessUrl
	 */
	public String getAccessUrl() {
		return accessUrl;
	}
	/**
	 * @param accessUrl the accessUrl to set
	 */
	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}
	/**
	 * @return the authzUrl
	 */
	public String getAuthzUrl() {
		return authzUrl;
	}
	/**
	 * @param authzUrl the authzUrl to set
	 */
	public void setAuthzUrl(String authzUrl) {
		this.authzUrl = authzUrl;
	}
	/**
	 * @return the requestToken
	 */
	public String getRequestToken() {
		return requestToken;
	}
	/**
	 * @param requestToken the requestToken to set
	 */
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}
	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	/**
	 * @return the tokenSecret
	 */
	public String getTokenSecret() {
		return tokenSecret;
	}
	/**
	 * @param tokenSecret the tokenSecret to set
	 */
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	/**
	 * @return the widgetInstance
	 */
	public WidgetInstance getWidgetInstance() {
		return widgetInstance;
	}
	/**
	 * @param widgetInstance the widgetInstance to set
	 */
	public void setWidgetInstance(WidgetInstance widgetInstance) {
		this.widgetInstance = widgetInstance;
	}

	// Active Record
	
	public static Token[] findByValue(String key, Object value){
		return (Token[]) findByValue(Token.class, key, value);
	}

}
