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
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.DBManagerInterface;

/**
 * A class to manage widget instances
 * @author Paul Sharples
 * @version $Id
 *
 */
public class WidgetServiceManager extends WidgetAPIManager {

	static Logger _logger = Logger.getLogger(WidgetServiceManager.class.getName());
		
	/**
	 * Get the default widget for the type specified by by the parameter
	 * @param typeToSearch
	 * @return a Widget or NULL meaning it has not been set
	 */
	public Widget getDefaultWidgetByType(String typeToSearch) throws WidgetTypeNotSupportedException {
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
		String sqlQuery = "SELECT widget.id, widget.widget_name, widget.url, widget.height, widget.width, widget.maximize "
						+ "FROM Widget widget, WidgetDefault widgetdefault "
						+ "WHERE widget.id = widgetdefault.widgetId "
						+ "AND widgetdefault.widgetContext='" + typeToSearch + "'";		
		
		Widget widget = (Widget)dbManager.createSQLQuery(sqlQuery).addEntity(Widget.class).uniqueResult();	
		if(widget==null){
			throw new WidgetTypeNotSupportedException("Widget type " + typeToSearch + " is not supported");
		}
		return widget;		 
	}
	
	/**
	 * Check if a widgetinstance exists in the DB
	 * @param userId - userId to check
	 * @param runId - runId to check
	 * @param envId - environmentId to check
	 * @param serviceId - serviceId to check
	 * @param serviceContext - the widget context
	 * @return
	 */
	public boolean widgetInstanceExists(String userId, String runId, String envId, String serviceId, String serviceContext){
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
		//got to exist in widgetinstance and also be registered as this type of context in widgetcontext		
		String sqlQuery =   "select " +
							"count(*) "
							+ "from WidgetInstance widgetinstance, WidgetType widgettype "
							+ "WHERE "
							+ "widgetinstance.userId ='" + userId + "' "
							+ "AND widgetinstance.runId ='" + runId + "' "
							+ "AND widgetinstance.envId ='" + envId + "' "
							+ "AND widgetinstance.serviceId ='" + serviceId + "' "							
							+ "AND widgettype.widgetContext ='" + serviceContext + "' "			
							+ "AND widgetinstance.widget = widgettype.widget"
							;							
		_logger.debug((sqlQuery));
		long count=0l; 				
		count = (Long) dbManager.createQuery(sqlQuery).uniqueResult();
		return (count == 1 ? true : false); 
				
	}
	
	/**
	 * Get a widgetinstance from the DB
	 * @param userId - userId to check
	 * @param runId - runId to check
	 * @param envId - environmentId to check
	 * @param serviceId - serviceId to check
	 * @param serviceContext - the widget context
	 * @return
	 */
	public WidgetInstance getwidgetInstance(String userId, String runId, String envId, String serviceId, String serviceContext){
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
		//got to exist in widgetinstance and also be registered as this type of context in widgetcontext		
		String sqlQuery =   "select widgetinstance " 							
							+ "from WidgetInstance widgetinstance, WidgetType widgettype "
							+ "WHERE "
							+ "widgetinstance.userId ='" + userId + "' "
							+ "AND widgetinstance.runId ='" + runId + "' "
							+ "AND widgetinstance.envId ='" + envId + "' "
							+ "AND widgetinstance.serviceId ='" + serviceId + "' "							
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
	

	/**
	 * Method to add a new instance to the widget instances table
	 * @param type
	 * @throws WidgetTypeNotSupportedException 
	 */
	public WidgetInstance addNewWidgetInstance(String userId, String runId, String envId,
			String serId, Widget widget, String nonce, String idKey) {		
			final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
			WidgetInstance widgetInstance = new WidgetInstance();
			try {
				widgetInstance.setUserId(userId);
				widgetInstance.setRunId(runId);			
				widgetInstance.setEnvId(envId);
				widgetInstance.setServiceId(serId);
				widgetInstance.setIdKey(idKey);
				widgetInstance.setNonce(nonce);
				// set the defaults widget for this type			
				widgetInstance.setWidget(widget);						
				widgetInstance.setHidden(false);
				widgetInstance.setShown(true);
				widgetInstance.setUpdated(false);
				dbManager.saveObject(widgetInstance);	
				// we'll put the username of the LD user as a pref for later if needed
				Preference pref = new Preference();
				pref.setWidgetInstance(widgetInstance);
				pref.setDkey("LDUsername");
				pref.setDvalue(userId);
				dbManager.saveObject(pref);	
			} 
			catch (Exception e) {
				_logger.error(e.getMessage());
			}		
			return widgetInstance;
	}
	
	public static void main(String[] args) {
		WidgetServiceManager manager = new WidgetServiceManager();
		System.out.println("found:"+manager.widgetInstanceExists("paul", "0", "env001", "ser001", "chat"));
	}
	
	
	
}