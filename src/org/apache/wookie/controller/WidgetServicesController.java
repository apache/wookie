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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetDefault;
import org.apache.wookie.beans.WidgetService;
import org.apache.wookie.beans.WidgetType;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.helpers.WidgetServiceHelper;

/**
 * <p>Controller for widget service resources.</p>
 * 
 * <p>Supports the following methods:</p>
 * 
 * <ul>
 * <li>GET /services- index</li>
 * <li>GET /services/{id} - show</li>
 * <li>GET /services/{name} - show</li>
 * <li>POST /services/{name} - create <em>requires authentication</em></li>
 * <li>PUT /services/{id} {name} - rename <em>requires authentication</em></li>
 * <li>DELETE /services/{id} - remove <em>requires authentication</em></li>
 * </ul>
 * @author scott
 *
 */
public class WidgetServicesController extends Controller{

	private static final long serialVersionUID = 6652819258720061792L;
	
	// Implementation

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#show(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void show(String id, HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException, IOException{
		WidgetService ws = getWidgetService(id);
		returnXml(WidgetServiceHelper.createXMLWidgetServiceDocument(ws, getLocalPath(request)), response);
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#index(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void index(HttpServletRequest request, HttpServletResponse response) throws IOException{
		WidgetService[] ws = WidgetService.findAll();
		boolean defaults = (request.getParameter("defaults") != null);
		returnXml(WidgetServiceHelper.createXMLWidgetServicesDocument(ws, getLocalPath(request),defaults), response);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#create(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected boolean create(String resourceId, HttpServletRequest request)
			throws ResourceDuplicationException,InvalidParametersException {
		return create(resourceId);
	}
	
	/**
	 * @param resourceId
	 * @return
	 * @throws ResourceDuplicationException
	 */
	public static boolean create(String resourceId) throws ResourceDuplicationException,InvalidParametersException{
		if (resourceId == null || resourceId.trim().equals("")) throw new InvalidParametersException();
		WidgetService ws;
		try {
			ws = getWidgetService(resourceId);
			throw new ResourceDuplicationException();
		} catch (ResourceNotFoundException e) {
			ws = new WidgetService();
			ws.setServiceName(resourceId);
			return ws.save();
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#remove(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected boolean remove(String resourceId, HttpServletRequest request)
			throws ResourceNotFoundException {
		return remove(resourceId);
	}

	/**
	 * Removes a widget service and any associated objects
	 * @param resourceId
	 * @return true if successfully deleted
	 * @throws ResourceNotFoundException
	 */
	public static boolean remove(String resourceId) throws ResourceNotFoundException {
		WidgetService service = getWidgetService(resourceId);
		if (service == null) throw new ResourceNotFoundException();
		String serviceName = service.getServiceName();
		
		// if exists, remove from widget default table
		WidgetDefault[] widgetDefaults = WidgetDefault.findByValue("widgetContext", serviceName);
		WidgetDefault.delete(widgetDefaults);
		
		// delete from the widget service table
		service.delete();	
		// remove any widgetTypes for each widget that match
		Widget[] widgets = Widget.findByType(serviceName);
		if (widgets == null||widgets.length==0) return true;
		for(Widget widget : widgets){
			// remove any widget types for this widget
			Set<?> types = widget.getWidgetTypes();
		    WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
		    for(int j=0;j<widgetTypes.length;++j){	
		    	if(serviceName.equalsIgnoreCase(widgetTypes[j].getWidgetContext())){
		    		widgetTypes[j].delete();
		    	}
			}
		}					
		return true;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#update(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void update(String resourceId, HttpServletRequest request)
			throws ResourceNotFoundException,InvalidParametersException {
		String name = request.getParameter("name");
		if (name == null || name.trim().equals("")) throw new InvalidParametersException();
		WidgetService ws = getWidgetService(resourceId);
		if (ws == null) throw new ResourceNotFoundException();
		ws.setServiceName(name);
		ws.save();
	}
	
	// Utilities
	
	/**
	 * Find a widget service matching the supplied parameter either as the
	 * service name or service id
	 * @param id
	 * @return
	 */
	private static WidgetService getWidgetService(String id) throws ResourceNotFoundException{
		if (id == null) throw new ResourceNotFoundException();
		WidgetService ws = null;
		if (isAnInteger(id)){
			ws = WidgetService.findById(Integer.valueOf(id));
		} else {
			WidgetService[] wsa = WidgetService.findByValue("serviceName", id);
			if (wsa != null && wsa.length == 1) ws = wsa[0];
		}
		if (ws == null) throw new ResourceNotFoundException();
		return ws;
	}
	
}
