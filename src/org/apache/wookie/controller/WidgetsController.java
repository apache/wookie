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
import java.util.ArrayList;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetDefault;
import org.apache.wookie.helpers.WidgetHelper;

/**
 * REST API for Widgets Methods: 
 * GET widgets (index) 
 * GET widgets/{id} (show widget) 
 * GET widgets/{category} (index widgets for category)
 * 
 * @author scott
 * 
 */
public class WidgetsController extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 8759704878105474902L;

	private static final String CONTENT_TYPE = "text/xml;charset=\"UTF-8\"";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String resource_id = locateRESTname(request);
		// If the resource identifier can resolve to an integer then
		// show the matching widget, otherwise return an index
		if (resource_id != null && resource_id.length() > 0 && isAnInteger(resource_id)) {
			show(resource_id, request, response);
		} else {
			index(resource_id, request, response);
		}

	}

	/**
	 * Returns a single widget
	 * @param id
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void show(String id, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Widget widget = null;
		widget = Widget.findById(Integer.valueOf(id));
		if (widget == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			response.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			out.println(WidgetHelper.createXMLWidgetsDocument(widget, getLocalPath(request)));
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	/**
	 * Returns a list of widgets
	 * @param type
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void index(String type, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Widget widget = null;
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		Widget[] widgets;
		
		// If the request has the parameter ?all, return all widgets.
		// If the request contains a String resource identifier
		// such as "/chat", return all matching widgets
		if (request.getParameter("all") != null
				|| (type != null && !type.equals(""))) {
			if (type != null && !type.equals("")) {
				widgets = Widget.findByType(type);
			} else {
				widgets = Widget.findAll();
			}
			// Otherwise, return default widgets for the defined services. In
			// future we may want
			// to move this into the Services controller.
		} else {
			ArrayList<Widget> widgetsarr = new ArrayList<Widget>();
			for (WidgetDefault widgetDefault : WidgetDefault.findAll()) {
				widget = Widget.findById(widgetDefault.getWidgetId());
				if (widget.getId() != 1) {
					widgetsarr.add(widget);
				}
			}
			widgets = (Widget[])widgetsarr.toArray(new Widget[widgetsarr.size()]);
		}
		out.println(WidgetHelper.createXMLWidgetsDocument(widgets, getLocalPath(request)));
	}


	/**
	 * Utility method for identifying the resource part of the URL
	 * note that pathinfo starts with a / for some reason
	 * @param the request
	 * @return the resource name
	 */
	String locateRESTname(HttpServletRequest req) {
		String path = req.getPathInfo(); // may be null, plain name or name plus
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
	 * 
	 * @param string
	 * @return
	 */
	private boolean isAnInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	/**
	 * Get local path for server
	 * 
	 * @param request
	 * @return a string of the local path
	 */
	private String getLocalPath(HttpServletRequest request){
		try {
			URL path = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),"");
			return path.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
