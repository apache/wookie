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

import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;

/**
 * Base class of RESTful controllers with common utility methods
 */
public abstract class Controller extends HttpServlet{

	private static final long serialVersionUID = 2791062551643568756L;

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
			if (create(resourceId, request)){
				response.setStatus(HttpServletResponse.SC_CREATED);
			} else {
				response.setStatus(HttpServletResponse.SC_OK);				
			}
		} catch (ResourceDuplicationException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT);// already exists with same name - need error message for this
		} catch (InvalidParametersException e){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST); 
		} catch (UnauthorizedAccessException e){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
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
			update(resourceId,request);
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
	 */
	protected boolean create(String resourceId, HttpServletRequest request) throws ResourceDuplicationException, InvalidParametersException, UnauthorizedAccessException{return false;};
	
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
	protected void update(String resourceId, HttpServletRequest request) throws ResourceNotFoundException,InvalidParametersException,UnauthorizedAccessException{};
	
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
		int p = path.indexOf('/');
		if (p > 0) {
			path = path.substring(0, p); // name isolated
		}
		if (path != null)
			path = path.trim();
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
	 * Get local path for server, for example to prepend
	 * onto icon URLs
	 * 
	 * @param request the request
	 * @return a string containing the local path
	 */
	protected String getLocalPath(HttpServletRequest request){
		try {
			URL path = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),"");
			return path.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Send XML back to client
	 * @param xml
	 * @param response
	 * @throws IOException
	 */
	protected void returnXml(String xml, HttpServletResponse response) throws IOException{
		response.setContentType(XML_CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println(xml);
	}
	
	/**
	 * Send HTML back to client
	 * @param html
	 * @param response
	 * @throws IOException
	 */
	protected void returnHtml(String html, HttpServletResponse response) throws IOException{
		response.setContentType(HTML_CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println(html);
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
	
	/**
	 * Returns an int value for the content-type of a request; this 
	 * can be used to create a switch statement that
	 * returns different representations based on the 
	 * request content-type. If no content-type is present in the
	 * request, this method will return HTML (1)
	 */
	protected int format(HttpServletRequest request){
		if (request.getContentType() == null) return HTML;
		if (request.getContentType().contains("xml"))
			return XML;
		if (request.getContentType().contains("json"))
			return JSON;
		return HTML;
	}


}
