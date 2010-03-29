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
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetDefault;
import org.apache.wookie.beans.WidgetType;
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
	
	private void deleteWidgetDefaultByIdAndServiceType(int widgetKey, String serviceType){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("widgetId", widgetKey);
		map.put("widgetContext", serviceType);
		WidgetDefault[] widgetDefaults;
		widgetDefaults = WidgetDefault.findByValues(map);
		WidgetDefault.delete(widgetDefaults);
	}
	
	private boolean doesServiceExistForWidget(int dbkey, String serviceType){
		Widget widget = Widget.findById(Integer.valueOf(dbkey));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("widget", widget);
		map.put("widgetContext", serviceType);
		WidgetType[] types = WidgetType.findByValues(map);
		if (types == null || types.length !=1) return false;
		return true;					
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manager.IWidgetAdminManager#removeSingleWidgetType(int, java.lang.String)
	 */
	public boolean removeSingleWidgetType(int widgetId, String widgetType) {
		boolean response = false;	
		Widget widget = Widget.findById(Integer.valueOf(widgetId));
		// remove any widget types for this widget
		Set<?> types = widget.getWidgetTypes();
        WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
        for(int j=0;j<widgetTypes.length;++j){						
    		if(widgetType.equalsIgnoreCase(widgetTypes[j].getWidgetContext())){
    			// BUG FIX
    			// Using only the deleteObject method meant that
    			// the set still contained this widgetType.
    			// So we also remove it from the list
    			types.remove(widgetTypes[j]);
    			widgetTypes[j].delete();
    			response = true;
    		}
		}
        // if it exists as a service default, then remove it
        deleteWidgetDefaultByIdAndServiceType(widgetId, widgetType);
        return response;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manager.IWidgetAdminManager#setDefaultWidget(int, java.lang.String)
	 */
	public void setDefaultWidget(int key, String widgetType){
        boolean found=false;
		// does it already exist in the widgetdefault table?
		WidgetDefault[] currentDefaults = WidgetDefault.findAll();
		for(int i=0;i<currentDefaults.length;i++){
			if(currentDefaults[i].getWidgetContext().equalsIgnoreCase(widgetType)){   
				// found it so update to new widget id
				currentDefaults[i].setWidgetId(key);
				currentDefaults[i].save();
				found=true;
			}
		}
		// didnt find it already set, so add new one
		if(!found){
			WidgetDefault wd = new WidgetDefault();
			wd.setWidgetContext(widgetType);
			wd.setWidgetId(key);	
			wd.save();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manager.IWidgetAdminManager#setWidgetTypesForWidget(int, java.lang.String[], boolean)
	 */
	@SuppressWarnings("unchecked")
	public void setWidgetTypesForWidget(int dbKey, String[] widgetTypes, boolean maximize){
		Widget widget = Widget.findById(dbKey);

		WidgetType widgetType;
		if (widgetTypes!=null){
			for(int i=0;i<widgetTypes.length;i++){	
				if(!doesServiceExistForWidget(widget.getId(), widgetTypes[i])){
					widgetType = new WidgetType();
					widgetType.setWidgetContext(widgetTypes[i]);
					widgetType.setWidget(widget);
					widget.getWidgetTypes().add(widgetType);
					widgetType.save();						
				}
			}
		}			
	}

}
