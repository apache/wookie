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
import java.util.Set;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetDefault;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.beans.WidgetType;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.DBManagerInterface;

/**
 * WidgetAdminManager
 * 
 * This class is responsible for administrative functions such as adding new widget types
 * and setting which widget is to be the default
 * 
 * @author Paul Sharples
 * @version $Id: WidgetAdminManager.java,v 1.2 2007-10-17 23:11:12 ps3com Exp $
 */
public class WidgetAdminManager extends WidgetServiceManager {
	
	static Logger _logger = Logger.getLogger(WidgetAdminManager.class.getName());
	
	/**
	 * Add a new widget to the system - without the widget type
	 * @param widgetName - the name of this widget
	 * @param url - the url which it resides
	 * @param height - the height at which it is supposed to be displayed
	 * @param width - the width at which it is supposed to be displayed
	 */
	public void addNewWidget(String widgetName, String url, int height, int width) {
		addNewWidget(widgetName, url, height, width, null);
	}
	
	
	/**
	 * Add a new widget to the system
	 * @param widgetName - the name of this widget
	 * @param url - the url which it resides
	 * @param height - the height at which it is supposed to be displayed
	 * @param width - the width at which it is supposed to be displayed
	 * @param widgetTypes - a string array containing the types this widget can perform as
	 * @return - the new key created for this widget
	 */
	@SuppressWarnings("unchecked")
	public int addNewWidget(String widgetName, String url, int height, int width, String[] widgetTypes) {
			int newWidgetIdx = -1;
			final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
	        Widget widget;
			try {
				widget = new Widget();
				widget.setWidgetName(widgetName);
				widget.setUrl(url);
				widget.setHeight(height);
				widget.setWidth(width);
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
			} 
			catch (Exception e) {
				_logger.error(e.getMessage());
			}
	        return newWidgetIdx;	       
	    }

	/**
	 * Get all widgets from widget table
	 * @return - an array of widgets
	 */
	public Widget[] getAllWidgets() {
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
		List<?> sqlReturnList = dbManager.createQuery("from Widget").list();
		Widget[] widgets = sqlReturnList.toArray(new Widget[sqlReturnList.size()]);
		return widgets;
	}
	
	/**
	 * Get all of the default widgets from the DefaultWidgets table
	 * @return - an array of default widgets
	 */
	public WidgetDefault[] getAllDefaultWidgets() {
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
		List<?> sqlReturnList = dbManager.createQuery("from WidgetDefault").list();		
		WidgetDefault[] widgetDefs = sqlReturnList.toArray(new WidgetDefault[sqlReturnList.size()]);
		return widgetDefs;
	}
	
	/**
	 * Get all widgets of a particular type - e.g. - chat
	 * @param typeToSearch - a widget type
	 * @return - returns an array of widgets
	 */
	public Widget[] getWidgetsByType(String typeToSearch) throws WidgetTypeNotSupportedException {
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();			
		String sqlQuery = "SELECT widget.id, widget.widget_name, widget.url, widget.height, widget.width, widgettype.widget_context "
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

	/**
	 * Sets a given widget to be the default for a given context - i.e. chat or discussion etc...
	 * @param key - the key of the widget to set as defult
	 * @param widgetType - the type of widget
	 */
	public void setDefaultWidget(int key, String widgetType){
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();	
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
	
	
	
	/**
	 * Prints the details of all widgets
	 * @param magr - a widgetAdminManager instance
	 */	
	@SuppressWarnings("unchecked")
	public void printOutAllWidgets(WidgetAdminManager magr){				
		Widget[] widgets = magr.getAllWidgets();
		    for (int i = 0; i < widgets.length; i++) {
		        Widget theWidget = (Widget) widgets[i];		        
		        _logger.debug(
		        				   "\n\t Name: " + theWidget.getWidgetName() +
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
	
	/**
	 * Testing main method
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		WidgetAdminManager manager = new WidgetAdminManager();
		manager.setUpDefaultData(manager, "http://www.default_services.org.uk");
		//manager.printOutAllWidgets(manager);	
		
		Widget[] widgets;
		try {
			widgets = manager.getWidgetsByType("chatw");		
			for (int i = 0; i < widgets.length; i++) {
				_logger.debug("\n\t Name: " + widgets[i].getWidgetName());			
				Set<WidgetType> types= widgets[i].getWidgetTypes();
				WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		
				for(int j=0;j<widgetTypes.length;j++){	
					_logger.debug("\n\t Type: " + widgetTypes[j].getWidgetContext());
				}
			}
		} 
		catch (WidgetTypeNotSupportedException e) {
			_logger.error(e.getMessage());
		}
	}
	
	/**
	 * Setup some default data
	 * @param mgr
	 * @param defaultPath
	 */
	public void setUpDefaultData(WidgetAdminManager mgr, String defaultPath){
		if (defaultPath.equals(null)){
			defaultPath = "http://localhost:8080/wookie/services/default/";
		} 
		
		// 	add 4 test widgets
		
		setDefaultWidget(mgr.addNewWidget("The default chat widget", 
		 		 defaultPath + "/chat.html", 
				 475, 375,
				 new String[]{"chat"})
		, "chat");
		
		
		mgr.addNewWidget("A discussion tool - better for inexperienced users", 
						"http://www.something.com/discuss1",
						350, 440,
						 new String[]{"chat","discussion"});
		
		setDefaultWidget(mgr.addNewWidget("A discussion tool - better for experienced users", 
						"http://www.something.com/discuss2",
						350, 440,
						new String[]{"discussion","wiki","chat"})
		,"discussion");
		
		mgr.addNewWidget("acmeForum forum software for all", 
						"http://www.acme-forum.com/discuss",
						350, 440,
						new String[]{"forum"});
		
		mgr.addNewWidget("wikiplus+", 
						"http://www.something.com/wiki",
						350, 440);
		
		try {
			WidgetInstance wi = addNewWidgetInstance("paul", "0", "env001", "ser001", getDefaultWidgetByType("chat"), "nonce", "idkeythingy");
			addNewPreference(wi,"surname","smith");
			addNewPreference(wi,"firstname","paul");
			addNewPreference(wi,"age","35");
			addNewPreference(wi,"gender","male");
			
			wi = addNewWidgetInstance("fred", "0", "env001", "ser001", getDefaultWidgetByType("chat"), "nonce2", "idkeythingy2");
			addNewPreference(wi,"surname","smith");
			addNewPreference(wi,"firstname","fred");
			addNewPreference(wi,"age","23");
			addNewPreference(wi,"gender","male");
		} 
		catch (WidgetTypeNotSupportedException e) {
			_logger.error(e.getMessage());
		}
		catch (Exception e) {
			_logger.error(e.getMessage());
		}
					
	}
}
