/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.widgetservice.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.helpers.Notifier;
import org.tencompetence.widgetservice.helpers.WidgetKeyManager;
import org.tencompetence.widgetservice.server.LocaleHandler;

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
		boolean setpublic = false;
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			String is_public = request.getParameter("is_public"); //$NON-NLS-1$		
			if (is_public != null){
				if (is_public.equals("true")|| is_public.equals("1")){
					setpublic = true;
				}
			}
		}
		doSetProperty(request, response, setpublic);
	}



	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response){					
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			try {
				String requestId = request.getParameter("requestid"); //$NON-NLS-1$
				if (requestId == null || requestId.equals("")){
					response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
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
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}


	/// Implementation

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