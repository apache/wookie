package org.tencompetence.widgetservice.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.DBManagerInterface;

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return widgetInstance;
	}
	
	public static void main(String[] args) {
		WidgetServiceManager manager = new WidgetServiceManager();
		System.out.println("found:"+manager.widgetInstanceExists("paul", "0", "env001", "ser001", "chat"));
	}
	
	
	
}