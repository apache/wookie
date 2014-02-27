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

import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.auth.AuthTokenUtils;
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

		//
		// Do we have an idkey parameter containing an access token?
		//
		String idkey = request.getParameter("idkey");
		if (idkey == null || idkey.trim().equals("")){
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
		}

		//
		// Do we have a valid token?
		//
		AuthToken token = null;
		try {
			token = AuthTokenUtils.decryptAuthToken(idkey);
		} catch (InvalidAuthTokenException e) {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
		}	
		if (token == null){
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
		}
		
		//
		// Add the decrypted AuthToken object into the request attributes  
		//
		request.setAttribute("org.apache.wookie.auth.AuthToken", token);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
