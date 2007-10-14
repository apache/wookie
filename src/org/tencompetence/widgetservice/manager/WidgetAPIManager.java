package org.tencompetence.widgetservice.manager;

import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.DBManagerInterface;

public class WidgetAPIManager {
	
	boolean showProcess = false;
	
	static Logger _logger = Logger.getLogger(WidgetAPIManager.class.getName());
	
	// all instances should have the same info regarding locked shared data
	// all threads execute the same instance - so doesnt need to be static???? - check
	private static Hashtable<String, String> _lockedTable = new Hashtable<String, String>();
	
	/**
	 * if this is locked then return who locked it
	 * @return
	 */
	public boolean isLockedByMe(WidgetInstance instance){
		// NOTE: DB rules say these values below cannot be null
		String sharedDataKey = instance.getRunId() + "-" + instance.getEnvId() + "-" + instance.getServiceId();		
		synchronized(this){
			if(_lockedTable.containsKey(sharedDataKey)){
				// the shareddata is locked - but by who?			
				if(_lockedTable.get(sharedDataKey).equals(instance.getIdKey())){
					return true;
				}
			}	
		}
		return false;
	}
	

	public boolean handleLock(WidgetInstance instance){				
		// NOTE: DB rules say these values below cannot be null
		String sharedDataKey = instance.getRunId() + "-" + instance.getEnvId() + "-" + instance.getServiceId();		
		synchronized(this){
			if(_lockedTable.containsKey(sharedDataKey)){
				// already locked so return false - unsuccessful call
				_logger.debug("lock refused (already locked) for :"+sharedDataKey + instance.getIdKey());
				return false;
			}
			else {
				// lock the shared data by "sharedDataKey" and store which instance has the lock
				_lockedTable.put(sharedDataKey, instance.getIdKey());	
				_logger.debug("locked table:"+sharedDataKey + instance.getIdKey());
				return true;
		    }
		}
	}
	
	public boolean handleUnLock(WidgetInstance instance){	
		// NOTE: DB rules say these values below cannot be null
		String sharedDataKey = instance.getRunId() + "-" + instance.getEnvId() + "-" + instance.getServiceId();
		synchronized(this){
			if(_lockedTable.containsKey(sharedDataKey)){
				// the shareddata is locked - but by who?			
				if(_lockedTable.get(sharedDataKey).equals(instance.getIdKey())){
					// this instance locked the data
					_lockedTable.remove(sharedDataKey);
					_logger.debug("unlock table for :"+sharedDataKey + instance.getIdKey());
					return true;
				}
				else{
					// some other instance locked this data so cant unlock
					_logger.debug("unlock refused (already locked by someone else) for :"+sharedDataKey + instance.getIdKey());
					return false;
				}
			}
			else{
				// doesnt contain it so it not locked
				_logger.debug("unlock refused (not locked) for :"+sharedDataKey + instance.getIdKey());
				return false;
			}
		}
	}
	
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
	 * @param instance
	 * @return
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
						//_logger.debug("found record will delete: " + sharedData.getDkey() + "  :: " + name + " TRUE");  
						dbManager.deleteObject(sharedData);
					}
					else{    
						//_logger.debug("found record will update : " + sharedData.getDkey() + " :was: " + sharedData.getDvalue() + " :is now: " + value);
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
				//_logger.debug("Could not find record, so add new one: "+ name + " value " + value); 
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
	 * @return
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
	
		
	public void addNewPreference(WidgetInstance widgetInstance, String name, String value) throws Exception{
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();	
		Preference pref = new Preference();
		pref.setWidgetInstance(widgetInstance);
		pref.setDkey(name);
		pref.setDvalue(value);	
		dbManager.saveObject(pref);				
	}

}
