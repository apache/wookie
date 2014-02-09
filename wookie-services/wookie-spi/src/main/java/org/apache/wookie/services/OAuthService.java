package org.apache.wookie.services;

import java.util.ServiceLoader;

import org.apache.wookie.beans.IOAuthToken;

public interface OAuthService {
	
	public abstract IOAuthToken getOAuthToken(String token);
	
	public abstract void deleteOAuthToken(String token);
	
	/**
	 * Create a "blank" token
	 * @param apikey
	 * @param contextid
	 * @param widgetid
	 * @param viewerid
	 * @return
	 */
	public abstract IOAuthToken createOAuthToken(String token);
	
	public abstract void updateOAuthToken(String token, IOAuthToken oauthtoken);
	
	public static class Factory {
		
		private static OAuthService provider;
		
	    public static OAuthService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	//
	    	if (provider == null){
	    		ServiceLoader<OAuthService> ldr = ServiceLoader.load(OAuthService.class);
	    		for (OAuthService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		//provider = new DefaultOAuthService();
	    	}
	    	
	    	return provider;
	    }
	}

}
