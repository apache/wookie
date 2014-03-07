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

import org.apache.commons.io.IOUtils;
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
import org.json.JSONArray;
import org.json.JSONObject;

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

	
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//
		// For now we just execute the show() method. In future we could return a JSON array of all properties
		//
		String name = request.getParameter("propertyname"); //$NON-NLS-1$
		try {
			show(name, request, response);
		} catch (ResourceNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch(UnauthorizedAccessException e1){
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}



	@Override
	protected void show(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws ResourceNotFoundException,
			UnauthorizedAccessException, IOException {
		
		AuthToken authToken = getAuthTokenFromRequest(request);		
		if (authToken == null) throw new ResourceNotFoundException();
		if (resourceId == null || resourceId.trim().equals("")) throw new ResourceNotFoundException();
		String value = null;
		// Note that preferences and shared data keys may be the same!
		// We let the shared data values override.
		
		value = PreferencesService.Factory.getInstance().getPreference(authToken.getApiKey(), authToken.getWidgetId(), authToken.getContextId(), authToken.getViewerId(), resourceId);
		ISharedData data = new SharedContext(authToken).getSharedData(resourceId);
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
			found = updatePreference(authToken, name, null, false);
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
	private static void createOrUpdate(HttpServletRequest request)
	throws InvalidParametersException,UnauthorizedAccessException {

		AuthToken authToken = getAuthTokenFromRequest(request);
		if (authToken == null) throw new InvalidParametersException();

		//
		// The data we need is in JSON in the request body, structured as:
		//
		// {
		//   {"preferences" {[
		//      {"name":"pref1", "value":"pass", "readOnly":0}
		//   ]},
		//   {"shareddata" {[
		//      {"name":"sd1", "value":"pass"}
		//   ]}
		// }
		//
		//
		
		try {
			String body = IOUtils.toString(request.getInputStream());
			JSONObject json = new JSONObject(body);
			if (json != null){
				if (json.has("preferences")){
					JSONArray array = json.getJSONArray("preferences");
					if (array != null){
						for (int i=0;i<array.length();i++){
							JSONObject property = array.getJSONObject(i);
							if (!property.has("name")) throw new InvalidParametersException();
							String name = property.getString("name");
							if (name != null && !name.trim().equals("")){
								String value = property.getString("value");
								boolean readOnly = property.getBoolean("readOnly");
								updatePreference(authToken, name, value, readOnly);
							} else {
								throw new InvalidParametersException();
							}
						}
					}
				}
				if (json.has("shareddata")){
					JSONArray array = json.getJSONArray("shareddata");
					if (array != null){
						for (int i=0;i<array.length();i++){
							JSONObject property = array.getJSONObject(i);
							if (!property.has("name")) throw new InvalidParametersException();
							String name = property.getString("name");
							if (name != null && !name.trim().equals("")){
								String value = property.getString("value");
								new SharedContext(authToken).updateSharedData(name, value, false);
								Notifier.notifyWidgets(request.getSession(), authToken, Notifier.STATE_UPDATED);
							} else {
								throw new InvalidParametersException();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidParametersException();
		}
	}

	/**
	 * Update a preference entry
	 * @param widgetInstance
	 * @param name
	 * @param value
	 */
	private static boolean updatePreference(AuthToken authToken, String name, String value, boolean readOnly){
        boolean found=false;
        String preference = PreferencesService.Factory.getInstance().getPreference(authToken.getApiKey(), authToken.getWidgetId(), authToken.getContextId(), authToken.getViewerId(), name);        
        if (preference != null) found=true;
   	    PreferencesService.Factory.getInstance().setPreference(authToken.getApiKey(), authToken.getWidgetId(), authToken.getContextId(), authToken.getViewerId(), name, value, readOnly);
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