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
package org.tencompetence.widgetservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.InvalidWidgetCallException;
import org.tencompetence.widgetservice.exceptions.SystemUnavailableException;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;
import org.tencompetence.widgetservice.manager.IWidgetServiceManager;
import org.tencompetence.widgetservice.manager.impl.WidgetKeyManager;
import org.tencompetence.widgetservice.manager.impl.WidgetServiceManager;
import org.tencompetence.widgetservice.server.LocaleHandler;
import org.tencompetence.widgetservice.util.HashGenerator;
import org.tencompetence.widgetservice.util.RandomGUID;

/**
 * Servlet implementation class for Servlet: WidgetService
 * @author Paul Sharples
 * @version $Id: WidgetServiceServlet.java,v 1.19 2009-06-03 15:45:58 scottwilson Exp $ 
 *
 */
public class WidgetServiceServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(WidgetServiceServlet.class.getName());	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; //$NON-NLS-1$
	private static final String CONTENT_TYPE = "text/xml;charset=\"UTF-8\""; 	 //$NON-NLS-1$
	private static URL urlWidgetProxyServer = null;	

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public WidgetServiceServlet() {
		super();
	}   	

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response){					
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if (!WidgetKeyManager.isValidRequest(request)){
			try {
				returnDoc(response, localizedMessages.getString("WidgetServiceServlet.2"), "error"); //$NON-NLS-1$ //$NON-NLS-2$
			} 
			catch (IOException e) {
				_logger.error("Error in doGet():", e); //$NON-NLS-1$
			}
		} 
		else {
			try {
				String requestId = request.getParameter("requestid"); //$NON-NLS-1$
				if(requestId.equals("getwidget")){ //$NON-NLS-1$
					doGetWidget(request, response);
				}
				else if(requestId.equals("stopwidget")){ //$NON-NLS-1$
					doStopWidget(request, response);
				}
				else if(requestId.equals("resumewidget")){ //$NON-NLS-1$
					doResumeWidget(request, response);
				}
				else if(requestId.equals("setpublicproperty")){ //$NON-NLS-1$
					doSetProperty(request, response, false);
				}		
				else if(requestId.equals("setpersonalproperty")){ //$NON-NLS-1$
					doSetProperty(request, response, true );
				}
				else if(requestId.equals("addparticipant")){ //$NON-NLS-1$
					doAddParticipant(request, response );
				}
				else if(requestId.equals("removeparticipant")){ //$NON-NLS-1$
					doRemoveParticipant(request, response );
				}
				else {
					returnDoc(response, localizedMessages.getString("WidgetServiceServlet.0"), "error"); //$NON-NLS-1$ //$NON-NLS-2$
				}

			} 
			catch (Exception ex) {					
				_logger.error("Error in doGet():", ex); //$NON-NLS-1$
			}
		}
	}
	
	private void doAddParticipant(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetServiceManager manager = getServiceManager(session, localizedMessages); 
		String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String user_id = request.getParameter("userid"); //$NON-NLS-1$
		String sharedDataKey = getSharedDataKey(request);
		String serviceType= request.getParameter("servicetype"); //$NON-NLS-1$
		String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$
		String participant_id = request.getParameter("participant_id"); //$NON-NLS-1$
		String participant_display_name = request.getParameter("participant_display_name"); //$NON-NLS-1$
		String participant_thumbnail_url = request.getParameter("participant_thumbnail_url"); //$NON-NLS-1$

		WidgetInstance instance = null;
		if (widgetId != null){
			instance = manager.getWidgetInstanceById(apiKey, user_id, sharedDataKey, widgetId);
		} 
		else {
			instance = manager.getWidgetInstance(apiKey, user_id, sharedDataKey, serviceType);
		}
		if(instance != null){
			try {
				if (manager.addParticipantToWidgetInstance(instance, participant_id, participant_display_name, participant_thumbnail_url))
					notifyWidgets(session, manager, instance);
				returnDoc(response, localizedMessages.getString("WidgetServiceServlet.1"), "message");							 //$NON-NLS-1$ //$NON-NLS-2$
			} 
			catch (Exception ex) {	
				ex.printStackTrace();
				returnDoc(response, localizedMessages.getString("WidgetServiceServlet.6"), "error");	//$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		else{
			returnDoc(response, localizedMessages.getString("WidgetServiceServlet.3"), "error"); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}
	
	private void doRemoveParticipant(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetServiceManager manager = getServiceManager(session, localizedMessages); 
		String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String user_id = request.getParameter("userid"); //$NON-NLS-1$
		String participant_id = request.getParameter("participant_id"); //$NON-NLS-1$
		String sharedDataKey = getSharedDataKey(request);
		String serviceType= request.getParameter("servicetype"); //$NON-NLS-1$
		String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$

		WidgetInstance instance = null;
		if (widgetId != null){
			instance = manager.getWidgetInstanceById(apiKey, user_id, sharedDataKey, widgetId);
		} 
		else {
			instance = manager.getWidgetInstance(apiKey, user_id, sharedDataKey, serviceType);
		}
		if(instance != null){
			try {
				if(manager.removeParticipantFromWidgetInstance(instance, participant_id))
					notifyWidgets(session, manager, instance);
				returnDoc(response, localizedMessages.getString("WidgetServiceServlet.1"), "message");							 //$NON-NLS-1$ //$NON-NLS-2$
			} 
			catch (Exception ex) {			
				returnDoc(response, localizedMessages.getString("WidgetServiceServlet.5"), "error");	//$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		else{
			returnDoc(response, localizedMessages.getString("WidgetServiceServlet.3"), "error"); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	private void doStopWidget(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetServiceManager manager = getServiceManager(session, localizedMessages); 		
		String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String userId = request.getParameter("userid");	 //$NON-NLS-1$
		String sharedDataKey = getSharedDataKey(request);	
		String serviceType= request.getParameter("servicetype"); //$NON-NLS-1$
		
		WidgetInstance widgetInstance = manager.getWidgetInstance(apiKey, userId, sharedDataKey, serviceType);		
		if(widgetInstance!=null){
			manager.lockWidgetInstance(widgetInstance);
			returnDoc(response, localizedMessages.getString("WidgetServiceServlet.1"), "message"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else{
			returnDoc(response, localizedMessages.getString("WidgetServiceServlet.3"), "error"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		_logger.debug("*** stop widget called ****"); //$NON-NLS-1$
		_logger.debug("*** "+ userId + " ****"); //$NON-NLS-1$ //$NON-NLS-2$
		_logger.debug("***************************"); //$NON-NLS-1$
	}

	private void doResumeWidget(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetServiceManager manager = getServiceManager(session, localizedMessages); 
		String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String userId = request.getParameter("userid"); //$NON-NLS-1$
		String sharedDataKey = getSharedDataKey(request);		
		String serviceType= request.getParameter("servicetype"); //$NON-NLS-1$
						
		WidgetInstance widgetInstance = manager.getWidgetInstance(apiKey, userId, sharedDataKey, serviceType);		
		if(widgetInstance!=null){
			manager.unlockWidgetInstance(widgetInstance);
			returnDoc(response, localizedMessages.getString("WidgetServiceServlet.1"), "message"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else{
			returnDoc(response, localizedMessages.getString("WidgetServiceServlet.3"), "error"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		_logger.debug("*** resume widget called ****"); //$NON-NLS-1$
		_logger.debug("*** "+ userId + " ****"); //$NON-NLS-1$ //$NON-NLS-2$
		_logger.debug("***************************"); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param isPersonalProperty - If the boolean is set to true, then a preference will be set otherwise its shareddata
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doSetProperty (HttpServletRequest request, HttpServletResponse response, boolean isPersonalProperty) throws ServletException, IOException {
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetServiceManager manager = getServiceManager(session, localizedMessages); 
		String apiKey = request.getParameter("api_key"); //$NON-NLS-1$
		String userId = request.getParameter("userid"); //$NON-NLS-1$
		String sharedDataKey = getSharedDataKey(request);
		String serviceType= request.getParameter("servicetype"); //$NON-NLS-1$
		String propertyName = request.getParameter("propertyname"); //$NON-NLS-1$
		String propertyValue = request.getParameter("propertyvalue"); //$NON-NLS-1$
		String widgetId = request.getParameter("widgetid"); //$NON-NLS-1$

		WidgetInstance instance = null;
		if (widgetId != null){
			instance = manager.getWidgetInstanceById(apiKey,userId, sharedDataKey, widgetId);
		} 
		else {
			instance = manager.getWidgetInstance(apiKey,userId, sharedDataKey, serviceType);
		}
		if(instance != null){
			try {
				if(isPersonalProperty){
					manager.updatePreference(instance, propertyName, propertyValue);
				}
				else{
					manager.updateSharedDataEntry(instance, propertyName, propertyValue, false);
				}				
				returnDoc(response, localizedMessages.getString("WidgetServiceServlet.1"), "message");							 //$NON-NLS-1$ //$NON-NLS-2$
			} 
			catch (Exception ex) {			
				returnDoc(response, localizedMessages.getString("WidgetServiceServlet.4") + propertyName, "error");			 //$NON-NLS-1$ //$NON-NLS-2$
				_logger.error("error on doSetProperty", ex); //$NON-NLS-1$
			}
		}
		else{
			returnDoc(response, localizedMessages.getString("WidgetServiceServlet.3"), "error"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private void doGetWidget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
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
			_logger.debug("InvalidWidgetCallException:"+ex.getMessage());				 //$NON-NLS-1$
			returnDoc(response,ex.getMessage(), "error"); //$NON-NLS-1$
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
		_logger.debug(urlWidgetProxyServer.toString());
		//set the service url.
		/*
		if(urlWidgetAPIServer==null){
			urlWidgetAPIServer = new URL(request.getScheme() ,
					request.getServerName() ,
					request.getServerPort() , "/wookie/dwr/interface/widget.js");
			}
		_logger.debug(urlWidgetAPIServer.toString());
		 */
		WidgetInstance widgetInstance;
		IWidgetServiceManager manager = getServiceManager(session, localizedMessages); 
			
		if (widgetId != null){
			widgetInstance = manager.getWidgetInstanceById(apiKey, userId, sharedDataKey, widgetId);
		} 
		else {
			widgetInstance = manager.getWidgetInstance(apiKey, userId, sharedDataKey, serviceType);
		}

		if(widgetInstance!=null){
			// generate a key, url etc
			//doForward(request, response, _okPage);
			formatReturnDoc(request, response, widgetInstance.getWidget(), widgetInstance.getIdKey(), widgetInstance.getOpensocialToken());
		}
		else{
			try {
				Widget widget;
				// Widget ID or Widget Type?
				if (widgetId != null){
					widget = manager.getWidgetById(widgetId);
				} 
				else {
					// does this type of widget exist?
					widget = manager.getDefaultWidgetByType(serviceType);					
				}

				// generate a nonce
				String nonce = RandomGUID.getUniqueID("nonce-");				 //$NON-NLS-1$

				// now use SHA hash on the nonce				
				String hashKey = HashGenerator.getInstance().encrypt(nonce);	

				// get rid of any chars that might upset a url...
				hashKey = hashKey.replaceAll("=", ".eq."); //$NON-NLS-1$ //$NON-NLS-2$
				hashKey = hashKey.replaceAll("\\?", ".qu."); //$NON-NLS-1$ //$NON-NLS-2$
				hashKey = hashKey.replaceAll("&", ".am."); //$NON-NLS-1$ //$NON-NLS-2$
				hashKey = hashKey.replaceAll("\\+", ".pl."); //$NON-NLS-1$ //$NON-NLS-2$


				Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("opensocial"); //$NON-NLS-1$
				widgetInstance = manager.addNewWidgetInstance(apiKey, userId, sharedDataKey, widget, nonce, hashKey, properties);
				_logger.debug("new widgetinstance added"); //$NON-NLS-1$
				formatReturnDoc(request, response, widgetInstance.getWidget(), widgetInstance.getIdKey(), widgetInstance.getOpensocialToken());
			} 
			catch (WidgetTypeNotSupportedException ex) {
				// widget not supported	
				// Here we will return a key to a holding page - ie no widget found
				try {
					Widget unsupportedWidget = manager.getDefaultWidgetByType("unsupported"); //$NON-NLS-1$
					formatReturnDoc(request, response, unsupportedWidget, "0000", ""); //$NON-NLS-1$ //$NON-NLS-2$
				} 
				catch (WidgetTypeNotSupportedException e) {	
					_logger.debug("WidgetTypeNotSupportedException:"+ex.getMessage());				 //$NON-NLS-1$
					returnDoc(response,ex.getMessage(), "error"); //$NON-NLS-1$
				}												
			}
			catch (SystemUnavailableException ex) {
				_logger.debug("System Unavailable:"+ex.getMessage());				 //$NON-NLS-1$

				returnDoc(response, ex.getMessage(), "error"); //$NON-NLS-1$
			}
		}
	}  	

	private void formatReturnDoc(HttpServletRequest request, HttpServletResponse response, Widget widget, String key, String token) throws IOException{
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
		out.println("<title>"+widget.getWidgetTitle()+"</title>"); //$NON-NLS-1$ //$NON-NLS-2$
		out.println("<height>"+widget.getHeight()+"</height>"); //$NON-NLS-1$ //$NON-NLS-2$
		out.println("<width>"+widget.getWidth()+"</width>"); //$NON-NLS-1$ //$NON-NLS-2$
		out.println("<maximize>"+widget.isMaximize()+"</maximize>"); //$NON-NLS-1$ //$NON-NLS-2$
		out.println("</widgetdata>"); //$NON-NLS-1$

	}



	private void returnDoc(HttpServletResponse response, String message, String tagName) throws IOException {
		//_logger.error("returnDoc called: "+ message + tagName);
		StringBuffer envelope = new StringBuffer();	
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		envelope.append(XMLDECLARATION);					
		envelope.append("<"+tagName+">"); //$NON-NLS-1$ //$NON-NLS-2$
		envelope.append(message);
		envelope.append("</"+tagName+">"); //$NON-NLS-1$ //$NON-NLS-2$
		//_logger.debug("Call to getWidget failed:" + message);		
		out.println(envelope.toString());
		//out.flush();
	}	



	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}   
	
	private String getSharedDataKey(HttpServletRequest request){
		return String.valueOf((request.getParameter("apikey")+":"+request.getParameter("shareddatakey")).hashCode());	 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	/**
	 * Notifies widgets that participants have been updated
	 * @param session
	 * @param manager
	 * @param instance
	 */
	private void notifyWidgets(HttpSession session, IWidgetServiceManager manager, WidgetInstance instance){
		ServletContext ctx = session.getServletContext();
		ServerContext sctx = ServerContextFactory.get(ctx);
		String currentPage = instance.getWidget().getUrl();
		ScriptBuffer script = new ScriptBuffer();
		script.appendScript("wave.onParticipantUpdate();");
		Collection<?> pages = sctx.getScriptSessionsByPage(currentPage);
		for (Iterator<?> it = pages.iterator(); it.hasNext();){
			ScriptSession otherSession = (ScriptSession) it.next();
			otherSession.addScript(script);
		}
	}
	
	/**
	 * Return a handle on a service manager localized for the session
	 * @param session
	 * @param localizedMessages
	 * @return
	 */
	private IWidgetServiceManager getServiceManager(HttpSession session, Messages localizedMessages){
		IWidgetServiceManager manager = (IWidgetServiceManager)session.getAttribute(WidgetServiceManager.class.getName());				
		if(manager == null){
			manager = new WidgetServiceManager(localizedMessages);
			session.setAttribute(WidgetServiceManager.class.getName(), manager);
		}
		return manager;
	}

}