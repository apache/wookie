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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.MigrationHelper;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.SharedDataHelper;
import org.apache.wookie.helpers.WidgetInstanceFactory;
import org.apache.wookie.helpers.WidgetInstanceHelper;
import org.apache.wookie.helpers.WidgetRuntimeHelper;
import org.apache.wookie.server.LocaleHandler;
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
	

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/**
	 * We override the default POST action as we need to support the legacy getOrCreate method
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doGetOrCreateWidget(request, response);
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/**
	 * We override the default GET method to support legacy clients tunneling through GET to send other types of
	 * requests usually sent over POST and PUT.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{					
	    try {
	      String resourceId = getResourceId(request);
	      String requestId = request.getParameter("requestid"); //$NON-NLS-1$
	      if (requestId == null || requestId.equals("")){
	        show(resourceId, request, response);
	      } else {
	        if(requestId.equals("getwidget")){ //$NON-NLS-1$
	          doGetOrCreateWidget(request, response);
	        } else if(requestId.equals("stopwidget")){ //$NON-NLS-1$
	          doStopWidget(request);
	        } else if(requestId.equals("resumewidget")){ //$NON-NLS-1$
	          doResumeWidget(request);
	        } else if(requestId.equals("clone")){ //$NON-NLS-1$
	          cloneSharedData(request);
	        } else {
	          response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	        }
	      }
      } catch (ResourceNotFoundException e) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      } catch (UnauthorizedAccessException e){
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	    } catch (Exception ex) {					
	      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    }
	}
	
	/// Implementation
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#show(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void show(String resourceId, HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException, UnauthorizedAccessException, IOException {
	   IWidgetInstance instance = WidgetInstancesController.getLocalizedWidgetInstance(request);
	   if (instance == null){
	     throw new ResourceNotFoundException();
	   } else {
	     // Check the API key matches
	     String apiKey = request.getParameter("api_key");
	     if (!instance.getApiKey().equals(apiKey)) throw new UnauthorizedAccessException();
	     // Return the response XML
	     checkProxy(request);
	     String url = getUrl(request, instance);
	     String locale = request.getParameter("locale");//$NON-NLS-1$
	     response.setContentType(CONTENT_TYPE);
	     response.setStatus(HttpServletResponse.SC_OK);
	     PrintWriter out = response.getWriter();
	     out.println(WidgetInstanceHelper.createXMLWidgetInstanceDocument(instance, url, locale));
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
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#remove(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected boolean remove(String resourceId, HttpServletRequest request) 
	throws ResourceNotFoundException,UnauthorizedAccessException,InvalidParametersException{	  
	  return deleteWidgetInstance(resourceId, request);
	}

	/**
	 * Locks a widget instance
	 * @param request
	 * @throws InvalidParametersException
	 */
	public static void doStopWidget(HttpServletRequest request) throws InvalidParametersException{				
	  IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);	
	  if(instance!=null){
	    lockWidgetInstance(instance);
	    Notifier.notifyWidgets(request.getSession(), instance, Notifier.STATE_UPDATED);
	  }else{
	    throw new InvalidParametersException();
	  }
	}

	/**
	 * UNlocks a widget insa
	 * @param request
	 * @throws InvalidParametersException
	 */
	public static void doResumeWidget(HttpServletRequest request) throws InvalidParametersException{					
	  IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
	  if(instance!=null){
	    unlockWidgetInstance(instance);
	    Notifier.notifyWidgets(request.getSession(), instance, Notifier.STATE_UPDATED);
	  }else{
	    throw new InvalidParametersException();
	  }
	}
	
	public static boolean deleteWidgetInstance(String resourceId, HttpServletRequest request) throws InvalidParametersException, ResourceNotFoundException {
	  //TODO - check security and that the key is from valid user for instance
	  String userId = request.getParameter("userid"); //$NON-NLS-1$
	  String sharedDataKey =  request.getParameter("shareddatakey");   //$NON-NLS-1$
	  String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
	  String serviceType = request.getParameter("servicetype"); //$NON-NLS-1$
	  String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$
	  sharedDataKey = SharedDataHelper.getInternalSharedDataKey(apiKey, widgetId, sharedDataKey);
	  if(userId==null || sharedDataKey==null || (serviceType==null && widgetId==null)){
	    throw new InvalidParametersException();
	  }
	  IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
	  if(instance==null){
	    throw new ResourceNotFoundException();
	  }
	  else{
	    WidgetInstanceFactory.destroy(instance);
	    return true;
	  }    	  
	}

	/**
	 * This is the "legacy" get-or-create method accessed via POST or a tunnel through GET. This will be deprecated in future.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void doGetOrCreateWidget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userid"); //$NON-NLS-1$
		String sharedDataKey =  request.getParameter("shareddatakey");	 //$NON-NLS-1$
		String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String serviceType = request.getParameter("servicetype"); //$NON-NLS-1$
		String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$
		sharedDataKey = SharedDataHelper.getInternalSharedDataKey(apiKey, widgetId, sharedDataKey);
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
					
		if(userId==null || sharedDataKey==null || (serviceType==null && widgetId==null)){
		  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		  return;
		}

		checkProxy(request);
		
		IWidgetInstance instance = WidgetInstancesController.getLocalizedWidgetInstance(request);
        String locale = request.getParameter("locale");//$NON-NLS-1$
		
		// Widget exists
		if(instance==null){
			instance = WidgetInstanceFactory.getWidgetFactory(session, localizedMessages).newInstance(apiKey, userId, sharedDataKey, serviceType, widgetId, locale);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} else {
			response.setStatus(HttpServletResponse.SC_OK);			
		}
		
		// Return the default widget if not created by now
		
		if(instance==null){
			instance = WidgetInstanceFactory.defaultInstance(locale);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);	
		}
		
		String url = getUrl(request, instance);
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println(WidgetInstanceHelper.createXMLWidgetInstanceDocument(instance, url, locale));
	}  
	
	/**
	 * Create a clone of the instance
	 * @param request
	 * @throws InvalidParametersException
	 */
	public static void cloneSharedData(HttpServletRequest request) throws InvalidParametersException{
		IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);	
		if (instance == null){
      throw new InvalidParametersException();		
		}
		String sharedDataKey = request.getParameter("shareddatakey");	 //$NON-NLS-1$;	
		String cloneSharedDataKey = request.getParameter("cloneshareddatakey");
		if (sharedDataKey == null || sharedDataKey.trim().equals("") || cloneSharedDataKey == null || cloneSharedDataKey.trim().equals("")){//$NON-NLS-1$ //$NON-NLS-2$
      throw new InvalidParametersException();
		}
		String cloneKey = SharedDataHelper.getInternalSharedDataKey(instance, cloneSharedDataKey);
        IWidget widget = instance.getWidget();
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		for (ISharedData sharedData : new SharedContext(instance).getSharedData())
		{
		    ISharedData clone = persistenceManager.newInstance(ISharedData.class);
            clone.setDkey(sharedData.getDkey());
            clone.setDvalue(sharedData.getDvalue());
            clone.setSharedDataKey(cloneKey);
            persistenceManager.save(clone);
		}
		boolean ok = persistenceManager.save(widget);
		if (!ok) throw new InvalidParametersException();
	}
	
	public synchronized static void lockWidgetInstance(IWidgetInstance instance){
	  new SharedContext(instance).updateSharedData("isLocked", "true", false); //$NON-NLS-1$ //$NON-NLS-2$
		instance.setLocked(true);
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    persistenceManager.save(instance);
	}

	public synchronized static void unlockWidgetInstance(IWidgetInstance instance){
	  new SharedContext(instance).updateSharedData("isLocked", "false", false); //$NON-NLS-1$ //$NON-NLS-2$
		instance.setLocked(false);
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    persistenceManager.save(instance);
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
	 * @throws IOException
	 */
	protected static String getUrl(HttpServletRequest request, IWidgetInstance instance) throws IOException{
		String url = "";

		//
		// Locate the startfile for the Widget Instance
		//
		IStartFile[] startFiles = instance.getWidget().getStartFiles().toArray(new IStartFile[instance.getWidget().getStartFiles().size()]);
        IStartFile sf = (IStartFile) LocalizationUtils.getLocalizedElement(startFiles, new String[]{instance.getLang()}, instance.getWidget().getDefaultLocale());
    
    //
    // Try default locale if no appropriate localization found
    //
		if (sf == null) sf = (IStartFile) LocalizationUtils.getLocalizedElement(startFiles, null, instance.getWidget().getDefaultLocale());
		
		//
		// No start file found, so throw an exception
		//
		if (sf == null) throw new IOException("No start file located for widget "+instance.getWidget().getGuid());
		
		//
		// Get a URL for the start file on this Wookie server
		//
    String path = sf.getUrl();
		URL urlWidget =  getWookieServerURL(request, path);
		
		//
		// Append querystring parameters for the URL: id key, proxy URL, and social token
		//
		if (urlWidget.getQuery() != null){
			url+= urlWidget + "&amp;idkey=" + instance.getIdKey()  //$NON-NLS-1$
					+ "&amp;proxy=" + urlWidgetProxyServer.toExternalForm()  //$NON-NLS-1$
					+ "&amp;st=" + instance.getOpensocialToken(); //$NON-NLS-1$
		} else {
			url+= urlWidget + "?idkey=" + instance.getIdKey()  //$NON-NLS-1$
					+ "&amp;proxy=" + urlWidgetProxyServer.toExternalForm()  //$NON-NLS-1$
					+ "&amp;st=" + instance.getOpensocialToken(); //$NON-NLS-1$
		}
		return url;
	}

	/**
	 * Utility method for finding an instance localized for the request locale, and updating
	 * the locale if the user has changed it.
	 * @param request
	 * @return the widget instance
	 */
	private static IWidgetInstance getLocalizedWidgetInstance(HttpServletRequest request){
	  IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
	  if (instance != null){
	    String locale = request.getParameter("locale");//$NON-NLS-1$
	    // If the requested locale is different to the saved locale, update the "lang" attribute
	    // of the widget instance and save it
	    if (
	        (locale == null && instance.getLang()!=null) || 
	        (locale != null && instance.getLang()==null) ||           
	        (locale != null && !instance.getLang().equals(locale))
	    ){
	      IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	      instance.setLang(locale);
	      persistenceManager.save(instance);
	    }
	  }
	  return instance;
	}

	
	/**
	 * Utility method for locating an instance based on various parameters. Consider moving to a utils class, or
	 * even to the WidgetInstance ActiveRecord class.
	 * @param request
	 * @return
	 */
	public static IWidgetInstance findWidgetInstance(HttpServletRequest request){
		IWidgetInstance instance;
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();

		// Try using the id_key parameter
    String id_key = request.getParameter("idkey"); //$NON-NLS-1$
		if (id_key != null & id_key != ""){
		  instance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		  return instance;
		}
		// Try using the resource part of the path as an id key e.g. widgetinstances/xyz
		id_key = getResourceId(request);
		if (id_key != null & id_key != ""){
		  instance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		  return instance;
		}
		//
		// If all else fails, try using instance parameters
		//
		try {

		  String apiKey = URLDecoder.decode(request.getParameter("api_key"), "UTF-8"); //$NON-NLS-1$
		  String userId = URLDecoder.decode(request.getParameter("userid"), "UTF-8"); //$NON-NLS-1$
		  String sharedDataKey = request.getParameter("shareddatakey");	 //$NON-NLS-1$;
		  String widgetId = request.getParameter("widgetid");
		  //
		  // First see if there is a legacy 0.9.0 instance that matches
		  //
		  // NOTE: This step will be deprecated in future releases
		  //
		  instance = MigrationHelper.findLegacyWidgetInstance(apiKey, userId, sharedDataKey, widgetId);
		  // 
		  // Otherwise, look for a 0.9.1 or later version
		  //
		  if (instance == null){   
		    String internalSharedDataKey = SharedDataHelper.getInternalSharedDataKey(apiKey, widgetId, sharedDataKey);
		    instance = findWidgetInstance(apiKey, userId, internalSharedDataKey, widgetId);
		  }

		  if (instance == null) {
		    _logger.debug("No widget instance found for APIkey= "+apiKey+" userId="+userId+" widgetId="+widgetId);
		  }
		  return instance;

		} catch (UnsupportedEncodingException e) {
		  throw new RuntimeException("Server must support UTF-8 encoding", e);
		} //$NON-NLS-1$
	}

	/**
	 * Find a widget instance using instance parameters, by widget URI (guid)
	 * 
	 * @param apiKey
	 * @param userId
	 * @param sharedDataKey
	 * @param widgetId
	 * @return the widget instance, or null if there is no matching instance
	 * @throws UnsupportedEncodingException
	 */
	public static IWidgetInstance findWidgetInstance(String apiKey, String userId, String sharedDataKey, String widgetId) throws UnsupportedEncodingException{
	  IWidgetInstance instance = null;
	  IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	  if (widgetId != null){
	    widgetId = URLDecoder.decode(widgetId, "UTF-8"); //$NON-NLS-1$
	    _logger.debug("Looking for widget instance with widgetid of " + widgetId);
	    instance = persistenceManager.findWidgetInstanceByGuid(apiKey, userId, sharedDataKey, widgetId);
	  }
	  return instance;
	}
}