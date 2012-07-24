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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.ServiceUnavailableException;
import org.apache.wookie.exceptions.SystemUnavailableException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;

/**
 * Base class of RESTful controllers with common utility methods
 */
public abstract class Controller extends HttpServlet{

	private static final long serialVersionUID = 2791062551643568756L;
  static Logger _logger = Logger.getLogger(Controller.class.getName()); 

	/**
	 * Content type for XML output
	 */
	protected final String XML_CONTENT_TYPE = "text/xml;charset=\"UTF-8\"";
	
	/**
	 * Content type for JSON output
	 */
	protected final String JSON_CONTENT_TYPE = "application/json;charset=\"UTF-8\"";

	/**
	 * Content type for HTML output
	 */
	protected final String HTML_CONTENT_TYPE = "text/html;charset=\"UTF-8\"";

	/**
	 * Content type for ATOM output
	 */
	protected final String ATOM_CONTENT_TYPE = "application/atom+xml;charset=\"UTF-8\"";
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String resourceId = getResourceId(request);
		// If the resource identifier is not null, show otherwise index
		if (resourceId != null && resourceId.length() > 0) {
			try {
				show(resourceId, request, response);
				response.setStatus(HttpServletResponse.SC_OK);
			} catch (ResourceNotFoundException e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (UnauthorizedAccessException e){
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} else {
			try {
				index(request, response);
				response.setStatus(HttpServletResponse.SC_OK);
			} catch (UnauthorizedAccessException e){
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String resourceId = getResourceId(request);
		try {
			remove(resourceId,request);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ResourceNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (UnauthorizedAccessException e){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (InvalidParametersException e){
		  _logger.debug(e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String resourceId = getResourceId(request);
		try {
			if (create(resourceId, request, response)){
				response.setStatus(HttpServletResponse.SC_CREATED);
			} else {
				response.setStatus(HttpServletResponse.SC_OK);				
			}
		} catch (ResourceDuplicationException e) {
		  _logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_CONFLICT);// already exists with same name - need error message for this
		} catch (InvalidParametersException e){
		  _logger.error(e.getMessage(), e);
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage()); 
		} catch (UnauthorizedAccessException e){
		  _logger.error(e.getMessage(), e);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	    } catch (ServiceUnavailableException e){
	      _logger.error(e.getMessage(), e);
	      response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
	    }
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String resourceId = getResourceId(request);
		try {
			update(resourceId,request,response);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ResourceNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (InvalidParametersException e){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST); 
		} catch (UnauthorizedAccessException e){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	// Implementation - override these methods

	/**
	 * Return a representation of the specified resource
	 * @param resource_id
	 * @param request
	 * @param response
	 */
	protected void show(String resourceId, HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException, UnauthorizedAccessException,IOException{
	}

	/**
	 * Return a representation of the collection of all resources
	 * @param resource_id
	 * @param request
	 * @param response
	 */
	protected void index(HttpServletRequest request, HttpServletResponse response) throws UnauthorizedAccessException, IOException{
	}
	
	/**
	 * Create a new resource
	 * @param resource_id
	 * @param request
	 @return true if the resource was successfully created
	 * @throws ResourceDuplicationException
	 * @throws SystemUnavailableException 
	 */
	protected boolean create(String resourceId, HttpServletRequest request, HttpServletResponse response) throws ResourceDuplicationException, InvalidParametersException, UnauthorizedAccessException, ServiceUnavailableException{return false;};
	
	/**
	 * Delete a resource
	 * @param resource_id
	 * @return true if the resource was successfully deleted
	 * @throws ResourceNotFoundException
	 */
	protected boolean remove(String resourceId, HttpServletRequest request) throws ResourceNotFoundException,UnauthorizedAccessException,InvalidParametersException{return false;};
	
	/**
	 * Update a resource
	 * @param resource_id
	 * @param request
	 * @throws ResourceNotFoundException
	 */
	protected void update(String resourceId, HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException,InvalidParametersException,UnauthorizedAccessException{};
	
	// Utilities

	/**
	 * Utility method for identifying the resource part of the URL
	 * note that pathinfo starts with a / for some reason
	 * @param the request
	 * @return the resource name
	 */
	public static String getResourceId(HttpServletRequest request) {
		String path = request.getPathInfo(); // may be null, plain name or name plus
		// params
		if (path == null) {
			return null;
		}
		// extract REST name
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path != null)
			path = path.trim();
		// TODO:  policy requests have two urls. (this should work for all other cases)
		// fix for tomcat where one of the forward slashes is missing
		if(path.contains("http:/") && !path.contains("http://")){
		  path = path.replace("http:/", "http://");     
		} 		
		return path;
	}


	/**
	 * Check if a string is an integer
	 * Useful for parsing resource Ids
	 * @param string
	 * @return true if the string represents an integer
	 */
	protected static boolean isAnInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	/**
	 * Send XML back to client
	 * @param xml
	 * @param response
	 * @throws IOException
	 */
	protected void returnXml(String xml, HttpServletResponse response) throws IOException{
		output(xml,response, XML_CONTENT_TYPE);
	}
	
	/**
	 * Send HTML back to client
	 * @param html
	 * @param response
	 * @throws IOException
	 */
	protected void returnHtml(String html, HttpServletResponse response) throws IOException{
		output(html,response, HTML_CONTENT_TYPE);
	}
	
	/**
	 * Send JSON back to client
	 * @param json the JSON string
	 * @param response the response object
	 * @throws IOException
	 */
	protected void returnJson(String json, HttpServletResponse response) throws IOException{
		output(json,response, JSON_CONTENT_TYPE);
	}
	
	/**
	 * Send Atom back to client
	 * @param xml
	 * @param response
	 * @throws IOException
	 */
	protected void returnAtom(String xml, HttpServletResponse response) throws IOException{
		output(xml,response, ATOM_CONTENT_TYPE);
	}
	
	/**
	 * Sends the output from the controller to the client
	 * @param content the content to write
	 * @param response the response object
	 * @param contentType the content type for the response
	 * @throws IOException
	 */
	private void output(String content, HttpServletResponse response, String contentType) throws IOException{
		response.setContentType(contentType);
		PrintWriter out = response.getWriter();
		out.println(content);	
	}
	
	/**
	 * Get any localization parameters for the request. Currently this 
	 * only accepts a single locale rather than a list, support for
	 * lists of preferred locales could be added later, for example
	 * using the accept-header format
	 * @param request
	 * @return an array of locales, or null if no parameter present
	 */
	protected String[] getLocales(HttpServletRequest request){
		String locale = request.getParameter("locale");
		if (locale == null) return null;
		return new String[]{locale};
	}
	
  protected static final int XML = 0;
  protected static final int HTML = 1;
  protected static final int JSON = 2;
  protected static final int ATOM = 3;
  protected static final int WIDGET = 4;
	
  /**
   * Returns an int value for the Accept header of a request; this can be used
   * to create a switch statement that returns different representations based
   * on the requested media type.
   * 
   * If there is no Accept header, we also check for a "format" query parameter
   * to support clients which are unable to set the Accept header.
   * 
   * If no media type requirement is present in the request, this method will
   * return HTML (1).
   */
  protected int format(HttpServletRequest request) {
    String type = request.getHeader("Accept");

    // check for format parameters in the request
    if (request.getParameter("format") != null) {
        type = request.getParameter("format");
    } 
    
    if (type == null){
        return HTML;
    }
    
    if (type.contains("xml"))
      return XML;
    if (type.contains("json"))
      return JSON;
    if (type.contains("atom"))
      return ATOM;
    if (type.contains("application/widget"))
      return WIDGET;
    return HTML;
  }

	/**
	 * Get a URL for a resource on the current Wookie server; this is either determined from the request or overridden by properties
	 * defined in widgetserver.properties to support virtual hosts
	 * @param request the originating request
	 * @param path an optional path to the resource
	 * @return a URL pointing to the resource on the Wookie server
	 * @throws MalformedURLException
	 */
	protected static URL getWookieServerURL(HttpServletRequest request, String path) throws MalformedURLException{

    //
    // Use the request to generate the initial URL components
    //
    String scheme = request.getScheme();
    String serverName = request.getServerName();
    int serverPort = request.getServerPort();
    
    //
    // Override with configuration properties where present
    //
    Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$
    if (properties.getString("widget.server.scheme")!=null && !properties.getString("widget.server.scheme").trim().equals("")) scheme = properties.getString("widget.server.scheme"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    if (properties.getString("widget.server.hostname")!=null && !properties.getString("widget.server.hostname").trim().equals("")) serverName = properties.getString("widget.server.hostname"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    if (properties.getString("widget.server.port")!=null && !properties.getString("widget.server.port").trim().equals("")) serverPort = Integer.parseInt(properties.getString("widget.server.port")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    //
    // Construct and return URL
    //
    return new URL(scheme, serverName, serverPort, path);
	}

}
