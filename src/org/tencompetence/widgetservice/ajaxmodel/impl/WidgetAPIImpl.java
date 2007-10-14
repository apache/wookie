package org.tencompetence.widgetservice.ajaxmodel.impl;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.manager.WidgetAPIManager;

/**
 * 
 * @author Paul Sharples
 *
 */
public class WidgetAPIImpl implements IWidgetAPI {
	
	public static final String UNAUTHORISED_MESSAGE = "Unauthorised";
	public static final String NOKEY_MESSAGE = "No matching key found";
	
	static Logger _logger = Logger.getLogger(WidgetAPIImpl.class.getName());
			
	private WidgetAPIManager manager = new WidgetAPIManager();			
	private WidgetInstance widgetInstance = null;
	
	public WidgetAPIImpl(){}

	public String preferenceForKey(String id_key, String key) {	
		if(id_key==null) return UNAUTHORISED_MESSAGE;
		if(key==null)return NOKEY_MESSAGE;		
		String preferenceText = null;		
		try {
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				boolean found = false;
				
				for(Preference preference : manager.getPreferenceForInstance(widgetInstance)){
					if(preference.getDkey().equals(key)){
						preferenceText = preference.getDvalue();
						found = true;
						break;					
					}
				}
				if(!found){
					_logger.debug("No key found in getpreference call:" + key);
					preferenceText = "null";
				}
			}
			else{
				preferenceText = UNAUTHORISED_MESSAGE;
			}	
		} 
		catch (Exception ex) {			
			_logger.error("Error getting preferences", ex);
		}
		return preferenceText;	
	}

	public String sharedDataForKey(String id_key, String key) {
		if(id_key==null) return UNAUTHORISED_MESSAGE;
		if(key==null) return NOKEY_MESSAGE;
		String sharedDataValue = null;				
		try {
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				boolean found = false;
								
				for(SharedData sharedData : manager.getSharedDataForInstance(widgetInstance)){
					if(sharedData.getDkey().equals(key)){
						sharedDataValue = sharedData.getDvalue();
						found = true;
						break;
					}
				}				
				if(!found){
					_logger.debug("No key found in getshareddata call:" + key);
					sharedDataValue = null;
				}
			}
			else{
				sharedDataValue = UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {
			_logger.error("Error getting shared data", ex);
		}
		return sharedDataValue;
	}
	
	public String setPreferenceForKey(String id_key, String key, String value) {		
		try {
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				manager.updatePreference(widgetInstance, key, value);
				_logger.debug("setpreferenceForKey set " + key + "  to  " + value);
				
			}
			else{
				return UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {
			_logger.error("setpreferenceForKey failed", ex);			
		}
		return "*TODO*";
	}

	public String setSharedDataForKey(String id_key, String key, String value) {		
		try {
			_logger.error("setSharedDataForKey: "+ id_key+ "\tkey:" + key + "\tvalue:" + value);
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				// TODO locking????
				manager.updateSharedDataEntry(widgetInstance, key, value, false);
				// ********
				WebContext wctx = WebContextFactory.get();
		        String currentPage = wctx.getCurrentPage();

		        ScriptBuffer script = new ScriptBuffer();
		        script.appendScript("widget.onSharedUpdate(")
		            //  .appendData(sharedDataForKey(id_key, key))		        
		              .appendScript(");");

		        // Loop over all the users on the current page
		        Collection pages = wctx.getScriptSessionsByPage(currentPage);
		        for (Iterator it = pages.iterator(); it.hasNext();)
		        {
		            ScriptSession otherSession = (ScriptSession) it.next();
		            otherSession.addScript(script);
		        }
				// *******************
			}
			else{
				return UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {
			_logger.error("setSharedDataForKey failed", ex);
		}
		return "*TODO*";
	}

	public String appendSharedDataForKey(String id_key, String key, String value) {
		try {
			_logger.debug("appendSharedDataForKey: "+ id_key+ "\tkey:" + key + "\tvalue:" + value);
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				manager.updateSharedDataEntry(widgetInstance, key, value, true);
//				 ********
				WebContext wctx = WebContextFactory.get();
		        String currentPage = wctx.getCurrentPage();

		        ScriptBuffer script = new ScriptBuffer();
		        script.appendScript("widget.onSharedUpdate(")
		             //  .appendData(sharedDataForKey(id_key, key))	
		              .appendScript(");");

		        // Loop over all the users on the current page
		        Collection pages = wctx.getScriptSessionsByPage(currentPage);
		        for (Iterator it = pages.iterator(); it.hasNext();)
		        {
		            ScriptSession otherSession = (ScriptSession) it.next();
		            otherSession.addScript(script);
		        }
				// *******************
			}
			else{
				return UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {
			_logger.error("appendSharedDataForKey failed", ex);
		}
		return "*TODO*";
	}

	// TODO this is what the admin or moderator does
	// need to identify who the admin person is from the UOL
	// and somehow get it from the widget..
	public String lock(String id_key) {
		try {
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				return Boolean.toString(manager.handleLock(widgetInstance));
			}
			else{
				return "false";
			}
		} 
		catch (Exception ex) {
			_logger.error("lock failed", ex);
		}
		return "false";
	}
	
	public String unlock(String id_key) {
		try {
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				return Boolean.toString(manager.handleUnLock(widgetInstance));
			}
			else{
				return "false";
			}
		} 
		catch (Exception ex) {
			_logger.error("unlock failed", ex);
		}
		return "false";
	}

	public String contextPropertyForKey(String id_key, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public String setContextPropertyForKey(String id_key, String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public String setUserPropertyForKey(String id_key, String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public String userPropertyForKey(String id_key, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public String openURL(String url) {
		// we could use the whitelist to block here if we wanted...
//		 ********
		_logger.debug("openurl called with        "+ url );
		
		WebContext wctx = WebContextFactory.get();
        //String currentPage = wctx.getCurrentPage();

        ScriptBuffer script = new ScriptBuffer();      
        script.appendScript("window.open(")
        .appendData(url)	
        .appendScript(");");
        
        wctx.getScriptSession().addScript(script);
        return "TODO";
	}

	public String system(String id_key, String arg1) {
		_logger.debug("system called with " + arg1 );
		return "Not available";
	}
	
	public String system(String id_key, String arg1, String arg2) {
		_logger.debug("system called with " + arg1 + "   " + arg2);
		return "Not available";
	}
	
	public String system(String id_key, String arg1, String arg2, String arg3) {
		_logger.debug("system called with " + arg1 + "   " + arg2+ "   " + arg3);
		return "Not available";
	}
}