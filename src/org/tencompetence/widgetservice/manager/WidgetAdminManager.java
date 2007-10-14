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
 * @author Paul Sharples
 * @version $Id: WidgetAdminManager.java,v 1.1 2007-10-14 10:58:25 ps3com Exp $
 */
public class WidgetAdminManager extends WidgetServiceManager {
	
	static Logger _logger = Logger.getLogger(WidgetAdminManager.class.getName());
					
	public void addNewWidget(String widgetName, String url, int height, int width) {
		addNewWidget(widgetName, url, height, width, null);
	}
	
			
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
				// Widget widgetObj = (Widget)session.merge(widget);	       	        
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //session.getTransaction().commit();
	        //HibernateUtil.closeSession();
	        return newWidgetIdx;	       
	    }

	/**
	 * Get all widgets from widget table
	 * @return
	 */
	public Widget[] getAllWidgets() {
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
		List<?> sqlReturnList = dbManager.createQuery("from Widget").list();
		Widget[] widgets = sqlReturnList.toArray(new Widget[sqlReturnList.size()]);
		return widgets;
	}
	
	/**
	 * Get all of the default widgets from the DefaultWidgets table
	 * @return
	 */
	public WidgetDefault[] getAllDefaultWidgets() {
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
		List<?> sqlReturnList = dbManager.createQuery("from WidgetDefault").list();		
		WidgetDefault[] widgetDefs = sqlReturnList.toArray(new WidgetDefault[sqlReturnList.size()]);
		return widgetDefs;
	}
	
	/**
	 * Get all widgets of a particular type - eg - chat
	 * @param typeToSearch
	 * @return
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
	 * @param key
	 * @param widgetType
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
			//session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		Widget w = manager.getDefaultWidgetByType("testing");
		if(w==null){
			_logger.debug("tis null");
		}
		else{
			_logger.debug(w.getWidgetName());
		}
		
		*/
		
		//HibernateUtil.getSessionFactory().close();
		//HibernateUtil.closeSession();
	}
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
	}
}
