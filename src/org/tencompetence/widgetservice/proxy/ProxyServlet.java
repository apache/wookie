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
package org.tencompetence.widgetservice.proxy;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.manager.IWidgetAPIManager;
import org.tencompetence.widgetservice.manager.IWidgetProxyManager;
import org.tencompetence.widgetservice.manager.impl.WidgetAPIManager;
import org.tencompetence.widgetservice.manager.impl.WidgetProxyManager;
import org.tencompetence.widgetservice.server.LocaleHandler;

/**
 * A web proxy servlet which will translate calls for content and return them as if they came from
 * this domain
 * @author Paul Sharples
 * @version $Id
 *
 */
public class ProxyServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;
	public static final String UNAUTHORISED_MESSAGE = "Unauthorised";
	
	static Logger fLogger = Logger.getLogger(ProxyServlet.class.getName());
	
	public void init(){}

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dealWithRequest(request, response, "post");	
	}  
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		dealWithRequest(request, response, "get");			
	}
		
	private void dealWithRequest(HttpServletRequest request, HttpServletResponse response, String httpMethod){
		try {
			PrintWriter out = response.getWriter();	
			
			Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties");
			boolean shouldFilter = properties.getBoolean("widget.proxy.usewhitelist");
			boolean checkDomain =  properties.getBoolean("widget.proxy.checkdomain");
			
			if(isValidUser(request, checkDomain)){
														
				IWidgetProxyManager proxyManager = new WidgetProxyManager();
				
				ProxyURLBean bean = new ProxyURLBean(request);		
				if(bean.isErrorFound()){
					out.print(bean.getErrorStr());
					fLogger.error("Error parsing url");
					return;
				}
				// no errors
				else{	
					fLogger.debug("URL to be proxied="+bean.getNewUrl().toExternalForm());
					boolean cFlag = true;
					// should we filter urls?
					if (shouldFilter){
						// set this to false until found
						cFlag=false;
						// check if url is in the white list from DB...
						if(proxyManager.isAllowed(bean.getNewUrl().toExternalForm())){
							fLogger.debug("is allowed");
							cFlag = true;				
						}
						// not in white list, so block
						else{
							out.print("<error>URL Blocked</error>");
							fLogger.debug("URL" + bean.getNewUrl().toExternalForm() + "Blocked");
							return;
						}
					}			
					if(cFlag){
						String res = "";
						String proxyUserName = request.getParameter("username");
						String proxyPassword = request.getParameter("password");
						ProxyClient proxyclient = new ProxyClient();
						if(proxyUserName != null){
							if(proxyPassword != null){								
								proxyclient.setProxyAuthConfig(proxyUserName, proxyPassword);
							}
						}										
						if(httpMethod.equals("get")){
							res = proxyclient.get(bean.getNewUrl().toExternalForm(), properties);
							fLogger.debug("GET called");
						}
						else{
							res = proxyclient.post(bean.getNewUrl().toExternalForm(),"XMLDATA-TODO", properties);
							fLogger.debug("POST called");
						}
						//TODO - find all the links etc & make them absolute - to make request come thru this servlet
						response.setContentType(proxyclient.getCType());						
						out.print(res);
					}			
				}
			
			}
			else{
				// unauthorised or different domain request
				out.print("<error>"+UNAUTHORISED_MESSAGE+"</error>");
			}
			
		}
		catch (Exception ex) {
			try {
				PrintWriter out = response.getWriter();	
				fLogger.error(ex.getMessage());
				out.print("<error>"+ex.getMessage()+"</error>");
			} 
			catch (IOException e) {
				// give up!
				fLogger.error(ex.getMessage());			}
		}
	}
	
	
	private boolean isValidUser(HttpServletRequest request, boolean checkDomain){
		if(isSameDomain(request, checkDomain) && isValidWidgetInstance(request)){
			return true;
		}
		return false;
	}
	
	private boolean isSameDomain(HttpServletRequest request, boolean checkDomain){
		if(!checkDomain) return true;
		String remoteHost = request.getRemoteHost();
		String serverHost = request.getServerName();
		fLogger.debug("remote host:"+remoteHost);
		fLogger.debug("server host:"+ serverHost);
		if(remoteHost.equals(serverHost)){
			return true;
		}
		return false;
	}
	
	private boolean isValidWidgetInstance(HttpServletRequest request){
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = (Messages)session.getAttribute(Messages.class.getName());
		if(localizedMessages == null){
			Locale locale = request.getLocale();
			localizedMessages = LocaleHandler.getInstance().getResourceBundle(locale);
			session.setAttribute(Messages.class.getName(), localizedMessages);			
		}
		String instanceId = request.getParameter("instanceid_key");
		if(instanceId == null) return false;
		IWidgetAPIManager manager = new WidgetAPIManager(localizedMessages);
		// check if instance is valid
		WidgetInstance widgetInstance = manager.checkUserKey(instanceId);			
		if(widgetInstance!=null){
			return true;
		}
		else{
			// check  if the default Shindig gadget key is being used
			Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("opensocial");
			if (properties.getBoolean("opensocial.enable") && properties.getString("opensocial.proxy.id").equals(instanceId)) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	/**
	 * 
	 * A class used to model a url both with and without a proxy address attached to it
	 *
	 */
	private class ProxyURLBean {	
		
		private URL fOriginatingUrl = null;
		private URL fNewUrl;
		private String fErrorStr;
		private boolean fErrorFound = false;
				
		public ProxyURLBean(HttpServletRequest request){			
			doParse(request);
		}	
		
		private void doParse(HttpServletRequest request){

			URL proxiedEndPointURL = null;
			String endPointURL = null;
			
			String file = request.getRequestURI();
			if (request.getQueryString() != null) {
			   file += '?' + request.getQueryString();
			}
			// the request didn't contain any params
			else{	
				fErrorFound = true;
				fErrorStr = "<error>Unable to obtain url from args</error>";
				return;
			}
			
			try {
				// build the requested path
				proxiedEndPointURL = new URL(request.getScheme() ,
							request.getServerName() ,
							request.getServerPort() , file);
				fOriginatingUrl = proxiedEndPointURL;
			} 
			catch (MalformedURLException ex) {
				fErrorFound = true;
				fErrorStr = "<error>URL error on proxy. " + ex.getMessage() + "</error>";
				return;
			}	
												
			// find where the url parameter is ..
			int idx = proxiedEndPointURL.toString().indexOf("url=");
			if(idx>-1){
			  // reconstruct the path to be proxied by removing the reference to this servlet
				endPointURL=proxiedEndPointURL.toString().substring(idx+4,proxiedEndPointURL.toString().length());
			}												
			try {
				fNewUrl = new URL(endPointURL);
			} 
			catch (Exception ex) {
				// try decoding the URL
				try {
					fNewUrl = new URL(URLDecoder.decode(endPointURL, "UTF-8"));
				} catch (MalformedURLException e) {
					fErrorFound = true;
					fErrorStr = "<error>URL error on request. " + e.getMessage() + "</error>";	
				} catch (UnsupportedEncodingException e) {
					fErrorFound = true;
					fErrorStr = "<error>URL error on request. " + e.getMessage() + "</error>";	
				}
				
			}
		}

		public URL getOriginatingUrl() {
			return fOriginatingUrl;
		}

		public URL getNewUrl() {
			
			return fNewUrl;
		}

		public String getErrorStr() {
			return fErrorStr;
		}

		public boolean isErrorFound() {
			fLogger.debug("orig:"+fOriginatingUrl);
			fLogger.debug("new:"+fNewUrl);
			return fErrorFound;
		}
		
	}
	
}

