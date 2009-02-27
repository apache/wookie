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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;
import org.tencompetence.widgetservice.manager.IWidgetServiceManager;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.IDBManager;
import org.tencompetence.widgetservice.util.opensocial.OpenSocialUtils;

/**
 * A class to manage widget instances
 * @author Paul Sharples
 * @version $Id
 *
 */
public class WidgetServiceManager extends WidgetAPIManager implements IWidgetServiceManager {

	static Logger _logger = Logger.getLogger(WidgetServiceManager.class.getName());

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getDefaultWidgetByType(java.lang.String)
	 */
	public Widget getDefaultWidgetByType(String typeToSearch) throws WidgetTypeNotSupportedException {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		String sqlQuery = "SELECT widget.id, widget.widget_title, widget_description, widget_author, widget_icon_location, widget.url, widget.height, widget.width, widget.maximize, widget.guid "
			+ "FROM Widget widget, WidgetDefault widgetdefault "
			+ "WHERE widget.id = widgetdefault.widgetId "
			+ "AND widgetdefault.widgetContext='" + typeToSearch + "'";		

		Widget widget = (Widget)dbManager.createSQLQuery(sqlQuery).addEntity(Widget.class).uniqueResult();	
		if(widget==null){
			throw new WidgetTypeNotSupportedException("Widget type " + typeToSearch + " is not supported");
		}
		return widget;		 
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getWidgetById(java.lang.String)
	 */
	public Widget getWidgetById(String id)
	throws WidgetTypeNotSupportedException {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		String sqlQuery = "SELECT widget.id, widget.widget_title, widget_description, widget_author, widget_icon_location, widget.url, widget.height, widget.width, widget.maximize, widget.guid "
			+ "FROM Widget widget, WidgetDefault widgetdefault "
			+ "WHERE widget.guid = '" + id + "'";		

		Widget widget = (Widget)dbManager.createSQLQuery(sqlQuery).addEntity(Widget.class).uniqueResult();	
		if(widget==null){
			throw new WidgetTypeNotSupportedException("Widget " + id + " is not supported");
		}
		return widget;	
	}




	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getWidgetInstance(java.lang.String)
	 */
	public WidgetInstance getWidgetInstance(String key) {
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
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getwidgetInstancesForWidget(org.tencompetence.widgetservice.beans.Widget)
	 */
	public WidgetInstance[] getwidgetInstancesForWidget(Widget widget){			
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(WidgetInstance.class);						
			crit.add( Restrictions.eq( "widget", widget ) );								
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

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#deletePreferenceInstancesForWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public void deletePreferenceInstancesForWidgetInstance(WidgetInstance instance){				
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(Preference.class);						
			crit.add( Restrictions.eq( "widgetInstance", instance) );			
			final List<Preference> sqlReturnList =  dbManager.getObjects(Preference.class, crit);
			Preference[] preference = sqlReturnList.toArray(new Preference[sqlReturnList.size()]);	
			for(Preference sData : preference){
				dbManager.deleteObject(sData);
			}				
		} 
		catch (Exception ex) {
			dbManager.rollbackTransaction();
			_logger.error(ex.getMessage());			
		}
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#deleteSharedDataInstancesForWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public void deleteSharedDataInstancesForWidgetInstance(WidgetInstance instance){				
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(SharedData.class);
			String sharedDataKey = instance.getSharedDataKey();		
			crit.add( Restrictions.eq( "sharedDataKey", sharedDataKey ) );			
			final List<SharedData> sqlReturnList =  dbManager.getObjects(SharedData.class, crit);
			SharedData[] sharedData = sqlReturnList.toArray(new SharedData[sqlReturnList.size()]);	
			for(SharedData sData : sharedData){
				dbManager.deleteObject(sData);
			}			
		} 
		catch (Exception ex) {
			dbManager.rollbackTransaction();
			_logger.error(ex.getMessage());			
		}
	}


	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#widgetInstanceExists(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean widgetInstanceExists(String userId, String sharedDataKey, String serviceContext){
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		//got to exist in widgetinstance and also be registered as this type of context in widgetcontext		
		String sqlQuery =   "select " +
		"count(*) "
		+ "from WidgetInstance widgetinstance, WidgetType widgettype "
		+ "WHERE "
		+ "widgetinstance.userId ='" + userId + "' "
		+ "AND widgetinstance.sharedDataKey ='" + sharedDataKey + "' "												
		+ "AND widgettype.widgetContext ='" + serviceContext + "' "			
		+ "AND widgetinstance.widget = widgettype.widget"
		;							
		_logger.debug((sqlQuery));
		long count=0l; 				
		count = (Long) dbManager.createQuery(sqlQuery).uniqueResult();
		return (count == 1 ? true : false); 

	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getwidgetInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public WidgetInstance getWidgetInstance(String userId, String sharedDataKey, String serviceContext){
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		//got to exist in widgetinstance and also be registered as this type of context in widgetcontext		
		String sqlQuery =   "select widgetinstance " 							
			+ "from WidgetInstance widgetinstance, WidgetType widgettype "
			+ "WHERE "
			+ "widgetinstance.userId ='" + userId + "' "
			+ "AND widgetinstance.sharedDataKey ='" + sharedDataKey + "' "															
			+ "AND widgettype.widgetContext ='" + serviceContext + "' "			
			+ "AND widgetinstance.widget = widgettype.widget"
			;							
		_logger.debug((sqlQuery));				
		List<?> sqlReturnList = dbManager.createQuery(sqlQuery).list();
		if(sqlReturnList.size()!=1){
			return null;
		}
		else{
			return (WidgetInstance)sqlReturnList.get(0);
		}
	}




	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getWidgetInstanceById(java.lang.String, java.lang.String, java.lang.String)
	 */
	public WidgetInstance getWidgetInstanceById(String userId,
			String sharedDataKey, String widgetId) {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		//got to exist in widgetinstance and also be registered as this type of context in widgetcontext		
		String sqlQuery =   "select widgetinstance " 							
			+ "from WidgetInstance widgetinstance "
			+ "WHERE "
			+ "widgetinstance.userId ='" + userId + "' "
			+ "AND widgetinstance.sharedDataKey ='" + sharedDataKey + "' "															
			+ "AND widgetinstance.widget.guid = '" + widgetId + "' "			
			;							
		_logger.debug((sqlQuery));				
		List<?> sqlReturnList = dbManager.createQuery(sqlQuery).list();
		if(sqlReturnList.size()!=1){
			return null;
		}
		else{
			return (WidgetInstance)sqlReturnList.get(0);
		}
	}




	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#addNewWidgetInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.tencompetence.widgetservice.beans.Widget, java.lang.String, java.lang.String)
	 */
	public WidgetInstance addNewWidgetInstance(String userId, String sharedDataKey, Widget widget, String nonce, String idKey, Configuration properties) {		
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		WidgetInstance widgetInstance = new WidgetInstance();
		try {
			widgetInstance.setUserId(userId);
			widgetInstance.setSharedDataKey(sharedDataKey);
			widgetInstance.setIdKey(idKey);
			widgetInstance.setNonce(nonce);
			// set the defaults widget for this type			
			widgetInstance.setWidget(widget);						
			widgetInstance.setHidden(false);
			widgetInstance.setShown(true);
			widgetInstance.setUpdated(false);
			widgetInstance.setLocked(false);
			
			// Setup opensocial token if needed
			widgetInstance.setOpensocialToken("");

			if (properties.getBoolean("opensocial.enable")){
				if (properties.getString("opensocial.token").equals("secure")){
					widgetInstance.setOpensocialToken(OpenSocialUtils.createEncryptedToken(widgetInstance,properties.getString("opensocial.key")));
				} else {
					widgetInstance.setOpensocialToken(OpenSocialUtils.createPlainToken(widgetInstance));					
				}
			}
			
			// Save
			dbManager.saveObject(widgetInstance);

			// add in the sharedDataKey as a preference so that a widget can know
			// what sharedData event to listen to later
			Preference pref = new Preference();
			pref.setWidgetInstance(widgetInstance);
			pref.setDkey("sharedDataKey");				
			pref.setDvalue(sharedDataKey);
			dbManager.saveObject(pref);	
		} 
		catch (Exception e) {
			_logger.error(e.getMessage());
		}		
		return widgetInstance;
	}
	
}

