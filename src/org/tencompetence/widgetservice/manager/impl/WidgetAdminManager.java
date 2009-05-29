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

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.beans.PreferenceDefault;
import org.tencompetence.widgetservice.beans.Whitelist;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetDefault;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.beans.WidgetService;
import org.tencompetence.widgetservice.beans.WidgetType;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;
import org.tencompetence.widgetservice.manager.IWidgetAdminManager;
import org.tencompetence.widgetservice.util.ManifestHelper;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.IDBManager;

/**
 * WidgetAdminManager
 * 
 * This class is responsible for administrative functions such as adding new widget types
 * and setting which widget is to be the default
 * 
 * @author Paul Sharples
 * @version $Id: WidgetAdminManager.java,v 1.11 2009-05-29 10:20:38 scottwilson Exp $
 */
public class WidgetAdminManager extends WidgetServiceManager implements IWidgetAdminManager {
	
	static Logger _logger = Logger.getLogger(WidgetAdminManager.class.getName());

	public WidgetAdminManager(Messages localizedMessages) {
		super(localizedMessages);
	}
				
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#addNewService(java.lang.String)
	 */
	public boolean addNewService(String serviceName) {
		boolean success = false;
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		WidgetService service;
		try {
			service = new WidgetService();
			service.setServiceName(serviceName);
			dbManager.saveObject(service);
			success = true;
		} 
		catch (Exception ex) {
			_logger.error(ex.getMessage());					
		}
		return success;
	}
	
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#addNewWidget(java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public void addNewWidget( String widgetIconLocation, String url, Hashtable<String, String> widgetData, List<PreferenceDefault> prefs ) {
		addNewWidget(widgetIconLocation, url, widgetData, prefs, null);
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#addNewWidget(java.lang.String, java.lang.String, java.lang.String, int, int, java.lang.String[])
	 */
	@SuppressWarnings("unchecked")
	public int addNewWidget(String widgetIconLocation, String url, Hashtable<String,String> widgetData, List<PreferenceDefault> prefs, String[] widgetTypes) {
			int newWidgetIdx = -1;
			final IDBManager dbManager = DBManagerFactory.getDBManager();
	        Widget widget;
			try {
				widget = new Widget();
				widget.setWidgetTitle(widgetData.get(ManifestHelper.NAME_ELEMENT));
				widget.setWidgetDescription(widgetData.get(ManifestHelper.DESCRIPTION_ELEMENT));
				widget.setWidgetAuthor(widgetData.get(ManifestHelper.AUTHOR_ELEMENT));
				widget.setWidgetIconLocation(widgetIconLocation);
				widget.setUrl(url);
				widget.setGuid(widgetData.get(ManifestHelper.ID_ATTRIBUTE));
				widget.setHeight(Integer.parseInt(widgetData.get(ManifestHelper.HEIGHT_ATTRIBUTE)));
				widget.setWidth(Integer.parseInt(widgetData.get(ManifestHelper.WIDTH_ATTRIBUTE)));
				widget.setVersion(widgetData.get(ManifestHelper.VERSION_ATTRIBUTE));
				dbManager.saveObject(widget);	       	        
				WidgetType widgetType;
				if (widgetTypes!=null){
					for(int i=0;i<widgetTypes.length;i++){
						widgetType = new WidgetType();
						widgetType.setWidgetContext(widgetTypes[i]);
						widgetType.setWidget(widget);
						widget.getWidgetTypes().add(widgetType);
						dbManager.saveObject(widgetType);
					}
				}
				newWidgetIdx = widget.getId();
				// Save default preferences
				if (prefs != null){
					for (PreferenceDefault pref:(PreferenceDefault[])prefs.toArray(new PreferenceDefault[prefs.size()])){
						pref.setWidget(widget);
						dbManager.saveObject(pref);
					}
				}
			} 
			catch (Exception e) {
				_logger.error(e.getMessage());
			}
	        return newWidgetIdx;	       
	    }
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#addWhiteListEntry(java.lang.String)
	 */
	public boolean addWhiteListEntry(String uri) {
		boolean success = false;
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		Whitelist list;
		try {
			list = new Whitelist();
			list.setfUrl(uri);
			dbManager.saveObject(list);
			success = true;
		} 
		catch (Exception ex) {
			_logger.error(ex.getMessage());
		}
		return success;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#deleteWidgetDefaultById(int)
	 */
	public void deleteWidgetDefaultById(int widgetKey){		
	 final IDBManager dbManager = DBManagerFactory.getDBManager();			
		String sqlQuery = "DELETE from WidgetDefault where widgetId =" + widgetKey;
		dbManager.createSQLQuery(sqlQuery).executeUpdate();
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#deleteWidgetDefaultByIdAndServiceType(int, java.lang.String)
	 */
	public void deleteWidgetDefaultByIdAndServiceType(int widgetKey, String serviceType){		
		 final IDBManager dbManager = DBManagerFactory.getDBManager();			
			String sqlQuery = "DELETE from WidgetDefault where widgetId =" + widgetKey
			+ " AND widgetContext = '" + serviceType + "'";
			dbManager.createSQLQuery(sqlQuery).executeUpdate();
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#deleteWidgetDefaultByServiceName(java.lang.String)
	 */
	public void deleteWidgetDefaultByServiceName(String serviceName){		
	 final IDBManager dbManager = DBManagerFactory.getDBManager();			
		String sqlQuery = "DELETE from WidgetDefault where widgetContext ='" + serviceName + "'";
		dbManager.createSQLQuery(sqlQuery).executeUpdate();
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#doesServiceExistForWidget(int, java.lang.String)
	 */
	public boolean doesServiceExistForWidget(int dbkey, String serviceType){
		IDBManager dbManager = null;		
		dbManager = DBManagerFactory.getDBManager();
		String sqlQuery =   "select " +
		"count(*) "
		+ "from WidgetType widgettype "
		+ "WHERE "
		+ "widgettype.widget ='" + dbkey + "' AND "
		+ "widgettype.widgetContext ='" + serviceType + "'";
		long count=0l; 				
		count = (Long) dbManager.createQuery(sqlQuery).uniqueResult();		
		return (count == 1 ? true : false); 					
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#doesWidgetAlreadyExistInSystem(java.lang.String)
	 */
	public boolean doesWidgetAlreadyExistInSystem(String guid){
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		String sqlQuery =   "select " +
							"count(*) "
							+ "from Widget widget "
							+ "WHERE "
							+ "widget.guid ='" + guid + "'";
		_logger.debug((sqlQuery));
		long count=0l; 				
		count = (Long) dbManager.createQuery(sqlQuery).uniqueResult();
		return (count == 1 ? true : false); 		
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#getAllDefaultWidgets()
	 */
	public WidgetDefault[] getAllDefaultWidgets() {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		List<?> sqlReturnList = dbManager.createQuery("from WidgetDefault").list();		
		WidgetDefault[] widgetDefs = sqlReturnList.toArray(new WidgetDefault[sqlReturnList.size()]);
		return widgetDefs;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#getAllServices()
	 */
	public WidgetService[] getAllServices() {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		List<?> sqlReturnList = dbManager.createQuery("from WidgetService").list();		
		WidgetService[] widgetDefs = sqlReturnList.toArray(new WidgetService[sqlReturnList.size()]);
		return widgetDefs;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#getAllWidgets()
	 */
	public Widget[] getAllWidgets() {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		List<?> sqlReturnList = dbManager.createQuery("from Widget").list();
		Widget[] widgets = sqlReturnList.toArray(new Widget[sqlReturnList.size()]);
		return widgets;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#getWhiteList()
	 */
	public Whitelist[] getWhiteList() {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		List<?> sqlReturnList = dbManager.createQuery("from Whitelist").list();		
		Whitelist[] whiteListEntries = sqlReturnList.toArray(new Whitelist[sqlReturnList.size()]);
		return whiteListEntries;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#getWidget(int)
	 */
	public Widget getWidget(int dbKey) {

		final IDBManager dbManager = DBManagerFactory.getDBManager();
		final Criteria crit = dbManager.createCriteria(Widget.class);

		try {
			crit.add(Restrictions.eq("id", dbKey));
			List<Widget> sqlReturnList;
			sqlReturnList = dbManager.getObjects(Widget.class, crit);

			if (sqlReturnList.size() != 1) {
				return null;
			} 
			else {
				return (Widget) sqlReturnList.get(0);
			}
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
			return null;
		}
	}
	

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#getWidgetsByType(java.lang.String)
	 */
	public Widget[] getWidgetsByType(String typeToSearch) throws WidgetTypeNotSupportedException {
		final IDBManager dbManager = DBManagerFactory.getDBManager();			
		String sqlQuery = "SELECT widget.id, widget.widget_title, widget_description, widget_author, widget_icon_location, widget.url, widget.maximize, widget.guid, " +
								"widget.height, widget.width, widgettype.widget_context "
						+ "FROM Widget widget, WidgetType widgettype "
						+ "WHERE widget.id = widgettype.widget_id "
						+ "AND widgettype.widget_context='" + typeToSearch + "'";		
		
		List<?> sqlReturnList = dbManager.createSQLQuery(sqlQuery).addEntity(Widget.class).list();
		Widget[] widgets = sqlReturnList.toArray(new Widget[sqlReturnList.size()]);
		if(widgets.length==0){
			throw new WidgetTypeNotSupportedException("Widget type " + typeToSearch + " is not supported");
		}
		return widgets;		 
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#getWidgetService(int)
	 */
	public WidgetService getWidgetService(int dbKey) {

		final IDBManager dbManager = DBManagerFactory.getDBManager();
		final Criteria crit = dbManager.createCriteria(WidgetService.class);

		try {
			crit.add(Restrictions.eq("id", dbKey));
			List<WidgetService> sqlReturnList;
			sqlReturnList = dbManager.getObjects(WidgetService.class, crit);

			if (sqlReturnList.size() != 1) {
				return null;
			} 
			else {
				return (WidgetService) sqlReturnList.get(0);
			}
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
			return null;
		}

	}
	
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#isWidgetMaximized(int)
	 */
	public boolean isWidgetMaximized(int dbKey){
		boolean response = false;
		final IDBManager dbManager = DBManagerFactory.getDBManager();		 
		final Criteria crit = dbManager.createCriteria(Widget.class);
			
		try {
			crit.add(Restrictions.eq("id", dbKey));
			List<Widget> sqlReturnList;
			sqlReturnList = dbManager.getObjects(Widget.class, crit);
			
			if (sqlReturnList.size() != 1) {
				response = false;
			} 
			else {
				Widget widget = (Widget) sqlReturnList.get(0);
				response = widget.isMaximize();				 
			}
			return response;
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
			return response;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#printOutAllWidgets(org.tencompetence.widgetservice.manager.IWidgetAdminManager)
	 */	
	@SuppressWarnings("unchecked")
	public void printOutAllWidgets(IWidgetAdminManager magr){				
		Widget[] widgets = magr.getAllWidgets();
		    for (int i = 0; i < widgets.length; i++) {
		        Widget theWidget = (Widget) widgets[i];		        
		        _logger.debug(
		        				   "\n\t Name: " + theWidget.getWidgetTitle() +
		        				   "\n\t URL: " + theWidget.getUrl() +
		                           "\n\t Height: " + theWidget.getHeight() +		          
		                           "\n\t width: " + theWidget.getWidth() + "\n\t Types:");
		        
		        Set<WidgetType> types = theWidget.getWidgetTypes();
		        WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);
		        for(int j=0;j<widgetTypes.length;j++){
		        	_logger.debug("\n\t "+widgetTypes[j].getWidgetContext());
		        }			     
		    }		    
	}
	
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#removeServiceAndReferences(int)
	 */
	public boolean removeServiceAndReferences(int serviceId){
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		
			WidgetService service = getWidgetService(serviceId);
			String serviceName = service.getServiceName();
			
			// if exists, remove from widget default table
			deleteWidgetDefaultByServiceName(serviceName);
			
			// delete from the widget service table
			try {
				dbManager.deleteObject(service);
			} 
			catch (Exception ex1) {
				dbManager.rollbackTransaction();
				_logger.error(ex1.getMessage());
				ex1.printStackTrace();
				return false;
			}	
			
			try {
				// remove any widgetTypes for each widget that match
				for(Widget widget : getWidgetsByType(serviceName)){
					// remove any widget types for this widget
					Set<?> types = widget.getWidgetTypes();
				    WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
				    for(int j=0;j<widgetTypes.length;++j){	
				    	if(serviceName.equalsIgnoreCase(widgetTypes[j].getWidgetContext())){
				    		try {
								dbManager.deleteObject(widgetTypes[j]);
							} 
				    		catch (Exception ex2) {
				    			dbManager.rollbackTransaction();
								_logger.error(ex2.getMessage());
								ex2.printStackTrace();
								return false;
							}
				    	}
					}
				}
			} 
			catch (WidgetTypeNotSupportedException e) {
				return true;
			}					
			return true;
	}	
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#removeSingleWidgetType(int, java.lang.String)
	 */
	public boolean removeSingleWidgetType(int widgetId, String widgetType) {
		boolean response = false;
		final IDBManager dbManager = DBManagerFactory.getDBManager();	
		Widget widget = getWidget(widgetId);
		// remove any widget types for this widget
		Set<?> types = widget.getWidgetTypes();
        WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
        for(int j=0;j<widgetTypes.length;++j){						
        	try {
        		if(widgetType.equalsIgnoreCase(widgetTypes[j].getWidgetContext())){
        			// BUG FIX
        			// Using only the deleteObject method meant that
        			// the set still contained this widgetType.
        			// So we also remove it from the list
        			types.remove(widgetTypes[j]);
        			dbManager.deleteObject(widgetTypes[j]);
        			response = true;
        		}
			} 
        	catch (Exception e) {
        		dbManager.rollbackTransaction();
    			_logger.error(e.getMessage());
    			return response;
			}
		}
        // if it exists as a service default, then remove it
        deleteWidgetDefaultByIdAndServiceType(widgetId, widgetType);
        return response;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#removeWhiteListEntry(int)
	 */
	public boolean removeWhiteListEntry(int entryId) {
		boolean response = false;
		final IDBManager dbManager = DBManagerFactory.getDBManager();		 
		final Criteria crit = dbManager.createCriteria(Whitelist.class);
			
		try {
			crit.add(Restrictions.eq("id", entryId));
			List<Whitelist> sqlReturnList;
			sqlReturnList = dbManager.getObjects(Whitelist.class, crit);
			
			if (sqlReturnList.size() != 1) {
				response = false;
			} 
			else {
				Whitelist entry = (Whitelist) sqlReturnList.get(0);
				dbManager.deleteObject(entry);
				response = true;
			}			
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
			return response;
		}
		return response;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#removeWidgetAndReferences(int)
	 */
	public boolean removeWidgetAndReferences(int widgetId){
		final IDBManager dbManager = DBManagerFactory.getDBManager();	
		// get the widget
		Widget widget = getWidget(widgetId);
		// remove any defaults for this widget
		deleteWidgetDefaultById(widgetId);
		
		if(widget==null) return false;
		// find any widget instances for this widget
		WidgetInstance[] instances = getwidgetInstancesForWidget(widget);		
		// try to remove prefs, shareddata and then the instances
		try {
			for(WidgetInstance inst : instances){
				deleteSharedDataInstancesForWidgetInstance(inst);
				deletePreferenceInstancesForWidgetInstance(inst);
				dbManager.deleteObject(inst);
			}
			// remove any widget types for this widget
			Set<?> types = widget.getWidgetTypes();
	        WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
	        for(int j=0;j<widgetTypes.length;++j){						
	        	dbManager.deleteObject(widgetTypes[j]);
			}
			// remove the widget itself
			dbManager.deleteObject(widget);
			return true;
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
			return false;
		}
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#setDefaultWidget(int, java.lang.String)
	 */
	public void setDefaultWidget(int key, String widgetType){
		final IDBManager dbManager = DBManagerFactory.getDBManager();	
        boolean found=false;
        try {
			// does it already exist in the widgetdefault table?
			WidgetDefault[] currentDefaults = getAllDefaultWidgets();
			for(int i=0;i<currentDefaults.length;i++){
				if(currentDefaults[i].getWidgetContext().equalsIgnoreCase(widgetType)){   
					// found it so update to new widget id
					currentDefaults[i].setWidgetId(key);
					dbManager.saveGenericObject(currentDefaults[i]);
					found=true;
				}
			}
			// didnt find it already set, so add new one
			if(!found){
				WidgetDefault wd = new WidgetDefault();
				wd.setWidgetContext(widgetType);
				wd.setWidgetId(key);		
				dbManager.saveGenericObject(wd);
			}
		} 
        catch (Exception e) {
			_logger.error(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#setWidgetTypesForWidget(int, java.lang.String[], boolean)
	 */
	@SuppressWarnings("unchecked")
	public void setWidgetTypesForWidget(int dbKey, String[] widgetTypes, boolean maximize){
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(Widget.class);
			crit.add(Restrictions.eq("id", dbKey));
			final List<Widget> sqlReturnList = dbManager.getObjects(Widget.class, crit);
			
			Widget widget = (Widget) sqlReturnList.get(0);
			if(maximize){
				widget.setMaximize(maximize);
				dbManager.saveObject(widget);	
			}

			WidgetType widgetType;
			if (widgetTypes!=null){
				for(int i=0;i<widgetTypes.length;i++){	
					if(!doesServiceExistForWidget(widget.getId(), widgetTypes[i])){
						widgetType = new WidgetType();
						widgetType.setWidgetContext(widgetTypes[i]);
						widgetType.setWidget(widget);
						widget.getWidgetTypes().add(widgetType);
						dbManager.saveObject(widgetType);								
					}
				}
			}
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());			
		}			
	}


	public String getWidgetGuid(int dbKey) {
		Widget widget = getWidget(dbKey);
		return widget.getGuid();
	}

}
