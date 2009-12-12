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
 * @author Scott Wilson
 *
 */
public abstract class Controller extends HttpServlet{

	private static final long serialVersionUID = 2791062551643568756L;

	/**
	 * Content type for XML output
	 */
	protected final String XML_CONTENT_TYPE = "text/xml;charset=\"UTF-8\"";

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String resource_id = locateRESTname(request);
		// If the resource identifier is not null, show otherwise index
		if (resource_id != null && resource_id.length() > 0) {
			try {
				show(resource_id, request, response);
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
		String id = locateRESTname(request);
		try {
			remove(id,request);
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
		String name = locateRESTname(request);
		try {
			if (create(name, request)){
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
		String id = locateRESTname(request);
		try {
			update(id,request);
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
	protected void show(String resource_id, HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException, UnauthorizedAccessException,IOException{
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
	protected boolean create(String resource_id, HttpServletRequest request) throws ResourceDuplicationException, InvalidParametersException, UnauthorizedAccessException{return false;};
	
	/**
	 * Delete a resource
	 * @param resource_id
	 * @return true if the resource was successfully deleted
	 * @throws ResourceNotFoundException
	 */
	protected boolean remove(String resource_id, HttpServletRequest request) throws ResourceNotFoundException,UnauthorizedAccessException,InvalidParametersException{return false;};
	
	/**
	 * Update a resource
	 * @param resource_id
	 * @param request
	 * @throws ResourceNotFoundException
	 */
	protected void update(String resource_id, HttpServletRequest request) throws ResourceNotFoundException,InvalidParametersException,UnauthorizedAccessException{};
	
	// Utilities

	/**
	 * Utility method for identifying the resource part of the URL
	 * note that pathinfo starts with a / for some reason
	 * @param the request
	 * @return the resource name
	 */
	protected String locateRESTname(HttpServletRequest request) {
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


}
