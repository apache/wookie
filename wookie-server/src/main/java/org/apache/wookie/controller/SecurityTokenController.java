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
package org.apache.wookie.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.auth.AuthTokenUtils;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ServiceUnavailableException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;

/**
 * API called by widgets to refresh an authtoken. Called periodically from wookie.js
 */
public class SecurityTokenController extends Controller {

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#create(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected boolean create(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws ResourceDuplicationException,
			InvalidParametersException, UnauthorizedAccessException,
			ServiceUnavailableException {
		
		//
		// Get the AuthToken from the request
		//
		AuthToken oldToken = (AuthToken) request.getAttribute("org.apache.wookie.auth.AuthToken");
		if (oldToken == null) throw new UnauthorizedAccessException();
		
		//
		// Create a new AuthToken. The new token has a new
		// TTL expiry.
		//
		AuthToken newToken = AuthToken.STANDARD_LIFESPAN_TOKEN(oldToken);
		
		try {
			String securityToken = AuthTokenUtils.encryptAuthToken(newToken);
			returnJson("{\"token\":\""+securityToken+"\"}", response);
			return true;
		} catch (Exception e) {
			throw new ServiceUnavailableException(e);
		}
		
	}
	
	
	
	

}
