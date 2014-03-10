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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.auth.AuthTokenUtils;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.WidgetInstanceHelper;
import org.apache.wookie.helpers.WidgetRuntimeHelper;
import org.apache.wookie.services.PreferencesService;
import org.apache.wookie.services.SharedContextService;
import org.apache.wookie.services.WidgetMetadataService;
import org.apache.wookie.w3c.IContent;
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.w3c.util.LocalizationUtils;

/**
 * REST implementation for widgetInstance
 *
 * POST: creates and returns (or just returns) an instance
 * PUT: stop, resume, or clone an instance
 * (GET: redirect to other actions. Useful for some limited clients)
 *
 */
public class WidgetInstancesController extends Controller {

	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(WidgetInstancesController.class.getName());	
	protected static final String CONTENT_TYPE = "text/xml;charset=\"UTF-8\""; 	 //$NON-NLS-1$
	protected static URL urlWidgetProxyServer = null;	

	
	/// Implementation
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		AuthToken authToken = getAuthTokenFromRequest(request, true);
		
		if (authToken == null){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			//
			// Check the API key matches
			//
			String apiKey = request.getParameter("api_key");
			if (!authToken.getApiKey().equals(apiKey)) throw new UnauthorizedAccessException();
			
			
			//
			// When this API is called, we create a new single-use token to return in the URL. 
			// When the widget is rendered it will request a new standard token via AJAX.
			//
			AuthToken newToken = AuthToken.SINGLE_USE_TOKEN(authToken);
			
			checkProxy(request);
			String url;
			try {
				url = getUrl(request, newToken);
			} catch (Exception e1) {
				throw new IOException(e1);
			}
			
			//
			// If the widget was replaced by the not supported widget, return
			// 404. Otherwise return 200.
			//
			if (newToken.getWidgetId().equals("http://notsupported")){
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
			}

			//
			// Use default sizes where none provided?
			//
			boolean useDefaultSizes = true;
			Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$
			if (properties.containsKey("widget.use_default_sizes")){
				try {
					useDefaultSizes = properties.getBoolean("widget.use_default_sizes");
				} catch (Exception e) {
					useDefaultSizes = true;
				}
			}
			
			//
			// Set default preferences if there are no preferences associated with this session
			//
			IWidget widget = WidgetMetadataService.Factory.getInstance().getWidget(authToken.getWidgetId());
			Collection<IPreference> existingPreferences = PreferencesService.Factory.getInstance().getPreferences(newToken.getApiKey(), newToken.getWidgetId(), newToken.getContextId(), newToken.getViewerId());
			if (existingPreferences == null || existingPreferences.size() == 0){
				for (org.apache.wookie.w3c.IPreference preference: widget.getPreferences()){
					PreferencesService.Factory.getInstance().setPreference(newToken.getApiKey(), newToken.getWidgetId(), newToken.getContextId(), newToken.getViewerId(), preference.getName(), preference.getValue(), preference.isReadOnly());
				}				
			}

			//
			// Return XML or JSON 
			//			
			try {
				switch(format(request)){
				case XML: returnXml(WidgetInstanceHelper.createXMLWidgetInstanceDocument(newToken, url, useDefaultSizes), response); break;
				case JSON: returnJson(WidgetInstanceHelper.toJson(newToken, url, useDefaultSizes), response); break;
				default: returnXml(WidgetInstanceHelper.createXMLWidgetInstanceDocument(newToken, url, useDefaultSizes), response); break;
				}
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

			}

		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#update(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void update(String resourceId, HttpServletRequest request, HttpServletResponse response)
	throws ResourceNotFoundException, InvalidParametersException,
	UnauthorizedAccessException {
	  String requestId = request.getParameter("requestid"); //$NON-NLS-1$
	  if (requestId == null || requestId.equals(""))
	    throw new InvalidParametersException();
	  if(requestId.equals("stopwidget")){ //$NON-NLS-1$
	    doStopWidget(request);
	  } else if(requestId.equals("resumewidget")){ //$NON-NLS-1$
	    doResumeWidget(request);
	  } else if(requestId.equals("clone")){ //$NON-NLS-1$
	    cloneSharedData(request);
	  } else {
	    throw new InvalidParametersException();
	  }

	}

	/**
	 * Locks a widget instance
	 * @param request
	 * @throws InvalidParametersException
	 */
	public static void doStopWidget(HttpServletRequest request) throws InvalidParametersException{
		AuthToken authToken = getAuthTokenFromRequest(request);
		if (authToken == null) throw new InvalidParametersException();
		// TODO
	    // lockWidgetInstance(instance);
	    Notifier.notifyWidgets(request.getSession(), authToken, Notifier.STATE_UPDATED);
	}

	/**
	 * UNlocks a widget insa
	 * @param request
	 * @throws InvalidParametersException
	 */
	public static void doResumeWidget(HttpServletRequest request) throws InvalidParametersException{					
		AuthToken authToken = getAuthTokenFromRequest(request);
		if (authToken == null) throw new InvalidParametersException();
		// TODO
	    // unlockWidgetInstance(instance);
	    Notifier.notifyWidgets(request.getSession(), authToken, Notifier.STATE_UPDATED);
	}
	
	/**
	 * Create a clone of the instance
	 * @param request
	 * @throws InvalidParametersException
	 */
	public static void cloneSharedData(HttpServletRequest request) throws InvalidParametersException{
		AuthToken authToken = getAuthTokenFromRequest(request);	
		if (authToken == null) throw new InvalidParametersException();		

		String sharedDataKey = request.getParameter("shareddatakey");	 //$NON-NLS-1$;	
		String cloneSharedDataKey = request.getParameter("cloneshareddatakey");
		if (sharedDataKey == null || sharedDataKey.trim().equals("") || cloneSharedDataKey == null || cloneSharedDataKey.trim().equals("")){//$NON-NLS-1$ //$NON-NLS-2$
			throw new InvalidParametersException();
		}
		for (ISharedData sharedData : new SharedContext(authToken).getSharedData())
		{	
			SharedContextService.Factory.getInstance().updateSharedData(authToken.getApiKey(), authToken.getWidgetId(), cloneSharedDataKey, sharedData.getDkey(), sharedData.getDvalue(), false);
		}
	}
	
	// Utility methods
	
	/**
	 * Updates the Proxy url if necessary.
	 */
	public static String checkProxy(HttpServletRequest request){
	  // set the proxy url.
	  if(urlWidgetProxyServer==null){
	    Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$
	    String scheme = request.getScheme();
	    String serverName = request.getServerName();
	    int serverPort = request.getServerPort();
	    if (!properties.getString("widget.proxy.scheme").trim().equals("")) scheme = properties.getString("widget.proxy.scheme"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	    if (!properties.getString("widget.proxy.hostname").trim().equals("")) serverName = properties.getString("widget.proxy.hostname"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	    if (!properties.getString("widget.proxy.port").trim().equals("")) serverPort = Integer.parseInt(properties.getString("widget.proxy.port")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	    try {
	      urlWidgetProxyServer = new URL(scheme, serverName, serverPort, WidgetRuntimeHelper.getWebContextPath() + properties.getString("widget.proxy.path"));
	    } catch (MalformedURLException e) {
	      // ignore errors
	    } 
	  }
	  return urlWidgetProxyServer.toExternalForm();
	}
	
	/**
	 * Returns the absolute URL of the widget instance including id key, proxy url and opensocial token 
	 * @param request the current request
	 * @param instance the widget instance
	 * @return the absolute URL
	 * @throws Exception 
	 */
	protected static String getUrl(HttpServletRequest request, AuthToken authToken) throws Exception{
		String url = "";

		IWidget widget = WidgetMetadataService.Factory.getInstance().getWidget(authToken.getWidgetId());
		//
		// Locate the startfile for the Widget Instance
		//
		IContent[] startFiles = widget.getContentList().toArray(new IContent[widget.getContentList().size()]);
        IContent sf = (IContent) LocalizationUtils.getLocalizedElement(startFiles, new String[]{authToken.getLang()}, widget.getDefaultLocale());
    
    //
    // Try default locale if no appropriate localization found
    //
		if (sf == null) sf = (IContent) LocalizationUtils.getLocalizedElement(startFiles, null, widget.getDefaultLocale());
		
		//
		// No start file found, so throw an exception
		//
		if (sf == null) throw new IOException("No start file located for widget "+widget.getIdentifier());
		
		//
		// Get a URL for the start file on this Wookie server
		//
    String path = sf.getSrc();
		URL urlWidget =  getWookieServerURL(request, path);

		//
		// If locked domains are enabled, create a host prefix using a UUID generated from the
		// widget instance
		//
		URL proxyUrl = urlWidgetProxyServer;
		Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$
		if (properties.getBoolean("widget.instance.lockeddomain.enabled",false)){ //$NON-NLS-1$
			//
			// Generate a UUID from the instance ID key as the subdomain for the instance
			//
			String prefix =  UUID.nameUUIDFromBytes(authToken.toString().getBytes()).toString()+"-locked";
			urlWidget = new URL(urlWidget.getProtocol(), prefix+"."+urlWidget.getHost(), urlWidget.getPort(), path); //$NON-NLS-1$
			//
			// Prepend the subdomain to the proxy URL also
			//
			proxyUrl = new URL(urlWidgetProxyServer.getProtocol(), prefix + "." + urlWidgetProxyServer.getHost(),urlWidgetProxyServer.getPort(), urlWidgetProxyServer.getPath());
		}

		//
		// Append querystring parameters for the URL: id key, proxy URL, and social token
		//
		if (urlWidget.getQuery() != null){
			url+= urlWidget + "&amp;idkey=" + AuthTokenUtils.encryptAuthToken(authToken)  //$NON-NLS-1$
					+ "&amp;proxy=" + proxyUrl.toExternalForm();  //$NON-NLS-1$
		} else {
			url+= urlWidget + "?idkey=" + AuthTokenUtils.encryptAuthToken(authToken)   //$NON-NLS-1$
					+ "&amp;proxy=" + proxyUrl.toExternalForm();  //$NON-NLS-1$
		}
		return url;
	}
}