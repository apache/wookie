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
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.manager.WidgetProxyManager;

/**
 * A web proxy servlet which will translate calls for content and return them as if they came from
 * this domain
 * @author Paul Sharples
 * @version $Id
 *
 */
public class ProxyServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;
	
	static Logger _logger = Logger.getLogger(ProxyServlet.class.getName());
	
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
			WidgetProxyManager proxyManager = new WidgetProxyManager();

			Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties");
			boolean shouldFilter = properties.getBoolean("widget.filter");

			ProxyURLBean bean = new ProxyURLBean(request);		
			if(bean.isErrorFound()){
				out.print(bean.getErrorStr());
				_logger.error("Error parsing url");
				return;
			}
			// no errors
			else{	
				_logger.debug("URL to be proxied="+bean.getNewUrl().toExternalForm());
				boolean cFlag = true;
				// should we filter urls?
				if (shouldFilter){
					// set this to false until found
					cFlag=false;
					// check if url is in the white list from DB...
					if(proxyManager.isAllowed(bean.getNewUrl().toExternalForm())){
						_logger.debug("is allowed");
						cFlag = true;				
					}
					// not in white list, so block
					else{
						out.print("<error>URL Blocked</error>");
						_logger.debug("URL" + bean.getNewUrl().toExternalForm() + "Blocked");
						return;
					}
				}			
				if(cFlag){
					String res = "";
					ProxyClient p = new ProxyClient();
					if(httpMethod.equals("get")){
						res = p.get(bean.getNewUrl().toExternalForm());
						_logger.debug("GET called");
					}
					else{
						res = p.post("TODO", bean.getNewUrl().toExternalForm());
						_logger.debug("POST called");
					}
					//TODO - find all the links etc & make them absolute - to make request come thru this servlet
					response.setContentType(p.ctype);
					out.print(res);
				}			
			}
		}
		catch (Exception e) {
			_logger.error(e.getMessage());	
		}
	}
	
	/**
	 * 
	 * A class used to model a url both with and without a proxy address attached to it
	 *
	 */
	private class ProxyURLBean {	
		
		private URL _originatingUrl = null;
		private URL _newUrl;
		private String _errorStr;
		private boolean _errorFound = false;
				
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
				_errorFound = true;
				_errorStr = "<error>Unable to obtain url from args</error>";
				return;
			}
			
			try {
				// build the requested path
				proxiedEndPointURL = new URL(request.getScheme() ,
							request.getServerName() ,
							request.getServerPort() , file);
				_originatingUrl = proxiedEndPointURL;
			} 
			catch (MalformedURLException ex) {
				_errorFound = true;
				_errorStr = "<error>URL error on proxy. " + ex.getMessage() + "</error>";
				return;
			}	
												
			// find where the url parameter is
			int idx = proxiedEndPointURL.toString().indexOf("?url=");
			if(idx>-1){
			  // reconstruct the path to be proxied by removing the reference to this servlet
				endPointURL=proxiedEndPointURL.toString().substring(idx+5,proxiedEndPointURL.toString().length());
			}												
			try {
				
				_newUrl = new URL(endPointURL);
			} 
			catch (Exception ex) {
				_errorFound = true;
				_errorStr = "<error>URL error on request. " + ex.getMessage() + "</error>";	
			}
		}

		public URL getOriginatingUrl() {
			return _originatingUrl;
		}

		public URL getNewUrl() {
			return _newUrl;
		}

		public String getErrorStr() {
			return _errorStr;
		}

		public boolean isErrorFound() {
			return _errorFound;
		}
		
	}
	
}

