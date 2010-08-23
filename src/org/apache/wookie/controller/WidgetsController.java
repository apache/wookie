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
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;
import org.apache.wookie.beans.IWidgetService;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.helpers.WidgetHelper;

/**
 * <p>Controller for widget resources.</p>
 * 
 * <p>Supports the following methods:</p>
 * 
 * <ul>
 * <li>GET widgets - index </li>
 * <li>GET widgets/{id} - show widget</li> 
 * <li>GET widgets/{category} - index widgets for category</li>
 * </ul>
 * @author scott
 * 
 */
public class WidgetsController extends Controller{

	private static final long serialVersionUID = 8759704878105474902L;
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
	
	// Implementation

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#show(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void show(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws ResourceNotFoundException, IOException {
	    // support "all" queries
		if ((resourceId == null) || resourceId.equals("")){
			index(resourceId, request, response);
			return;
		}
		// attempt to get specific widget by id
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget widget = persistenceManager.findById(IWidget.class, resourceId);
		// support queries by type
		if (widget == null) {
			IWidgetService[] services = persistenceManager.findByValue(IWidgetService.class, "serviceName", resourceId);
		    if (services != null && services.length == 1) {
			    IWidget[] widgets = persistenceManager.findWidgetsByType(resourceId);
		        returnXml(WidgetHelper.createXMLWidgetsDocument(widgets, getLocalPath(request), getLocales(request)),response);
		        return;
		    }
		}
		// return widget result
		if (widget == null) throw new ResourceNotFoundException();
		returnXml(WidgetHelper.createXMLWidgetsDocument(widget, getLocalPath(request), getLocales(request)),response);
	}	

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#index(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void index(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		index("", request, response);
	}

	/**
	 * Returns a list of widgets
	 * @param resourceId
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void index(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		IWidget widget = null;
		IWidget[] widgets;
		
		// If the request has the parameter ?all, return all widgets.
		// If the request contains a String resource identifier
		// such as "/chat", return all matching widgets
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		if (request.getParameter("all") != null
				|| (resourceId != null && !resourceId.equals(""))) {
			if (resourceId != null && !resourceId.equals("")) {
				widgets = persistenceManager.findWidgetsByType(resourceId);
			} else {
				widgets = persistenceManager.findAll(IWidget.class);
			}
			// Otherwise, return default widgets for the defined services. In
			// future we may want
			// to move this into the Services controller.
		} else {
			ArrayList<IWidget> widgetsarr = new ArrayList<IWidget>();
			for (IWidgetDefault widgetDefault : persistenceManager.findAll(IWidgetDefault.class)) {
				widget = widgetDefault.getWidget();
				if (!widget.getGuid().equals("http://notsupported")) {
					widgetsarr.add(widget);
				}
			}
			widgets = (IWidget[])widgetsarr.toArray(new IWidget[widgetsarr.size()]);
		}
		returnXml(WidgetHelper.createXMLWidgetsDocument(widgets, getLocalPath(request), getLocales(request)),response);
	}
}
