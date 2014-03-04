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
package org.apache.wookie.server.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.auth.AuthTokenUtils;
import org.apache.wookie.auth.ExpiredSingleUseTokenCache;
import org.apache.wookie.auth.InvalidAuthTokenException;

/**
 * Security filter for requests to AJAX services by Widgets
 * This is different to security for the REST API from connectors
 * as there is no secret key for HMAC. Instead, a secure token is
 * shared with the widget and used to authenticate requests
 */
public class WidgetAuthorizationFilter implements Filter{
	
	private FilterConfig filterConfig = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		AuthToken authToken = getAuthToken(request);
		
		if (authToken == null){
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
		} 
		
		else 
			
		{
		//
		// Add the decrypted AuthToken object into the request attributes  
		//
		request.setAttribute("org.apache.wookie.auth.AuthToken", authToken);
		chain.doFilter(request, response);
		}
	}
	
	private AuthToken getAuthToken(ServletRequest request){

		//
		// Do we have an idkey parameter containing an access token?
		//
		String idkey = request.getParameter("idkey");
		if (idkey == null || idkey.trim().equals("")){
			return null;
		}

		//
		// Do we have a valid token?
		//
		AuthToken token = null;
		try {
			token = AuthTokenUtils.validateAuthToken(idkey);
		} catch (InvalidAuthTokenException e) {
			return null;
		}	
		if (token == null){
			return null;
		}
		
		//
		// If the token has a single-use flag, we can only accept it once for requesting a new token
		// by sending a POST to /token.
		//
		if (token.isSingleUse()){

			//
			// If the token has been used once already, reject the request
			//
			if (!ExpiredSingleUseTokenCache.getInstance().isValid(idkey)){
				return null;
			} 
			
			//
			// If the token is being used for anything other than requesting a session use token, reject the request
			//
			if (
					!((HttpServletRequest)request).getServletPath().equals("/token") || 
					!((HttpServletRequest)request).getMethod().equalsIgnoreCase("POST")
			   )
			{
				return null;

			}
			
			//
			// Add the token to the expiry cache
			//
			ExpiredSingleUseTokenCache.getInstance().addToken(idkey);
		}
		
		return token;
	}

	@Override
	public void destroy() {
	}

}
