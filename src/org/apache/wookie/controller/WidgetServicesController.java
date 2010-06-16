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
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;
import org.apache.wookie.beans.IWidgetService;
import org.apache.wookie.beans.IWidgetType;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
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
	protected void show(String resourceId, HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException, IOException{
		IWidgetService ws = getWidgetService(resourceId);
		returnXml(WidgetServiceHelper.createXMLWidgetServiceDocument(ws, getLocalPath(request),getLocales(request)), response);
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#index(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void index(HttpServletRequest request, HttpServletResponse response) throws IOException{
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetService[] ws = persistenceManager.findAll(IWidgetService.class);
		boolean defaults = (request.getParameter("defaults") != null);
		returnXml(WidgetServiceHelper.createXMLWidgetServicesDocument(ws, getLocalPath(request),defaults, getLocales(request)), response);
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
		IWidgetService ws;
		try {
			ws = getWidgetService(resourceId);
			throw new ResourceDuplicationException();
		} catch (ResourceNotFoundException e) {
		    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
			ws = persistenceManager.newInstance(IWidgetService.class);
			ws.setServiceName(resourceId);
			return persistenceManager.save(ws);
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
		IWidgetService service = getWidgetService(resourceId);
		if (service == null) throw new ResourceNotFoundException();
		String serviceName = service.getServiceName();
		
		// if exists, remove from widget default table
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetDefault[] widgetDefaults = persistenceManager.findByValue(IWidgetDefault.class, "widgetContext", serviceName);
		persistenceManager.delete(widgetDefaults);
		
		// delete from the widget service table
		persistenceManager.delete(service);	
		// remove any widgetTypes for each widget that match
		IWidget[] widgets = persistenceManager.findWidgetsByType(serviceName);
		if (widgets == null||widgets.length==0) return true;
		for(IWidget widget : widgets){
			// remove any widget types for this widget
		    Iterator<IWidgetType> typesIter = widget.getWidgetTypes().iterator();
		    while(typesIter.hasNext()) {
		        IWidgetType widgetType = typesIter.next();
                if(serviceName.equalsIgnoreCase(widgetType.getWidgetContext())){
                    typesIter.remove();
                }
		    }
		    persistenceManager.save(widget);
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
		IWidgetService ws = getWidgetService(resourceId);
		if (ws == null) throw new ResourceNotFoundException();
		ws.setServiceName(name);
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		persistenceManager.save(ws);
	}
	
	// Utilities
	
	/**
	 * Find a widget service matching the supplied parameter either as the
	 * service name or service id
	 * @param resourceId
	 * @return
	 */
	private static IWidgetService getWidgetService(String resourceId) throws ResourceNotFoundException{
		if (resourceId == null) throw new ResourceNotFoundException();
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetService ws = persistenceManager.findById(IWidgetService.class, resourceId);
		if (ws == null) {
			IWidgetService[] wsa = persistenceManager.findByValue(IWidgetService.class, "serviceName", resourceId);
			if (wsa != null && wsa.length == 1) ws = wsa[0];
		}
		if (ws == null) throw new ResourceNotFoundException();
		return ws;
	}
	
}
