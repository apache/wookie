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
import org.apache.wookie.Messages;
import org.apache.wookie.beans.Whitelist;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.server.LocaleHandler;

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
						if(isAllowed(bean.getNewUrl().toExternalForm())){
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
		// check if instance is valid
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(instanceId);			
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
	 * Check to see if a given url appears in the whitelist
	 * @param aUrl
	 * @return
	 */
	public boolean isAllowed(String aUrl){					
		for (Whitelist whiteList : Whitelist.findAll()){
			// TODO - make this better then just comparing the beginning...
			if(aUrl.toLowerCase().startsWith(whiteList.getfUrl().toLowerCase()))			
				return true;
		}
		return false;		
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

