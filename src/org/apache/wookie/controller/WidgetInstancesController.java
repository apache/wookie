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
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.exceptions.InvalidWidgetCallException;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.SharedDataHelper;
import org.apache.wookie.helpers.WidgetInstanceFactory;
import org.apache.wookie.helpers.WidgetInstanceHelper;
import org.apache.wookie.helpers.WidgetKeyManager;
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
public class WidgetInstancesController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(WidgetInstancesController.class.getName());	
	protected static final String CONTENT_TYPE = "text/xml;charset=\"UTF-8\""; 	 //$NON-NLS-1$
	protected static URL urlWidgetProxyServer = null;	
	
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
				urlWidgetProxyServer = new URL(scheme,serverName,serverPort,properties.getString("widget.proxy.path"));
			} catch (MalformedURLException e) {
				// ignore errors
			} 
		}
		return urlWidgetProxyServer.toExternalForm();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			doGetWidget(request, response);
		}
	}



	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			try {
				String requestId = request.getParameter("requestid"); //$NON-NLS-1$
				if (requestId == null || requestId.equals("")){
					response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				} else {
					 if(requestId.equals("stopwidget")){ //$NON-NLS-1$
						doStopWidget(request, response);
					} else if(requestId.equals("resumewidget")){ //$NON-NLS-1$
						doResumeWidget(request, response);
					} else if(requestId.equals("clone")){ //$NON-NLS-1$
						cloneSharedData(request, response);
					} else {
						response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					}
				}
			} catch (Exception ex) {					
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}



	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response){					
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			try {
				String requestId = request.getParameter("requestid"); //$NON-NLS-1$
				if (requestId == null || requestId.equals("")){
					response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				} else {
					if(requestId.equals("getwidget")){ //$NON-NLS-1$
						doGetWidget(request, response);
					} else if(requestId.equals("stopwidget")){ //$NON-NLS-1$
						doStopWidget(request, response);
					} else if(requestId.equals("resumewidget")){ //$NON-NLS-1$
						doResumeWidget(request, response);
					} else if(requestId.equals("clone")){ //$NON-NLS-1$
						cloneSharedData(request, response);
					} else {
						response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					}
				}
			} catch (Exception ex) {					
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	
	/// Implementation

	public static void doStopWidget(HttpServletRequest request, HttpServletResponse response) throws IOException{				
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);	
		if(instance!=null){
			lockWidgetInstance(instance);
			Notifier.notifyWidgets(request.getSession(), instance, Notifier.STATE_UPDATED);
			response.setStatus(HttpServletResponse.SC_OK);
		}else{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,localizedMessages.getString("WidgetServiceServlet.3"));//$NON-NLS-1$
		}
	}

	public static void doResumeWidget(HttpServletRequest request, HttpServletResponse response) throws IOException{					
		Messages localizedMessages = LocaleHandler.localizeMessages(request); 
		IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
		if(instance!=null){
			unlockWidgetInstance(instance);
			Notifier.notifyWidgets(request.getSession(), instance, Notifier.STATE_UPDATED);
			response.setStatus(HttpServletResponse.SC_OK);
		}else{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,localizedMessages.getString("WidgetServiceServlet.3"));//$NON-NLS-1$
		}
	}
	
	public static void doGetWidget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userid"); //$NON-NLS-1$
		String sharedDataKey =  request.getParameter("shareddatakey");	 //$NON-NLS-1$
        String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String serviceType = request.getParameter("servicetype"); //$NON-NLS-1$
		String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$
        sharedDataKey = SharedDataHelper.getInternalSharedDataKey(apiKey, widgetId, sharedDataKey);
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);

		try {						
			if(userId==null || sharedDataKey==null || (serviceType==null && widgetId==null)){
				throw new InvalidWidgetCallException();
			}
		} 
		catch (InvalidWidgetCallException ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		checkProxy(request);
		
		IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
		String locale = request.getParameter("locale");//$NON-NLS-1$
		
		// Widget exists
		if(instance==null){
			instance = WidgetInstanceFactory.getWidgetFactory(session, localizedMessages).newInstance(apiKey, userId, sharedDataKey, serviceType, widgetId, locale);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} else {
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
	
	public static void cloneSharedData(HttpServletRequest request, HttpServletResponse response){
		IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);	
		if (instance == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;			
		}
		String sharedDataKey = request.getParameter("shareddatakey");	 //$NON-NLS-1$;	
		String cloneSharedDataKey = request.getParameter("cloneshareddatakey");
		if (sharedDataKey == null || sharedDataKey.trim().equals("") || cloneSharedDataKey == null || cloneSharedDataKey.trim().equals("")){//$NON-NLS-1$ //$NON-NLS-2$
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String cloneKey = SharedDataHelper.getInternalSharedDataKey(instance, cloneSharedDataKey);
        IWidget widget = instance.getWidget();
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		for (ISharedData sharedData : SharedDataHelper.findSharedData(instance))
		{
		    ISharedData clone = persistenceManager.newInstance(ISharedData.class);
            clone.setDkey(sharedData.getDkey());
            clone.setDvalue(sharedData.getDvalue());
            clone.setSharedDataKey(cloneKey);
            persistenceManager.save(clone);
		}
		boolean ok = persistenceManager.save(widget);
		if (ok){
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	public synchronized static void lockWidgetInstance(IWidgetInstance instance){
		//doLock(instance, true);
		PropertiesController.updateSharedDataEntry(instance, "isLocked", "true", false);//$NON-NLS-1$ //$NON-NLS-2$
		instance.setLocked(true);
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        persistenceManager.save(instance);
	}

	public synchronized static void unlockWidgetInstance(IWidgetInstance instance){
		//doLock(instance, false);
		PropertiesController.updateSharedDataEntry(instance, "isLocked", "false", false);//$NON-NLS-1$ //$NON-NLS-2$
		instance.setLocked(false);
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        persistenceManager.save(instance);
	}
	
	// Utility methods
	
	/**
	 * Returns the absolute URL of the widget instance including id key, proxy url and opensocial token 
	 * @param request the current request
	 * @param instance the widget instance
	 * @return the absolute URL
	 * @throws IOException
	 */
	protected static String getUrl(HttpServletRequest request, IWidgetInstance instance) throws IOException{
		String url = "";

		IStartFile[] startFiles = instance.getWidget().getStartFiles().toArray(new IStartFile[instance.getWidget().getStartFiles().size()]);
        IStartFile sf = (IStartFile) LocalizationUtils.getLocalizedElement(startFiles, new String[]{instance.getLang()}, instance.getWidget().getDefaultLocale());
		// Try default locale if no appropriate localization found
		if (sf == null) sf = (IStartFile) LocalizationUtils.getLocalizedElement(startFiles, null, instance.getWidget().getDefaultLocale());
		// No start file found, so throw an exception
		if (sf == null) throw new IOException("No start file located for widget "+instance.getWidget().getGuid());
		
		URL urlWidget =  new URL(request.getScheme() ,
				request.getServerName() ,
				request.getServerPort() , sf.getUrl());
		
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
	 * Utility method for locating an instance based on various parameters. Consider moving to a utils class, or
	 * even to the WidgetInstance ActiveRecord class.
	 * @param request
	 * @return
	 */
	public static IWidgetInstance findWidgetInstance(HttpServletRequest request){
		IWidgetInstance instance;

		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		String id_key = request.getParameter("id_key"); //$NON-NLS-1$
		if (id_key != null & id_key != ""){
			instance = persistenceManager.findWidgetInstanceByIdKey(id_key);
			return instance;
		}

		try {
			String apiKey = URLDecoder.decode(request.getParameter("api_key"), "UTF-8"); //$NON-NLS-1$
			String userId = URLDecoder.decode(request.getParameter("userid"), "UTF-8"); //$NON-NLS-1$
			String sharedDataKey = request.getParameter("shareddatakey");	 //$NON-NLS-1$;
			String widgetId = request.getParameter("widgetid");
	        sharedDataKey = SharedDataHelper.getInternalSharedDataKey(apiKey, widgetId, sharedDataKey);
			if (widgetId != null){
				widgetId = URLDecoder.decode(widgetId, "UTF-8"); //$NON-NLS-1$
				_logger.debug("Looking for widget instance with widgetid of " + widgetId);
				instance = persistenceManager.findWidgetInstanceByGuid(apiKey, userId, sharedDataKey, widgetId);
			} else {
				String serviceType = URLDecoder.decode(request.getParameter("servicetype"), "UTF-8"); //$NON-NLS-1$
				_logger.debug("Looking for widget instance of service type " + serviceType);
				instance = persistenceManager.findWidgetInstance(apiKey, userId, sharedDataKey, serviceType);
			}
			if (instance == null) {
				_logger.debug("No widget instance found for APIkey= "+apiKey+" userId="+userId+" widgetId="+widgetId);
			}
			return instance;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Server must support UTF-8 encoding", e);
		} //$NON-NLS-1$

	}
}