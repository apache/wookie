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
import org.apache.wookie.Messages;
import org.apache.wookie.beans.Preference;
import org.apache.wookie.beans.SharedData;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.WidgetKeyManager;
import org.apache.wookie.server.LocaleHandler;

/**
 * REST implementation for widgetInstance
 *
 * POST: creates or updates a property for an instance
 * GET, PUT, DELETE: not implemented
 */
public class PropertiesController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(PropertiesController.class.getName());	

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		boolean setpersonal = true;
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			String is_public = request.getParameter("is_public"); //$NON-NLS-1$		
			if (is_public != null){
				if (is_public.equals("true")|| is_public.equals("1")){
					setpersonal = false;
				}
			}
		}
		doSetProperty(request, response, setpersonal);
	}



	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response){					
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			try {
				String requestId = request.getParameter("requestid"); //$NON-NLS-1$
				if (requestId == null || requestId.equals("")){
					doGetProperty(request, response);
				} else {
					if(requestId.equals("setpublicproperty")){ //$NON-NLS-1$
						doSetProperty(request, response, false);
					}		
					else if(requestId.equals("setpersonalproperty")){ //$NON-NLS-1$
						doSetProperty(request, response, true );
					}
					else {
						response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					}
				}
			} catch (Exception ex) {	
				ex.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}


	/// Implementation
	

	/**
	 * Get a property for an instance
	 * @param request
	 * @param response
	 */
	public static void doGetProperty(HttpServletRequest request, HttpServletResponse response){
		WidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
		if (instance == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		String propertyName = request.getParameter("propertyname"); //$NON-NLS-1$
		if (propertyName == null || propertyName.trim().equals("")){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}	
		
		String value = null;
		Preference pref = Preference.findPreferenceForInstance(instance, propertyName);
		if (pref != null) value = pref.getDvalue();
		SharedData data = SharedData.findSharedDataForInstance(instance, propertyName);
		if (data != null) value = data.getDvalue();
		
		if (value == null){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);	
			return;
		}
		try {
			PrintWriter out = response.getWriter();
			out.write(value);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param isPersonalProperty - If the boolean is set to true, then a preference will be set otherwise its shareddata
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void doSetProperty (HttpServletRequest request, HttpServletResponse response, boolean isPersonalProperty) throws ServletException, IOException {		
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String propertyName = request.getParameter("propertyname"); //$NON-NLS-1$
		String propertyValue = request.getParameter("propertyvalue"); //$NON-NLS-1$
		WidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
		if(instance != null){
			boolean found = false;
			if(isPersonalProperty){
				found = updatePreference(instance, propertyName, propertyValue);
			}else{
				found = updateSharedDataEntry(instance, propertyName, propertyValue, false);
				Notifier.notifyWidgets(request.getSession(), instance, Notifier.STATE_UPDATED);
			}	
			if (!found){
				response.setStatus(HttpServletResponse.SC_CREATED);
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
			}
		}
		else{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, localizedMessages.getString("WidgetServiceServlet.3")); //$NON-NLS-1$ 
		}
	}
	
	
	///// Business Methods
	
	/**
	 * 
	 * @param widgetInstance
	 * @param name
	 * @param value
	 */
	public static boolean updatePreference(WidgetInstance widgetInstance, String name, String value){
        boolean found=false;
        for (Preference preference : Preference.findPreferencesForInstance(widgetInstance)){
        	if(preference.getDkey().equals(name)){
        		// if the value is null we need to remove the tuple
        		if(value==null || value.equalsIgnoreCase("null")){  
        			preference.delete();     			
        		}
        		else{    
        			preference.setDvalue(value);
        			preference.save();
        		}
        		found=true;
        	}
        }
        if(!found){        	
    		Preference pref = new Preference();
    		pref.setWidgetInstance(widgetInstance);
    		pref.setDkey(name);
    		pref.setDvalue(value);	
    		pref.save();	
        }  
        return found;
	}
	
	public synchronized static boolean updateSharedDataEntry(WidgetInstance widgetInstance, String name, String value, boolean append){
		boolean found=false;
		for (SharedData sharedData : SharedData.findSharedDataForInstance(widgetInstance)){
			if(sharedData.getDkey().equals(name)){
				// if the value is null we need to remove the tuple
				if(value==null || value.equalsIgnoreCase("null")){   
					sharedData.delete();
				}
				else{    
					if(append){
						sharedData.setDvalue(sharedData.getDvalue() + value);
					}
					else{
						sharedData.setDvalue(value);
					}
					sharedData.save();
				}
				found=true;
			}       	
		}
		if(!found){     
			if(value!=null){
				String sharedDataKey = widgetInstance.getSharedDataKey();		
				SharedData sharedData= new SharedData();
				sharedData.setWidgetGuid(widgetInstance.getWidget().getGuid());
				sharedData.setSharedDataKey(sharedDataKey);
				sharedData.setDkey(name);
				sharedData.setDvalue(value);
				sharedData.save();
			}
		}
		return found;
	}


}