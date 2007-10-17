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
package org.tencompetence.widgetservice.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.DBManagerInterface;

/**
 * API manager - manages DB calls for widget API
 * @author Paul Sharples
 * @version $Id
 *
 */
public class WidgetAPIManager {
	
	boolean showProcess = false;
	
	static Logger _logger = Logger.getLogger(WidgetAPIManager.class.getName());
	
	/**
	 * Check that a request is valid by getting the hashed key and seeing if it exists in the DB
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public WidgetInstance checkUserKey(String key){
		DBManagerInterface dbManager = null;
		try {
			if (key == null) {
				return null;
			}
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(WidgetInstance.class);
			crit.add(Restrictions.eq("idKey", key));
			final List<WidgetInstance> sqlReturnList = dbManager.getObjects(
					WidgetInstance.class, crit);
			if (sqlReturnList.size() != 1) {
				return null;
			} 
			else {
				return (WidgetInstance) sqlReturnList.get(0);
			}
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
			return null;
		}		
	}

	/**
	 * Returns all sharedData records related to this instance
	 * 
	 * @param instance - a widget instance
	 * @return - shared data for a this instance
	 * @throws Exception 
	 */
	public synchronized SharedData[] getSharedDataForInstance(WidgetInstance instance){
		DBManagerInterface dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(SharedData.class);
			String sharedDataKey = instance.getRunId() + "-" + instance.getEnvId() + "-" + instance.getServiceId();			
			crit.add( Restrictions.eq( "sharedDataKey", sharedDataKey ) );			
			final List<SharedData> sqlReturnList =  dbManager.getObjects(SharedData.class, crit);
			SharedData[] sharedData = sqlReturnList.toArray(new SharedData[sqlReturnList.size()]);		
			return sharedData;
		} 
		catch (Exception ex) {
			dbManager.rollbackTransaction();
			_logger.error(ex.getMessage());
			return null;
		}
	}
		
	/**
	 * Add or update...
	 * @param widgetInstance
	 * @param name
	 * @param value
	 * @throws Exception 
	 */
	public synchronized void updateSharedDataEntry(WidgetInstance widgetInstance, String name, String value, boolean append){
		if(showProcess){_logger.debug("############ Start updateshareddataentry called "+ Thread.currentThread().getName() +"############## name="+name+"value="+value);}
		DBManagerInterface dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			boolean found=false;
			for (SharedData sharedData : getSharedDataForInstance(widgetInstance)){
				if(sharedData.getDkey().equals(name)){
					// if the value is null we need to remove the tuple
					if(value==null || value.equalsIgnoreCase("null")){        
						dbManager.deleteObject(sharedData);
					}
					else{    
						if(append){
							sharedData.setDvalue(sharedData.getDvalue() + value);
						}
						else{
							sharedData.setDvalue(value);
						}
						dbManager.saveObject(sharedData);
					}
					found=true;
				}       	
			}
			if(!found){     
				if(value!=null){
					addNewSharedDataEntry(widgetInstance, name, value);
				}
			}
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
		} 
        if(showProcess){ _logger.debug("############ End updateshareddataentry called "+ Thread.currentThread().getName() +"############## name="+name+"value="+value);}
	}
	
	/**
	 * Add a new shared data entry to the DB
	 * @param instance
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	public synchronized void addNewSharedDataEntry(WidgetInstance instance, String name, String value) throws Exception{
		if(showProcess){_logger.debug("############ Start addNewSharedDataEntry called "+ Thread.currentThread().getName() +"############## name="+name+"value="+value);}
		DBManagerInterface dbManager = null;
		
		try {
			dbManager = DBManagerFactory.getDBManager();
			String sharedDataKey = instance.getRunId() + "-" + instance.getEnvId() + "-" + instance.getServiceId();			
			SharedData sharedData= new SharedData();
			sharedData.setSharedDataKey(sharedDataKey);
			sharedData.setDkey(name);
			sharedData.setDvalue(value);	
			dbManager.saveObject(sharedData);
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
		}
		if(showProcess){_logger.debug("############ End addNewSharedDataEntry called "+ Thread.currentThread().getName() +"############## name="+name+"value="+value);}
	}


	/**
	 * Returns all preference records found which match a given WidgetInstance
	 * @param id
	 * @return - an array of preferences
	 * @throws Exception 
	 */
	public Preference[] getPreferenceForInstance(WidgetInstance id) throws Exception{
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();		
		Criteria crit = dbManager.createCriteria(Preference.class);		
		crit.add( Restrictions.eq( "widgetInstance", id ) );		
		final List<WidgetInstance> sqlReturnList =  dbManager.getObjects(WidgetInstance.class, crit);					
		Preference[] prefs = sqlReturnList.toArray(new Preference[sqlReturnList.size()]);
		return prefs;
	}
	
	/**
	 * Add or update...
	 * @param widgetInstance
	 * @param name
	 * @param value
	 * @throws Exception 
	 */
	public void updatePreference(WidgetInstance widgetInstance, String name, String value) throws Exception{
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
        boolean found=false;
        for (Preference preference :getPreferenceForInstance(widgetInstance)){
        	if(preference.getDkey().equals(name)){
        		// if the value is null we need to remove the tuple
        		if(value==null || value.equalsIgnoreCase("null")){       				
        			dbManager.deleteObject(preference);        			
        		}
        		else{    
        			preference.setDvalue(value);
        			dbManager.saveObject(preference);
        		}
        		found=true;
        	}
        }
     
        if(!found){        	
        	addNewPreference(widgetInstance, name, value);
        }       
	}
	
	/**
	 * Add a new preference for a widget instance
	 * @param widgetInstance
	 * @param name
	 * @param value
	 * @throws Exception
	 */	
	public void addNewPreference(WidgetInstance widgetInstance, String name, String value) throws Exception{
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();	
		Preference pref = new Preference();
		pref.setWidgetInstance(widgetInstance);
		pref.setDkey(name);
		pref.setDvalue(value);	
		dbManager.saveObject(pref);				
	}

}
