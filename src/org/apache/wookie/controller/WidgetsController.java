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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;
import org.apache.wookie.beans.IWidgetService;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
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

	/**
	 * Install a new Widget by saving it in the deploy folder
	 * Note: a Widget must have a .wgt extension!
	 */
	@Override
	protected boolean create(String resourceId, HttpServletRequest request)
			throws ResourceDuplicationException, InvalidParametersException,
			UnauthorizedAccessException {
				
		Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$
		final String DEPLOY_FOLDER = getServletContext().getRealPath(properties.getString("widget.deployfolder"));//$NON-NLS-1$
		FileItemFactory factory = new DiskFileItemFactory();
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			@SuppressWarnings("unchecked")
			List <FileItem> items = upload.parseRequest(request);
			
			for (FileItem item: items){
				File saveFile = new File(DEPLOY_FOLDER + "/" + item.getName());
				item.write(saveFile);
			}
			
		} catch (FileUploadException e) {
			throw new InvalidParametersException();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
