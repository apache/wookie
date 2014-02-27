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
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ServiceUnavailableException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.services.PreferencesService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Simple preferences controller used for AJAX requests from
 * widgets. Unlike PropertiesController, this does not require
 * requests to be signed using HMAC. It can only operate on
 * the preferences belonging to the associated auth token
 * provided.
 */
public class PreferencesController extends Controller{

	private static final long serialVersionUID = 5611606748176563026L;

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#index(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void index(HttpServletRequest request,
			HttpServletResponse response) throws UnauthorizedAccessException,
			IOException {
		
		AuthToken token = (AuthToken) request.getAttribute("org.apache.wookie.auth.AuthToken");
		if (token == null) throw new UnauthorizedAccessException();
		
		//
		// Get preferences
		//
		PreferencesService service = PreferencesService.Factory.getInstance();
		Collection<IPreference> preferences = service.getPreferences(token.getApiKey(), token.getWidgetId(), token.getContextId(), token.getViewerId());
		
		//
		// Write output as JSON
		//
		try {
			JSONObject json = new JSONObject();
			JSONArray array = new JSONArray();
			for (IPreference preference:preferences){
				JSONObject preferenceJson = new JSONObject();
				preferenceJson.put("dkey", preference.getName());
				preferenceJson.put("dvalue", preference.getValue());
				preferenceJson.put("readonly", preference.isReadOnly());
				array.put(preferenceJson);
			}
			json.put("Preferences", array);
			response.getWriter().write(json.toString());
		} catch (JSONException e) {
			throw new IOException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#create(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected boolean create(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws ResourceDuplicationException,
			InvalidParametersException, UnauthorizedAccessException,
			ServiceUnavailableException {

		AuthToken token = (AuthToken) request.getAttribute("org.apache.wookie.auth.AuthToken");
		if (token == null) throw new UnauthorizedAccessException();

		
		//
		// Get parameters
		//
		String name = request.getParameter("name");
		String value = request.getParameter("value");
		if (name == null || value == null) throw new InvalidParametersException();
		
		//
		// Get preferences service
		//
		PreferencesService service = PreferencesService.Factory.getInstance();
		
		//
		// Check whether the preference exists (update) or not (create)
		//
		String existing = service.getPreference(token.getApiKey(), token.getWidgetId(), token.getContextId(), token.getViewerId(), name);

		//
		// Set the preference
		//
		service.setPreference(token.getApiKey(), token.getWidgetId(), token.getContextId(), token.getViewerId(), name, value);
		
		if (existing == null){
			//
			// Created 201
			//
			return true;
		} else {
			//
			// Updated 200
			//
			return false;
		}
	}

	
}
