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

package org.apache.wookie.proxy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.log4j.Logger;
import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.auth.AuthTokenUtils;

/**
 * A web proxy servlet which will translate calls for content and return them as if they came from
 * this domain
 */
public class ProxyServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;
	public static final String UNAUTHORISED_MESSAGE = "Unauthorised";

	static Logger fLogger = Logger.getLogger(ProxyServlet.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dealWithRequest(request, response);	
	}  

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		dealWithRequest(request, response);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		dealWithRequest(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		dealWithRequest(request, response);
	}
	/**
	 * Check the validity of a proxy request, and execute it if it checks out  
	 * @param request
	 * @param response
	 * @param httpMethod
	 * @throws ServletException
	 */
	private void dealWithRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties");

			//
			// Check that the request is coming from the same domain (i.e. from a widget served by this server)
			//
			if (properties.getBoolean("widget.proxy.checkdomain") && !isSameDomain(request)){
				response.sendError(HttpServletResponse.SC_FORBIDDEN,"<error>"+UNAUTHORISED_MESSAGE+"</error>");	
				return;				
			}

			//
			// Check that the request is coming from a valid widget
			//
			AuthToken authToken = null;
			try {
				authToken = AuthTokenUtils.validateAuthToken(request.getParameter("instanceid_key"));
			} catch (Exception e1) {
				if (!isDefaultGadget(request)){
					response.sendError(HttpServletResponse.SC_FORBIDDEN,"<error>"+UNAUTHORISED_MESSAGE+"</error>");	
					return;
				}
			}
			
			//
			// Create the proxy bean for the request
			//
			ProxyURLBean bean;
			try {
				bean = new ProxyURLBean(request);
			} catch (MalformedURLException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
				return;
			}		

			//
			// should we filter urls?
			//
			if (properties.getBoolean("widget.proxy.usewhitelist") && !isAllowed(bean.getNewUrl().toURI(), authToken.getWidgetId())){
				response.sendError(HttpServletResponse.SC_FORBIDDEN,"<error>URL Blocked</error>");
				fLogger.warn("URL " + bean.getNewUrl().toExternalForm() + " Blocked for scope "+ authToken.getWidgetId());
				return;
			}	

			//
			// Create a ProxyClient instance for the request
			//
			ProxyClient proxyclient = new ProxyClient(request);
			ProxyClient.ResponseObject responseObject = null;

			//TODO - find all the links etc & make them absolute - to make request come thru this servlet

			//
			// Execute the request and populate the ResponseObject
			//
			responseObject = proxyclient.execRequest(bean.getNewUrl().toExternalForm(), request, properties);
			
			//
			// Set Status
			//
	        response.setStatus(responseObject.status);
	     
			//
	        // Set Headers
	        //
	        for (Header header:responseObject.headers){
	            if(!header.getName().startsWith("Transfer-Encoding")) // We don't want to add chunked data header - see WOOKIE-403
	                response.setHeader(header.getName(), header.getValue());
	        }
			
			//
			// Set Body
			//
			if(responseObject.body != null && responseObject.body.length > 0){
			  response.getOutputStream().write(responseObject.body);
			}
		}
		catch (Exception ex) {
			try {
				if (ex instanceof AuthenticationException){
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				} else {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
				}
				fLogger.error(ex.getMessage());
			} catch (IOException e) {
				// give up!
				fLogger.error(ex.getMessage());	
				throw new ServletException(e);
			}
		}
	}

	/**
	 * Checks that the request is from the same domain as this service
	 * @param request
	 * @param checkDomain
	 * @return
	 */
	private boolean isSameDomain(HttpServletRequest request){
		String remoteHost = request.getRemoteHost();
		String serverHost = request.getServerName();
		if(remoteHost.equals(serverHost)) return true;
		return false;
	}

	private boolean isDefaultGadget(HttpServletRequest request){
		String instanceId = request.getParameter("instanceid_key");
		// check  if the default Shindig gadget key is being used
		Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("opensocial");
		if (properties.getBoolean("opensocial.enable") && properties.getString("opensocial.proxy.id").equals(instanceId))
			return true;
		return false;
	}

	/**
	 * Check to see if a given url appears in the whitelist
	 * @param aUrl
	 * @return
	 */
	public boolean isAllowed(URI requestedUri, String widgetId){
	  try {
      return Policies.getInstance().validate(requestedUri, widgetId);
    } catch (ConfigurationException e) {
      fLogger.error("Problem with policies configuration", e);
      return false;
    } catch (URISyntaxException e) {
      return false;
    }
	}

	/**
	 * 
	 * A class used to model a url both with and without a proxy address attached to it
	 *
	 */
	private class ProxyURLBean {	

		private URL fNewUrl;

		public ProxyURLBean(HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException{			
			doParse(request);
		}	

		private void doParse(HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException{
			URL proxiedEndPointURL = null;
			String endPointURL = null;

			// Try to find a "url" parameter from the QueryString of the request
			String file = request.getRequestURI();
			if (request.getQueryString() != null) {
				file += '?' + request.getQueryString();
				// build the requested path
				proxiedEndPointURL = new URL(request.getScheme(),request.getServerName(),request.getServerPort() , file);
				// find where the url parameter is ..
				int idx = proxiedEndPointURL.toString().indexOf("url=");
				if(idx>-1){
					// reconstruct the path to be proxied by removing the reference to this servlet
					endPointURL=proxiedEndPointURL.toString().substring(idx+4,proxiedEndPointURL.toString().length());
					// fix initial querystring component - this is a hack for JQuery support. See WOOKIE-118 for
					// more information about this.
					if (endPointURL.contains("&") && !endPointURL.contains("?")){
						endPointURL = endPointURL.replaceFirst("&", "?");
					}
				}
			}

			// try to locate a POST form parameter instead
			if (endPointURL == null)	
				endPointURL=request.getParameter("url");

			// the request didn't contain any params, so throw an exception
			if (endPointURL == null)	
				throw new MalformedURLException("Unable to obtain url from args");

			try {
				fNewUrl = new URL(endPointURL);
			} catch (Exception ex) {
				// try decoding the URL
				fNewUrl = new URL(URLDecoder.decode(endPointURL, "UTF-8"));
			}
		}

		public URL getNewUrl() {
			return fNewUrl;
		}

	}

}

