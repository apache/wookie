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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Authorization Filter for API Requests
 */
public class AuthorizationFilter implements Filter {

  static Logger _logger = Logger.getLogger(AuthorizationFilter.class.getName());
  
  private String scheme;
  
  private static final String HMAC_AUTH_SCHEME = "HMAC";
  private static final String API_KEY_SCHEME = "APIKEY";

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException { 
	  scheme = filterConfig.getInitParameter("authorization-scheme");
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
   * javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    //
    // Check for exceptions
    //
    if (isException((HttpServletRequest) request)) {
      chain.doFilter(request, response);
      return;
    }

    //
    // Choose the authorization method required
    //
    boolean isAuthorized = false;
    if (scheme.equals(API_KEY_SCHEME)){
    	isAuthorized = this.isAuthorizedUsingPlainMessaging((HttpServletRequest) request);
    }
    if (scheme.equals(HMAC_AUTH_SCHEME)){
    	isAuthorized = this.isAuthorizedUsingHmac((HttpServletRequest)request);
    }
    
    //
    // return 403 if not authorised, otherwise continue
    //
    if (!isAuthorized) {
      ((HttpServletResponse) response)
          .sendError(HttpServletResponse.SC_FORBIDDEN);
    } else {
      chain.doFilter(request, response);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
  }

  /**
   * Returns true if the request is an exception - i.e. an open request type
   * against one of the mapped servlets
   * 
   * @param request
   * @return true if the request should be allowed as an exception, false if it
   *         should be processed normally
   */
  private boolean isException(HttpServletRequest request) {
	  
	  //
	  // GET /widgets
	  //
	  if (request.getServletPath().equalsIgnoreCase("/widgets")
			  && request.getMethod().equals("GET"))
		  return true;
	  
	  
	  return false;
  }

  /**
   * Checks if the given request is accompanied by a valid API key
   * 
   * @param request
   * @return true if valid, otherwise false
   */
  private boolean isAuthorizedUsingPlainMessaging(HttpServletRequest request) {
    
    //
    // Verify the API key exists
    //
    String key = request.getParameter("api_key");
   
    //
    // No key
    //
    if (key == null || key.trim().equals("")) {
      _logger.info("No API key supplied");
      return false;
    }
    
    //
    // Look up the key
    //
    return isRegistered(key);
  }
  
  private boolean isAuthorizedUsingHmac(HttpServletRequest request){

	  //
	  // Verify the message hash
	  //
	  if (Hmac.isValidSignedRequest(request)){

		  //
		  // if the request contains an API key parameter, ensure it matches the one
		  // used to sign the message
		  //
		  String key = request.getParameter("api_key");
		  if (key != null){
			  if (!key.equals(Hmac.getPublicKey(request))){
				  return false;
			  }
		  }

		  return true;
	  }
	  return false;
  }
  
  /**
   * Looks up an API key
   * @param apiKey
   * @return true if the API key is registered, false otherwise
   */
  private boolean isRegistered(String apiKey){
      if (ApiKeys.getInstance().validate(apiKey)){
        return true;
      } else {
        _logger.info("Invalid API key supplied: " + apiKey);
        return false;      
      }
  }

}
