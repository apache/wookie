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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.beans.Participant;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.PreferenceDefault;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.Whitelist;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetDefault;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.beans.WidgetService;
import org.tencompetence.widgetservice.beans.WidgetType;
import org.tencompetence.widgetservice.manager.IWidgetAdminManager;
import org.tencompetence.widgetservice.util.ManifestHelper;

/**
 * WidgetAdminManager
 * 
 * This class is responsible for administrative functions such as adding new widget types
 * and setting which widget is to be the default
 * 
 * @author Paul Sharples
 * @version $Id: WidgetAdminManager.java,v 1.13 2009-06-03 22:06:51 scottwilson Exp $
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
		WidgetService service = new WidgetService();
		service.setServiceName(serviceName);
		return service.save();
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
        Widget widget;
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
		widget.save();	       	        
		WidgetType widgetType;
		if (widgetTypes!=null){
			for(int i=0;i<widgetTypes.length;i++){
				widgetType = new WidgetType();
				widgetType.setWidgetContext(widgetTypes[i]);
				widgetType.setWidget(widget);
				widget.getWidgetTypes().add(widgetType);
				widgetType.save();
			}
		}
		newWidgetIdx = widget.getId();
		// Save default preferences
		if (prefs != null){
			for (PreferenceDefault pref:(PreferenceDefault[])prefs.toArray(new PreferenceDefault[prefs.size()])){
				pref.setWidget(widget);
				pref.save();
			}
		}
        return newWidgetIdx;	       
	 }
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#addWhiteListEntry(java.lang.String)
	 */
	public boolean addWhiteListEntry(String uri) {
		Whitelist list = new Whitelist();
		list.setfUrl(uri);
		return list.save();
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#deleteWidgetDefaultById(int)
	 */
	public void deleteWidgetDefaultById(int widgetKey){		
		WidgetDefault[] widgetDefault = WidgetDefault.findByValue("widgetId", widgetKey);
		if (widgetDefault.length == 1) widgetDefault[0].delete();
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#deleteWidgetDefaultByIdAndServiceType(int, java.lang.String)
	 */
	public void deleteWidgetDefaultByIdAndServiceType(int widgetKey, String serviceType){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("widgetId", widgetKey);
		map.put("widgetContext", serviceType);
		WidgetDefault[] widgetDefaults;
		widgetDefaults = WidgetDefault.findByValues(map);
		WidgetDefault.delete(widgetDefaults);
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#deleteWidgetDefaultByServiceName(java.lang.String)
	 */
	public void deleteWidgetDefaultByServiceName(String serviceName){
		WidgetDefault[] widgetDefaults = WidgetDefault.findByValue("widgetContent", serviceName);
		WidgetDefault.delete(widgetDefaults);
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#doesServiceExistForWidget(int, java.lang.String)
	 */
	public boolean doesServiceExistForWidget(int dbkey, String serviceType){
		Widget widget = Widget.findById(String.valueOf(dbkey));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("widget", widget);
		map.put("widgetContext", serviceType);
		WidgetType[] types = WidgetType.findByValues(map);
		if (types == null || types.length !=1) return false;
		return true;					
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#doesWidgetAlreadyExistInSystem(java.lang.String)
	 */
	public boolean doesWidgetAlreadyExistInSystem(String guid){
		Widget[] widget = Widget.findByValue("guid", guid);
		if (widget == null || widget.length!=1) return false;
		return true;		
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#isWidgetMaximized(int)
	 */
	public boolean isWidgetMaximized(int dbKey){
		Widget widget = null;
		widget = Widget.findById(dbKey);
		if (widget == null) return false;
		return widget.isMaximize();
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#printOutAllWidgets(org.tencompetence.widgetservice.manager.IWidgetAdminManager)
	 */	
	@SuppressWarnings("unchecked")
	public void printOutAllWidgets(IWidgetAdminManager magr){				
		Widget[] widgets = Widget.findAll();
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
		WidgetService service = WidgetService.findById(String.valueOf(serviceId));
		String serviceName = service.getServiceName();
		
		// if exists, remove from widget default table
		deleteWidgetDefaultByServiceName(serviceName);
		
		// delete from the widget service table
		service.delete();	
		// remove any widgetTypes for each widget that match
		Widget[] widgets = Widget.findByType(serviceName);
		if (widgets == null||widgets.length==0) return true;
		for(Widget widget : widgets){
			// remove any widget types for this widget
			Set<?> types = widget.getWidgetTypes();
		    WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
		    for(int j=0;j<widgetTypes.length;++j){	
		    	if(serviceName.equalsIgnoreCase(widgetTypes[j].getWidgetContext())){
		    		widgetTypes[j].delete();
		    	}
			}
		}					
		return true;
	}	
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#removeSingleWidgetType(int, java.lang.String)
	 */
	public boolean removeSingleWidgetType(int widgetId, String widgetType) {
		boolean response = false;	
		Widget widget = Widget.findById(String.valueOf(widgetId));
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
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#removeWhiteListEntry(int)
	 */
	public boolean removeWhiteListEntry(int entryId) {
		Whitelist entry = Whitelist.findById(String.valueOf(entryId));
		return entry.delete();
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#removeWidgetAndReferences(int)
	 */
	public boolean removeWidgetAndReferences(int widgetId){
		// get the widget
		Widget widget = Widget.findById(String.valueOf(widgetId));
		// remove any defaults for this widget
		deleteWidgetDefaultById(widgetId);
		
		if(widget==null) return false;
		// find any widget instances for this widget
		WidgetInstance[] instances = WidgetInstance.findByValue("widget", widget);		
		// try to remove prefs, shareddata and then the instances
		for(WidgetInstance inst : instances){
			SharedData.delete(SharedData.findByValue("widgetInstance", inst));
			Preference.delete(Preference.findByValue("widgetInstance", inst));
			Participant.delete(Participant.findByValue("widgetInstance", inst));
			inst.delete();
		}
		// remove any widget types for this widget
		Set<?> types = widget.getWidgetTypes();
        WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
        for(int j=0;j<widgetTypes.length;++j){	
        	widgetTypes[j].delete();
		}
		// remove the widget itself
        widget.delete();
		return true;
	} 
	
	
	
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#setDefaultWidget(int, java.lang.String)
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
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#setWidgetTypesForWidget(int, java.lang.String[], boolean)
	 */
	@SuppressWarnings("unchecked")
	public void setWidgetTypesForWidget(int dbKey, String[] widgetTypes, boolean maximize){
		Widget widget = Widget.findById(dbKey);
		if(maximize){
			widget.setMaximize(maximize);
			widget.save();
		}

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

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAdminManager#getWidgetGuid(int)
	 */
	public String getWidgetGuid(int dbKey) {
		Widget widget = Widget.findById(String.valueOf(dbKey));
		return widget.getGuid();
	}

}
