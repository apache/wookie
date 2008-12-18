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
package org.tencompetence.widgetservice.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.manager.IWidgetAPIManager;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.IDBManager;

/**
 * API manager - manages DB calls for widget API
 * @author Paul Sharples
 * @version $Id
 *
 */
public class WidgetAPIManager implements IWidgetAPIManager {
	
	boolean showProcess = false;
	
	public static Logger _logger = Logger.getLogger(WidgetAPIManager.class.getName());
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#checkUserKey(java.lang.String)
	 */
	public WidgetInstance checkUserKey(String key){
		IDBManager dbManager = null;
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
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#getSharedDataForInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public synchronized SharedData[] getSharedDataForInstance(WidgetInstance instance){
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(SharedData.class);
			String sharedDataKey = instance.getSharedDataKey();		
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
		
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#updateSharedDataEntry(org.tencompetence.widgetservice.beans.WidgetInstance, java.lang.String, java.lang.String, boolean)
	 */
	public synchronized void updateSharedDataEntry(WidgetInstance widgetInstance, String name, String value, boolean append){
		if(showProcess){_logger.debug("############ Start updateshareddataentry called "+ Thread.currentThread().getName() +"############## name="+name+"value="+value);}
		IDBManager dbManager = null;
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
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#lockWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public synchronized void lockWidgetInstance(WidgetInstance instance){
		//doLock(instance, true);
		updateSharedDataEntry(instance, "isLocked", "true", false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#unlockWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public synchronized void unlockWidgetInstance(WidgetInstance instance){
		//doLock(instance, false);
		updateSharedDataEntry(instance, "isLocked", "false", false);
	}
	
	public synchronized boolean isInstanceLocked(WidgetInstance widgetInstance){
			try {
				if(getSharedDataValue(widgetInstance, "isLocked") == null){
					//addNewSharedDataEntry(widgetInstance, "isLocked", "false");
					return false;
				}
				else{
					return Boolean.valueOf(getSharedDataValue(widgetInstance, "isLocked"));
				}
			} 
			catch (Exception e) {
				_logger.error(e.getMessage());
				return false;
			}			
	}
	
	public String getSharedDataValue(WidgetInstance widgetInstance, String key){
		String sharedDataValue = null;
		for(SharedData sharedData : getSharedDataForInstance(widgetInstance)){
			if(sharedData.getDkey().equals(key)){
				sharedDataValue = sharedData.getDvalue();
				break;
			}
		}	
		return sharedDataValue;
	}
	
	/*
	protected synchronized void doLock(WidgetInstance instance, boolean flag){
		//TODO - update all widget instances to lock/unlock based on key
		String sharedDataKey = instance.getRunId() + "-" + instance.getEnvId() + "-" + instance.getServiceId();
		
		
		IDBManager dbManager = null;
		dbManager = DBManagerFactory.getDBManager();
		instance.setLocked(flag);
		try {
			dbManager.saveObject(instance);
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
		}
	}
	
	// this gets all other widgetinstances that use the same widget in same run, env, serv -etc
	// these will differ by username, so we are getting all the users instances in a given service
	public WidgetInstance[] getSiblingInstancesFromInstance(WidgetInstance widgetInstance){			
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(WidgetInstance.class);						
			crit.add( Restrictions.eq( "runId", widgetInstance.getRunId() ) );	
			crit.add( Restrictions.eq( "envId", widgetInstance.getEnvId() ) );
			crit.add( Restrictions.eq( "serviceId", widgetInstance.getServiceId() ) );	
			final List<WidgetInstance> sqlReturnList =  dbManager.getObjects(WidgetInstance.class, crit);
			WidgetInstance[] instances = sqlReturnList.toArray(new WidgetInstance[sqlReturnList.size()]);		
			return instances;
		} 
		catch (Exception ex) {
			dbManager.rollbackTransaction();
			_logger.error(ex.getMessage());
			return null;
		}		 
	}
	*/
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#addNewSharedDataEntry(org.tencompetence.widgetservice.beans.WidgetInstance, java.lang.String, java.lang.String)
	 */
	public synchronized void addNewSharedDataEntry(WidgetInstance instance, String name, String value) throws Exception{
		if(showProcess){_logger.debug("############ Start addNewSharedDataEntry called "+ Thread.currentThread().getName() +"############## name="+name+"value="+value);}
		IDBManager dbManager = null;
		
		try {
			dbManager = DBManagerFactory.getDBManager();
			String sharedDataKey = instance.getSharedDataKey();		
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


	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#getPreferenceForInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public Preference[] getPreferenceForInstance(WidgetInstance id) throws Exception{
		final IDBManager dbManager = DBManagerFactory.getDBManager();		
		Criteria crit = dbManager.createCriteria(Preference.class);		
		crit.add( Restrictions.eq( "widgetInstance", id ) );		
		final List<WidgetInstance> sqlReturnList =  dbManager.getObjects(WidgetInstance.class, crit);					
		Preference[] prefs = sqlReturnList.toArray(new Preference[sqlReturnList.size()]);
		return prefs;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#updatePreference(org.tencompetence.widgetservice.beans.WidgetInstance, java.lang.String, java.lang.String)
	 */
	public void updatePreference(WidgetInstance widgetInstance, String name, String value) throws Exception{
		final IDBManager dbManager = DBManagerFactory.getDBManager();
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
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#addNewPreference(org.tencompetence.widgetservice.beans.WidgetInstance, java.lang.String, java.lang.String)
	 */	
	public void addNewPreference(WidgetInstance widgetInstance, String name, String value) throws Exception{
		final IDBManager dbManager = DBManagerFactory.getDBManager();	
		Preference pref = new Preference();
		pref.setWidgetInstance(widgetInstance);
		pref.setDkey(name);
		pref.setDvalue(value);	
		dbManager.saveObject(pref);				
	}

}
