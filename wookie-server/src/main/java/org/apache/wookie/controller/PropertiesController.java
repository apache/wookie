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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.services.PreferencesService;

/**
 * REST implementation for widgetInstance
 *
 * POST: creates or updates a property for an instance
 * GET: returns the value of a specified property for an instance
 * PUT : creates or updates a property for an instance
 * DELETE: deletes a property
 */
public class PropertiesController extends Controller {

	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(PropertiesController.class.getName());	

	/**
	 * We only override doGet to allow tunneling requests through GET 
	 * for legacy clients
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String requestId = request.getParameter("requestid");//$NON-NLS-1$
		// If the request id is not null, show otherwise index
		if (requestId != null && requestId.length() > 0) {
			try {
				createOrUpdate(request);
			} catch (InvalidParametersException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			} catch (UnauthorizedAccessException e){
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} else {
			try {
				show(null, request, response);
				response.setStatus(HttpServletResponse.SC_OK);
			} catch (ResourceNotFoundException e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (UnauthorizedAccessException e){
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
	}

	@Override
	protected void show(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws ResourceNotFoundException,
			UnauthorizedAccessException, IOException {
		
		AuthToken authToken = getAuthTokenFromRequest(request);		
		if (authToken == null) throw new ResourceNotFoundException();
		String name = request.getParameter("propertyname"); //$NON-NLS-1$
		if (name == null || name.trim().equals("")) throw new ResourceNotFoundException();
		String value = null;
		// Note that preferences and shared data keys may be the same!
		// We let the shared data values override.
		
		value = PreferencesService.Factory.getInstance().getPreference(authToken.getApiKey(), authToken.getWidgetId(), authToken.getContextId(), authToken.getViewerId(), name);
		ISharedData data = new SharedContext(authToken).getSharedData(name);
		if (data != null) value = data.getDvalue();
		if (value == null) throw new ResourceNotFoundException();
		PrintWriter out = response.getWriter();
		out.write(value);
	}

	@Override
	protected boolean remove(String resourceId, HttpServletRequest request)
			throws ResourceNotFoundException,UnauthorizedAccessException,InvalidParametersException {
		if (request.getParameter("value") != null) throw new InvalidParametersException();//$NON-NLS-1$
		String name = request.getParameter("propertyname"); //$NON-NLS-1$
		
		AuthToken authToken = getAuthTokenFromRequest(request);
		if (authToken == null) throw new InvalidParametersException();
		
		if (name == null || name.trim().equals("")) throw new InvalidParametersException();
		
		boolean found = false;
		if (isPublic(request)){ 
			found = new SharedContext(authToken).removeSharedData(name);
			Notifier.notifyWidgets(request.getSession(), authToken, Notifier.STATE_UPDATED);
		} else {
			found = updatePreference(authToken, name, null);
		}
		if (!found) throw new ResourceNotFoundException();
		return true;
	}	

	@Override
	protected boolean create(String resourceId, HttpServletRequest request, HttpServletResponse response)
			throws ResourceDuplicationException, InvalidParametersException,
			UnauthorizedAccessException {
		createOrUpdate(request);
		return true;
	}

	@Override
	protected void update(String resourceId, HttpServletRequest request, HttpServletResponse response)
			throws ResourceNotFoundException, InvalidParametersException,UnauthorizedAccessException {
		createOrUpdate(request);
	}
		
	/**
	 * Creates or updates a property for the given request
	 * @param request
	 * @return true if the property was created, false if updated
	 * @throws InvalidParametersException
	 * @throws UnauthorizedAccessException
	 */
	public static void createOrUpdate(HttpServletRequest request)
	throws InvalidParametersException,UnauthorizedAccessException {
		String name = request.getParameter("propertyname"); //$NON-NLS-1$
		String value = request.getParameter("propertyvalue"); //$NON-NLS-1$
		
		AuthToken authToken = getAuthTokenFromRequest(request);
		if (authToken == null) throw new InvalidParametersException();
		
		if (name == null || name.trim().equals("")) throw new InvalidParametersException();
		
		if (isPublic(request)){ 
		    new SharedContext(authToken).updateSharedData(name, value, false);
			Notifier.notifyWidgets(request.getSession(), authToken, Notifier.STATE_UPDATED);
		} else {
			updatePreference(authToken, name, value);
		}
	}

	/**
	 * Update a preference entry
	 * @param widgetInstance
	 * @param name
	 * @param value
	 */
	public static boolean updatePreference(AuthToken authToken, String name, String value){
        boolean found=false;
        String preference = PreferencesService.Factory.getInstance().getPreference(authToken.getApiKey(), authToken.getWidgetId(), authToken.getContextId(), authToken.getViewerId(), name);        
        if (preference != null) found=true;
   	    PreferencesService.Factory.getInstance().setPreference(authToken.getApiKey(), authToken.getWidgetId(), authToken.getContextId(), authToken.getViewerId(), name, value);
        return found;
	}

	/// Utilities
	
	private static boolean isPublic(HttpServletRequest request){
		String is_public = request.getParameter("is_public"); //$NON-NLS-1$	
		if (is_public != null) if (is_public.equals("true")|| is_public.equals("1")) return true;
		String requestId = request.getParameter("requestid"); //$NON-NLS-1$
		if (requestId != null) if (requestId.equals("setpublicproperty")) return true;
		return false;
	}

}