package org.apache.wookie.services.impl;

import java.util.HashMap;

import org.apache.wookie.beans.IOAuthToken;
import org.apache.wookie.services.OAuthService;

public class DefaultOAuthService implements OAuthService {
	
	private HashMap<String, IOAuthToken> tokens;

	@Override
	public IOAuthToken getOAuthToken(String token) {
		return tokens.get(token);
	}

	@Override
	public void deleteOAuthToken(String token) {
		tokens.remove(token);
	}

	@Override
	public IOAuthToken createOAuthToken(String token) {
		return new OAuthToken();
	}

	@Override
	public void updateOAuthToken(String token, IOAuthToken oauthtoken) {
		tokens.put(token, oauthtoken);
	}
	
	private class OAuthToken implements IOAuthToken{
		
		private String authzUrl;
		private String accessToken;
		private String clientId;
		private long expires;

		@Override
		public Object getId() {
			return null;
		}

		@Override
		public String getAuthzUrl() {
			return authzUrl;
		}

		@Override
		public void setAuthzUrl(String authzUrl) {
			this.authzUrl = authzUrl;
		}

		@Override
		public String getAccessToken() {
			return accessToken;
		}

		@Override
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		@Override
		public String getClientId() {
			return clientId;
		}

		@Override
		public void setClientId(String clientId) {
			this.clientId = clientId;
		}

		@Override
		public long getExpires() {
			return expires;
		}

		@Override
		public void setExpires(long expires) {
			this.expires = expires;
		}

		@Override
		public boolean isExpires() {
		    return System.currentTimeMillis() > expires;
		}
		
	}

}
