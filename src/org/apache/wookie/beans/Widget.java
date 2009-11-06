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

package org.apache.wookie.beans;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wookie.util.hibernate.DBManagerFactory;
import org.apache.wookie.util.hibernate.IDBManager;


/**
 * Widget - a simple bean to model a widgets attributes
 * 
 * @author Paul Sharples
 * @version $Id: Widget.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public class Widget extends AbstractKeyBean<Widget> {
	
	private static final long serialVersionUID = 1L;
	
	private String widgetTitle;
	private String widgetShortName;
	private String widgetDescription;
	private String widgetAuthor;
	private String widgetIconLocation;
	private String url;
	private String guid;	
	private int height;
	private int width;
	private boolean maximize;	
	@SuppressWarnings("unchecked")
	private Set widgetTypes = new HashSet();
	private String version;
	
	public Widget(){}
	
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void setHeight(int height){
		this.height = height;
	}

	public int getWidth(){
		return width;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	@SuppressWarnings("unchecked")
	public Set getWidgetTypes() {
	    return widgetTypes;
	}

	@SuppressWarnings("unchecked")
	public void setWidgetTypes(Set widgetTypes) {
	    this.widgetTypes = widgetTypes;
	}


	public boolean isMaximize() {
		return maximize;
	}


	public void setMaximize(boolean maximize) {
		this.maximize = maximize;
	}


	public String getGuid() {
		return guid;
	}


	public void setGuid(String guid) {
		this.guid = guid;
	}


	public String getWidgetTitle() {
		return widgetTitle;
	}


	public void setWidgetTitle(String widgetTitle) {
		this.widgetTitle = widgetTitle;
	}

	public String getWidgetShortName() {
		return widgetShortName;
	}


	public void setWidgetShortName(String name) {
		this.widgetShortName = name;
	}
	
	public String getWidgetDescription() {
		return widgetDescription;
	}


	public void setWidgetDescription(String widgetDescription) {
		this.widgetDescription = widgetDescription;
	}


	public String getWidgetAuthor() {
		return widgetAuthor;
	}


	public void setWidgetAuthor(String widgetAuthor) {
		this.widgetAuthor = widgetAuthor;
	}


	public String getWidgetIconLocation() {
		return widgetIconLocation;
	}


	public void setWidgetIconLocation(String widgetIconLocation) {
		this.widgetIconLocation = widgetIconLocation;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/// Active record methods
	public static Widget findById(Object id){
		return (Widget) findById(Widget.class, id);
	}
	
	public static Widget[] findByValue(String key, Object value) {
		return (Widget[]) findByValue(Widget.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static Widget[] findByValues(Map map) {
		return (Widget[]) findByValues(Widget.class, map);
	}
	
	public static Widget[] findAll(){
		return (Widget[]) findAll(Widget.class);
	}
	
	////// Special queries
	
	/**
	 * Find one widget by guid
	 */
	public static Widget findByGuid(String guid){
		Widget[] widget = Widget.findByValue("guid", guid);
		if (widget == null || widget.length !=1) return null;
		return widget[0];
	}
	
	/**
	 * Get all widgets of a particular type - e.g. - chat
	 * @param typeToSearch - a widget type
	 * @return - returns an array of widgets
	 */
	public static Widget[] findByType(String typeToSearch) {
		final IDBManager dbManager = DBManagerFactory.getDBManager();			
		String sqlQuery = "SELECT widget.id, widget.widget_title,  widget.widget_short_name, widget_description, widget_author, widget_icon_location, widget.url, widget.maximize, widget.guid, " +
								"widget.height, widget.width, widget_version, widgettype.widget_context "
						+ "FROM Widget widget, WidgetType widgettype "
						+ "WHERE widget.id = widgettype.widget_id "
						+ "AND widgettype.widget_context='" + typeToSearch + "'";		
		
		List<?> sqlReturnList = dbManager.createSQLQuery(sqlQuery).addEntity(Widget.class).list();
		Widget[] widgets = sqlReturnList.toArray(new Widget[sqlReturnList.size()]);
		return widgets;		 
	}	
	
	public static Widget findDefaultByType(String typeToSearch) {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		String sqlQuery = "SELECT widget.id, widget.widget_title, widget.widget_short_name, widget_version, widget_description, widget_author, widget_icon_location, widget.url, widget.height, widget.width, widget.maximize, widget.guid " //$NON-NLS-1$
			+ "FROM Widget widget, WidgetDefault widgetdefault " //$NON-NLS-1$
			+ "WHERE widget.id = widgetdefault.widgetId " //$NON-NLS-1$
			+ "AND widgetdefault.widgetContext='" + typeToSearch + "'";		 //$NON-NLS-1$ //$NON-NLS-2$
		Widget widget = (Widget)dbManager.createSQLQuery(sqlQuery).addEntity(Widget.class).uniqueResult();	
		return widget;		 
	}
}
