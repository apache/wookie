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
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.Whitelist;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.server.LocaleHandler;

/**
 * A web proxy servlet which will translate calls for content and return them as if they came from
 * this domain
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		dealWithRequest(request, response, "get");			
	}

	/**
	 * Check the validity of a proxy request, and execute it if it checks out  
	 * @param request
	 * @param response
	 * @param httpMethod
	 * @throws ServletException
	 */
	private void dealWithRequest(HttpServletRequest request, HttpServletResponse response, String httpMethod) throws ServletException{
		try {
			Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties");

			if(!isValidUser(request, properties.getBoolean("widget.proxy.checkdomain"))){
				response.sendError(HttpServletResponse.SC_FORBIDDEN,"<error>"+UNAUTHORISED_MESSAGE+"</error>");	
				return;
			}

			ProxyURLBean bean;
			try {
				bean = new ProxyURLBean(request);
			} catch (MalformedURLException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
				return;
			}		

			// should we filter urls?
			if (properties.getBoolean("widget.proxy.usewhitelist") && !isAllowed(bean.getNewUrl().toExternalForm())){
				response.sendError(HttpServletResponse.SC_FORBIDDEN,"<error>URL Blocked</error>");
				fLogger.warn("URL" + bean.getNewUrl().toExternalForm() + "Blocked");
				return;
			}	

			ProxyClient proxyclient = new ProxyClient(request);
			PrintWriter out = response.getWriter();	
			//TODO - find all the links etc & make them absolute - to make request come thru this servlet
			String output = "";
			if(httpMethod.equals("get")){
				output = proxyclient.get(bean.getNewUrl().toExternalForm(), properties);
			}else{	
				output = proxyclient.post(bean.getNewUrl().toExternalForm(),getXmlData(request), properties);
			}
			response.setContentType(proxyclient.getCType());
			out.print(output);
		}
		catch (Exception ex) {
			try {
				if (ex instanceof AuthenticationException){
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				} else {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
				}
				fLogger.error(ex.getMessage());
				throw new ServletException(ex);
			} catch (IOException e) {
				// give up!
				fLogger.error(ex.getMessage());	
				throw new ServletException(e);
			}
		}
	}

	/**
	 * Gets the content of the request
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private String getXmlData(HttpServletRequest request) throws IOException{
		// Note that we cannot use a Reader for this as we already
		// call getParameter() which works on an InputStream - and you
		// can only use an InputStream OR a Reader, not both.
		byte[] b = new byte[request.getContentLength()];
		request.getInputStream().read(b, 0, request.getContentLength());
		String xml = new String(b);
		return xml;
	}

	private boolean isValidUser(HttpServletRequest request, boolean checkDomain){
		return isSameDomain(request, checkDomain) && isValidWidgetInstance(request);
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
				}
			}

			// try to locate a POST form parameter instead
			if (endPointURL == null)	
				endPointURL=request.getParameter("url");
			
			// the request didn't contain any params, so throw an exception
			if (endPointURL == null)	
				throw new MalformedURLException("Unable to obtain url from args");
			
			try {
				// try decoding the URL
				fNewUrl = new URL(URLDecoder.decode(endPointURL, "UTF-8"));
			} catch (Exception ex) {
				fNewUrl = new URL(endPointURL);
			}
		}

		public URL getNewUrl() {
			return fNewUrl;
		}

	}

}

