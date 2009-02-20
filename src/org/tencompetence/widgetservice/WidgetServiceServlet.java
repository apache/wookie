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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.InvalidWidgetCallException;
import org.tencompetence.widgetservice.exceptions.SystemUnavailableException;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;
import org.tencompetence.widgetservice.manager.IWidgetServiceManager;
import org.tencompetence.widgetservice.manager.impl.WidgetServiceManager;
import org.tencompetence.widgetservice.util.HashGenerator;
import org.tencompetence.widgetservice.util.RandomGUID;

/**
 * Servlet implementation class for Servlet: WidgetService
 * @author Paul Sharples
 * @version $Id: WidgetServiceServlet.java,v 1.10 2009-02-20 23:51:00 scottwilson Exp $ 
 *
 */
 public class WidgetServiceServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    
	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(WidgetServiceServlet.class.getName());	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String CONTENT_TYPE = "text/xml;charset=\"UTF-8\""; 	
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
		try {
			String requestId = request.getParameter("requestid");
			if(requestId.equals("getwidget")){
				doGetWidget(request, response);
			}
			else if(requestId.equals("stopwidget")){
				doStopWidget(request, response);
			}
			else if(requestId.equals("resumewidget")){
				doResumeWidget(request, response);
			}
			else if(requestId.equals("setpublicproperty")){
				doSetProperty(request, response, false);
			}		
			else if(requestId.equals("setpersonalproperty")){
				doSetProperty(request, response, true );
			}
			else {
				returnDoc(response, "No valid requestid was found.", "error");
			}
		
		} 
		catch (Exception ex) {					
			_logger.error("Error in doGet():", ex);
		}
	}
	
	private void doStopWidget(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String userId = request.getParameter("userid");	
		String sharedDataKey = request.getParameter("shareddatakey");	
		String serviceType= request.getParameter("servicetype");
		
		IWidgetServiceManager wsm = new WidgetServiceManager();	
		WidgetInstance widgetInstance = wsm.getWidgetInstance(userId, sharedDataKey, serviceType);		
		if(widgetInstance!=null){
			wsm.lockWidgetInstance(widgetInstance);
			returnDoc(response,"completed", "message");
		}
		else{
			returnDoc(response,"Widget instance does not exist", "error");
		}
		_logger.debug("*** stop widget called ****");
		_logger.debug("*** "+ userId + " ****");
		_logger.debug("***************************");
	}
	
	private void doResumeWidget(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String userId = request.getParameter("userid");
		String sharedDataKey = request.getParameter("shareddatakey");	
		String serviceType= request.getParameter("servicetype");
		IWidgetServiceManager wsm = new WidgetServiceManager();	
		WidgetInstance widgetInstance = wsm.getWidgetInstance(userId, sharedDataKey, serviceType);		
		if(widgetInstance!=null){
			wsm.unlockWidgetInstance(widgetInstance);
			returnDoc(response,"completed", "message");
		}
		else{
			returnDoc(response,"Widget instance does not exist", "error");
		}
		_logger.debug("*** resume widget called ****");
		_logger.debug("*** "+ userId + " ****");
		_logger.debug("***************************");
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
		String userId = request.getParameter("userid");
		String sharedDataKey = request.getParameter("shareddatakey");	
		String serviceType= request.getParameter("servicetype");
		String propertyName = request.getParameter("propertyname");
		String propertyValue = request.getParameter("propertyvalue");
		
		IWidgetServiceManager wsm = new WidgetServiceManager();	
		WidgetInstance instance = wsm.getWidgetInstance(userId, sharedDataKey, serviceType);
		if(instance != null){
			try {
				if(isPersonalProperty){
					wsm.updatePreference(instance, propertyName, propertyValue);
				}
				else{
					wsm.updateSharedDataEntry(instance, propertyName, propertyValue, false);
				}				
				returnDoc(response,"completed", "message");							
			} 
			catch (Exception ex) {			
				returnDoc(response,"could not set property for " + propertyName, "error");			
				_logger.error("error on doSetProperty", ex);
			}
		}
		else{
			returnDoc(response,"Widget instance does not exist", "error");
		}
	}
		
	private void doGetWidget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userid");
		String sharedDataKey = request.getParameter("shareddatakey");	
		String serviceType = request.getParameter("servicetype");
		String widgetId = request.getParameter("widgetid");
		
		try {						
			if(userId==null || sharedDataKey==null || (serviceType==null && widgetId==null)){
				throw new InvalidWidgetCallException();
			}
		} 
		catch (InvalidWidgetCallException ex) {
			_logger.debug("InvalidWidgetCallException:"+ex.getMessage());				
			returnDoc(response,ex.getMessage(), "error");
			return;
		}
		
		// set the proxy url.
		if(urlWidgetProxyServer==null){
		urlWidgetProxyServer = new URL(request.getScheme() ,
				request.getServerName() ,
				request.getServerPort() , "/wookie/proxy");
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
				
		IWidgetServiceManager wsm = new WidgetServiceManager();	
		if (widgetId != null){
			widgetInstance = wsm.getWidgetInstanceById(userId, sharedDataKey, widgetId);
		} else {
			widgetInstance = wsm.getWidgetInstance(userId, sharedDataKey, serviceType);
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
					widget = wsm.getWidgetById(widgetId);
				} else {
					// does this type of widget exist?
					widget = wsm.getDefaultWidgetByType(serviceType);					
				}
				

				// generate a nonce
				String nonce = RandomGUID.getUniqueID("nonce-");				

				// now use SHA hash on the nonce				
				String hashKey = HashGenerator.getInstance().encrypt(nonce);	
				
				// get rid of any chars that might upset a url...
				hashKey = hashKey.replaceAll("=", ".eq.");
				hashKey = hashKey.replaceAll("\\?", ".qu.");
				hashKey = hashKey.replaceAll("&", ".am.");
				hashKey = hashKey.replaceAll("\\+", ".pl.");
				
				widgetInstance = wsm.addNewWidgetInstance(userId, sharedDataKey, widget, nonce, hashKey);
				_logger.debug("new widgetinstance added");
				formatReturnDoc(request, response, widgetInstance.getWidget(), widgetInstance.getIdKey(), widgetInstance.getOpensocialToken());
			} 
			catch (WidgetTypeNotSupportedException ex) {
				// widget not supported	
				// Here we will return a key to a holding page - ie no widget found
				try {
					Widget unsupportedWidget = wsm.getDefaultWidgetByType("unsupported");
					formatReturnDoc(request, response, unsupportedWidget, "0000", "");
				} 
				catch (WidgetTypeNotSupportedException e) {	
					_logger.debug("WidgetTypeNotSupportedException:"+ex.getMessage());				
					returnDoc(response,ex.getMessage(), "error");
				}												
			}
			catch (SystemUnavailableException ex) {
				_logger.debug("System Unavailable:"+ex.getMessage());				
								
				returnDoc(response, ex.getMessage(), "error");
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
		out.println("<widgetdata>");					
		out.print("<url>");
		out.print(urlWidget + "?idkey=" + key 
				//+ "&amp;url=" + urlWidgetAPIServer.toExternalForm()  
				+ "&amp;proxy=" + urlWidgetProxyServer.toExternalForm() 
				+ "&amp;st=" + token
		);
		out.println("</url>");
		out.println("<title>"+widget.getWidgetTitle()+"</title>");
		out.println("<height>"+widget.getHeight()+"</height>");
		out.println("<width>"+widget.getWidth()+"</width>");
		out.println("<maximize>"+widget.isMaximize()+"</maximize>");
		out.println("</widgetdata>");
	}
	
	
	
	private void returnDoc(HttpServletResponse response, String message, String tagName) throws IOException {
		//_logger.error("returnDoc called: "+ message + tagName);
		StringBuffer envelope = new StringBuffer();	
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		envelope.append(XMLDECLARATION);					
		envelope.append("<"+tagName+">");
		envelope.append(message);
		envelope.append("</"+tagName+">");
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

}