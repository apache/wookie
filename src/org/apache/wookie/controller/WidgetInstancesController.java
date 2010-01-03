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
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.SharedData;
import org.apache.wookie.beans.StartFile;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.exceptions.InvalidWidgetCallException;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.WidgetFactory;
import org.apache.wookie.helpers.WidgetInstanceHelper;
import org.apache.wookie.helpers.WidgetKeyManager;
import org.apache.wookie.server.LocaleHandler;
import org.apache.wookie.util.LocalizationUtils;

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
	
	protected static void checkProxy(HttpServletRequest request){
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
		WidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);	
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
		WidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
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
		String sharedDataKey = getSharedDataKey(request);	
		String serviceType = request.getParameter("servicetype"); //$NON-NLS-1$
		String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$
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
		
		WidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
		String locale = request.getParameter("locale");//$NON-NLS-1$
		
		// Widget exists
		if(instance==null){
			String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
			instance = WidgetFactory.getWidgetFactory(session, localizedMessages).newInstance(apiKey, userId, sharedDataKey, serviceType, widgetId, locale);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} else {
			response.setStatus(HttpServletResponse.SC_OK);			
		}
		
		// Return the default widget if not created by now
		if(instance==null){
			instance = WidgetFactory.defaultInstance(locale);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);	
		}
		
		String url = getUrl(request, instance);
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println(WidgetInstanceHelper.createXMLWidgetInstanceDocument(instance, url));
	}  
	
	public static void cloneSharedData(HttpServletRequest request, HttpServletResponse response){
		WidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);	
		if (instance == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;			
		}
		String sharedDataKey = getSharedDataKey(request);	
		String cloneSharedDataKey = request.getParameter("cloneshareddatakey");
		if (sharedDataKey == null || sharedDataKey.trim().equals("") || cloneSharedDataKey == null || cloneSharedDataKey.trim().equals("")){//$NON-NLS-1$ //$NON-NLS-2$
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String cloneKey = String.valueOf((request.getParameter("apikey")+":"+cloneSharedDataKey).hashCode());//$NON-NLS-1$ 
		boolean ok = SharedData.clone(sharedDataKey, instance.getWidget().getGuid(), cloneKey);
		if (ok){
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	public synchronized static void lockWidgetInstance(WidgetInstance instance){
		//doLock(instance, true);
		PropertiesController.updateSharedDataEntry(instance, "isLocked", "true", false);//$NON-NLS-1$ //$NON-NLS-2$
		instance.setLocked(true);
		instance.save();
	}

	public synchronized static void unlockWidgetInstance(WidgetInstance instance){
		//doLock(instance, false);
		PropertiesController.updateSharedDataEntry(instance, "isLocked", "false", false);//$NON-NLS-1$ //$NON-NLS-2$
		instance.setLocked(false);
		instance.save();
	}
	
	// Utility methods

	/**
	 * Returns the internal form of shared data key, which is hashed along with the API key. This
	 * prevents shared data keys from clashing between different applications
	 * @param request the HTTP request to retrieve the shared data key from 
	 * @return the shared data key
	 */
	public static String getSharedDataKey(HttpServletRequest request){
		return String.valueOf((request.getParameter("apikey")+":"+request.getParameter("shareddatakey")).hashCode());	 //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Returns the absolute URL of the widget instance including id key, proxy url and opensocial token 
	 * @param request the current request
	 * @param instance the widget instance
	 * @return the absolute URL
	 * @throws IOException
	 */
	protected static String getUrl(HttpServletRequest request, WidgetInstance instance) throws IOException{
		String url = "";
		
		StartFile sf = (StartFile) LocalizationUtils.getLocalizedElement(StartFile.findByValue("widget", instance.getWidget()), new String[]{instance.getLang()});
		// Try default locale if no appropriate localization found
		if (sf == null) sf = (StartFile) LocalizationUtils.getLocalizedElement(StartFile.findByValue("widget", instance.getWidget()), null);
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
	public static WidgetInstance findWidgetInstance(HttpServletRequest request){
		WidgetInstance instance;
		
		String id_key = request.getParameter("id_key"); //$NON-NLS-1$
		if (id_key != null & id_key != ""){
			instance = WidgetInstance.findByIdKey(id_key);
			return instance;
		}
		
		String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String userId = request.getParameter("userid"); //$NON-NLS-1$
		String sharedDataKey = WidgetInstancesController.getSharedDataKey(request);	
		String serviceType = request.getParameter("servicetype"); //$NON-NLS-1$
		String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$
		if (widgetId != null){
			instance = WidgetInstance.getWidgetInstanceById(apiKey, userId, sharedDataKey, widgetId);
		} else {
			instance = WidgetInstance.getWidgetInstance(apiKey, userId, sharedDataKey, serviceType);
		}
		return instance;
	}
}