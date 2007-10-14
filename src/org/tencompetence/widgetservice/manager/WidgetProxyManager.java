package org.tencompetence.widgetservice.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.Whitelist;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.DBManagerInterface;

public class WidgetProxyManager {
	
	static Logger _logger = Logger.getLogger(WidgetProxyManager.class.getName());
	
	public synchronized Whitelist[] getWhiteList(){
		DBManagerInterface dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			List<?> sqlReturnList = dbManager.createQuery("from Whitelist").list();		
			Whitelist[] whitelist = sqlReturnList.toArray(new Whitelist[sqlReturnList.size()]);
			return whitelist;
		} 
		catch (Exception ex) {
			dbManager.rollbackTransaction();
			_logger.error(ex.getMessage());
			return null;
		}	
	}
	
	public boolean isAllowed(String aUrl){					
		for (Whitelist whiteList : getWhiteList()){
			// TODO - make this better then just comparing the beginning...
			if(aUrl.toLowerCase().startsWith(whiteList.getfUrl().toLowerCase()))			
				return true;
		}
		return false;		
	}
}