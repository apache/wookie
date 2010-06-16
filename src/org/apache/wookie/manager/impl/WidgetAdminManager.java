/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.wookie.manager.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;
import org.apache.wookie.beans.IWidgetType;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.manager.IWidgetAdminManager;

/**
 * WidgetAdminManager
 * 
 * This class is responsible for administrative functions such as adding new widget types
 * and setting which widget is to be the default
 * 
 * @author Paul Sharples
 * @version $Id: WidgetAdminManager.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
 */
public class WidgetAdminManager implements IWidgetAdminManager {
	
	static Logger _logger = Logger.getLogger(WidgetAdminManager.class.getName());
	protected Messages localizedMessages;

	public WidgetAdminManager(Messages localizedMessages) {
		this.localizedMessages = localizedMessages;	
	}
	
	private void deleteWidgetDefaultByIdAndServiceType(Object widgetKey, String serviceType){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidget widget = persistenceManager.findById(IWidget.class, widgetKey);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("widget", widget);
		map.put("widgetContext", serviceType);
        IWidgetDefault[] widgetDefaults = persistenceManager.findByValues(IWidgetDefault.class, map);
		persistenceManager.delete(widgetDefaults);
	}
	
	private boolean doesServiceExistForWidget(Object dbkey, String serviceType){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidget widget = persistenceManager.findById(IWidget.class, dbkey);
        Iterator<IWidgetType> typesIter = widget.getWidgetTypes().iterator();
        while (typesIter.hasNext())
        {
            IWidgetType type = typesIter.next();
            if (type.getWidgetContext().equalsIgnoreCase(serviceType))
            {
                return true;
            }
        }
		return false;					
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manager.IWidgetAdminManager#removeSingleWidgetType(java.lang.String, java.lang.String)
	 */
	public boolean removeSingleWidgetType(String widgetId, String widgetType) {
		boolean response = false;	
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidget widget = persistenceManager.findById(IWidget.class, widgetId);
		// remove any widget types for this widget
        Iterator<IWidgetType> typesIter = widget.getWidgetTypes().iterator();
        while (typesIter.hasNext())
        {
            IWidgetType type = typesIter.next();
            if (type.getWidgetContext().equalsIgnoreCase(widgetType))
            {
                typesIter.remove();
                response = true;
            }
        }
        if (response)
        {
            persistenceManager.save(widget);
        }
        // if it exists as a service default, then remove it
        deleteWidgetDefaultByIdAndServiceType(widgetId, widgetType);
        return response;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manager.IWidgetAdminManager#setDefaultWidget(java.lang.String, java.lang.String)
	 */
	public void setDefaultWidget(String key, String widgetType){
        boolean found=false;
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidget widget = persistenceManager.findById(IWidget.class, key);
		// does it already exist in the widgetdefault table?
        IWidgetDefault [] currentDefaults = persistenceManager.findAll(IWidgetDefault.class);
		for(int i=0;i<currentDefaults.length;i++){
			if(currentDefaults[i].getWidgetContext().equalsIgnoreCase(widgetType)){   
				// found it so update to new widget id
				currentDefaults[i].setWidget(widget);
				persistenceManager.save(currentDefaults[i]);
				found=true;
			}
		}
		// didnt find it already set, so add new one
		if(!found){
			IWidgetDefault wd = persistenceManager.newInstance(IWidgetDefault.class);
			wd.setWidgetContext(widgetType);
			wd.setWidget(widget);	
			persistenceManager.save(wd);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manager.IWidgetAdminManager#setWidgetTypesForWidget(java.lang.String, java.lang.String[], boolean)
	 */
	public void setWidgetTypesForWidget(String dbKey, String[] widgetTypes, boolean maximize){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidget widget = persistenceManager.findById(IWidget.class, dbKey);

        boolean widgetTypesSet = false;
		if (widgetTypes!=null){
			for(int i=0;i<widgetTypes.length;i++){	
				if(!doesServiceExistForWidget(widget.getId(), widgetTypes[i])){
				    IWidgetType widgetType = persistenceManager.newInstance(IWidgetType.class);
					widgetType.setWidgetContext(widgetTypes[i]);
					widget.getWidgetTypes().add(widgetType);
					widgetTypesSet = true;
				}
			}
		}
		if (widgetTypesSet)
		{
		    persistenceManager.save(widget);
		}
	}

}
