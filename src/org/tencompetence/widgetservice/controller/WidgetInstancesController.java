/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.widgetservice.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.InvalidWidgetCallException;
import org.tencompetence.widgetservice.helpers.Notifier;
import org.tencompetence.widgetservice.helpers.WidgetFactory;
import org.tencompetence.widgetservice.helpers.WidgetKeyManager;
import org.tencompetence.widgetservice.server.LocaleHandler;
import org.tencompetence.widgetservice.util.HashGenerator;
import org.tencompetence.widgetservice.util.RandomGUID;

/**
 * REST implementation for widgetInstance
 *
 * POST: creates and returns (or just returns) an instance
 * PUT: stop or resume an instance
 * (GET: redirect to other actions. Useful for some limited clients)
 *
 */
public class WidgetInstancesController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(WidgetInstancesController.class.getName());	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; //$NON-NLS-1$
	private static final String CONTENT_TYPE = "text/xml;charset=\"UTF-8\""; 	 //$NON-NLS-1$
	private static URL urlWidgetProxyServer = null;	

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGetWidget(req, resp);
	}



	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,localizedMessages.getString("WidgetServiceServlet.3"));
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,localizedMessages.getString("WidgetServiceServlet.3"));
		}
	}
	
	public static void doGetWidget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String userId = request.getParameter("userid"); //$NON-NLS-1$
		String sharedDataKey = getSharedDataKey(request);	
		String serviceType = request.getParameter("servicetype"); //$NON-NLS-1$
		String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$

		try {						
			if(userId==null || sharedDataKey==null || (serviceType==null && widgetId==null)){
				throw new InvalidWidgetCallException();
			}
		} 
		catch (InvalidWidgetCallException ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		// set the proxy url.
		if(urlWidgetProxyServer==null){
			Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$
			String scheme = request.getScheme();
			String serverName = request.getServerName();
			int serverPort = request.getServerPort();
			if (!properties.getString("widget.proxy.scheme").trim().equals("")) scheme = properties.getString("widget.proxy.scheme"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (!properties.getString("widget.proxy.hostname").trim().equals("")) serverName = properties.getString("widget.proxy.hostname"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (!properties.getString("widget.proxy.port").trim().equals("")) serverPort = Integer.parseInt(properties.getString("widget.proxy.port")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			urlWidgetProxyServer = new URL(scheme,serverName,serverPort,properties.getString("widget.proxy.path")); //$NON-NLS-1$
		}

		WidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);

		// Widget exists
		if(instance==null) instance = newInstance(request);
		
		// Return default widget if not created by now
		if(instance==null){
			Widget unsupportedWidget = Widget.findDefaultByType("unsupported"); //$NON-NLS-1$
			formatReturnDoc(request, response, unsupportedWidget, "0000", ""); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			formatReturnDoc(request, response, instance.getWidget(), instance.getIdKey(), instance.getOpensocialToken());
		}
	}  
	
	// Utility methods

	private static WidgetInstance newInstance(HttpServletRequest request){
		String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String userId = request.getParameter("userid"); //$NON-NLS-1$
		String sharedDataKey = getSharedDataKey(request);	
		String serviceType = request.getParameter("servicetype"); //$NON-NLS-1$
		String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		WidgetFactory factory = getWidgetFactory(session, localizedMessages); 
		try {
			Widget widget;
			WidgetInstance widgetInstance;
			// Widget ID or Widget Type?
			if (widgetId != null){
				widget = Widget.findByGuid(widgetId);
			} 
			else {
				// does this type of widget exist?
				widget = Widget.findDefaultByType(serviceType);				
			}
			// Unsupported
			if (widget == null) return null;

			// generate a nonce
			String nonce = RandomGUID.getUniqueID("nonce-");//$NON-NLS-1$

			// now use SHA hash on the nonce				
			String hashKey = HashGenerator.getInstance().encrypt(nonce);	

			// get rid of any chars that might upset a url...
			hashKey = hashKey.replaceAll("=", ".eq."); //$NON-NLS-1$ //$NON-NLS-2$
			hashKey = hashKey.replaceAll("\\?", ".qu."); //$NON-NLS-1$ //$NON-NLS-2$
			hashKey = hashKey.replaceAll("&", ".am."); //$NON-NLS-1$ //$NON-NLS-2$
			hashKey = hashKey.replaceAll("\\+", ".pl."); //$NON-NLS-1$ //$NON-NLS-2$
			
			Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("opensocial"); //$NON-NLS-1$
			widgetInstance = factory.addNewWidgetInstance(apiKey, userId, sharedDataKey, widget, nonce, hashKey, properties);
			return widgetInstance;
		} catch (Exception ex) {
			return null;
		}
	}
	
	private static void formatReturnDoc(HttpServletRequest request, HttpServletResponse response, Widget widget, String key, String token) throws IOException{
		URL urlWidget =  new URL(request.getScheme() ,
				request.getServerName() ,
				request.getServerPort() , widget.getUrl());

		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println(XMLDECLARATION);			
		out.println("<widgetdata>");					 //$NON-NLS-1$
		out.print("<url>"); //$NON-NLS-1$
		if (urlWidget.getQuery() != null){
			out.print(urlWidget + "&amp;idkey=" + key  //$NON-NLS-1$
					+ "&amp;proxy=" + urlWidgetProxyServer.toExternalForm()  //$NON-NLS-1$
					+ "&amp;st=" + token //$NON-NLS-1$
			);	
		} else {
			out.print(urlWidget + "?idkey=" + key  //$NON-NLS-1$
					+ "&amp;proxy=" + urlWidgetProxyServer.toExternalForm()  //$NON-NLS-1$
					+ "&amp;st=" + token //$NON-NLS-1$
			);
		}
		out.println("</url>"); //$NON-NLS-1$
		out.println("<identifier>"+key+"</identifier>"); //$NON-NLS-1$ //$NON-NLS-2$
		out.println("<title>"+widget.getWidgetTitle()+"</title>"); //$NON-NLS-1$ //$NON-NLS-2$
		out.println("<height>"+widget.getHeight()+"</height>"); //$NON-NLS-1$ //$NON-NLS-2$
		out.println("<width>"+widget.getWidth()+"</width>"); //$NON-NLS-1$ //$NON-NLS-2$
		out.println("<maximize>"+widget.isMaximize()+"</maximize>"); //$NON-NLS-1$ //$NON-NLS-2$
		out.println("</widgetdata>"); //$NON-NLS-1$
	}

	public static String getSharedDataKey(HttpServletRequest request){
		return String.valueOf((request.getParameter("apikey")+":"+request.getParameter("shareddatakey")).hashCode());	 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Return a handle on a service manager localized for the session
	 * @param session
	 * @param localizedMessages
	 * @return
	 */
	private static WidgetFactory getWidgetFactory(HttpSession session, Messages localizedMessages){
		WidgetFactory manager = (WidgetFactory)session.getAttribute(WidgetFactory.class.getName());				
		if(manager == null){
			manager = new WidgetFactory(localizedMessages);
			session.setAttribute(WidgetFactory.class.getName(), manager);
		}
		return manager;
	}
	
	/**
	 * Utility method for locating an instance based on various parameters
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
	
	/// Business Methods

	public synchronized static void lockWidgetInstance(WidgetInstance instance){
		//doLock(instance, true);
		PropertiesController.updateSharedDataEntry(instance, "isLocked", "true", false);
		instance.setLocked(true);
		instance.save();
	}

	public synchronized static void unlockWidgetInstance(WidgetInstance instance){
		//doLock(instance, false);
		PropertiesController.updateSharedDataEntry(instance, "isLocked", "false", false);
		instance.setLocked(false);
		instance.save();
	}

}