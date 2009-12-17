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

import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.util.LocalizationUtils;
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
	
	private String widgetAuthor;
	private String widgetAuthorEmail;
	private String widgetAuthorHref;
	private String guid;	
	private Integer height;
	private Integer width;
	private boolean maximize;	
	@SuppressWarnings("unchecked")
	private Set widgetTypes = new HashSet();
	private String version;
	
	public Widget(){}
	
	public Integer getHeight(){
		return height;
	}
	
	public void setHeight(Integer height){
		this.height = height;
	}

	public Integer getWidth(){
		return width;
	}
	
	public void setWidth(Integer width){
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

	public String getWidgetAuthor() {
		return widgetAuthor;
	}


	public void setWidgetAuthor(String widgetAuthor) {
		this.widgetAuthor = widgetAuthor;
	}


	public String getWidgetAuthorEmail() {
		return widgetAuthorEmail;
	}


	public void setWidgetAuthorEmail(String widgetAuthorEmail) {
		this.widgetAuthorEmail = widgetAuthorEmail;
	}


	public String getWidgetAuthorHref() {
		return widgetAuthorHref;
	}


	public void setWidgetAuthorHref(String widgetAuthorHref) {
		this.widgetAuthorHref = widgetAuthorHref;
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
	
	/**
	 * Temporary convenience method for getting an icon; this
	 * should be replaced with an algorithm that returns an appropriate
	 * icon that matches the user locale so its marked as deprecated
	 */
	@Deprecated
	public String getWidgetIconLocation(){
		WidgetIcon[] icons = WidgetIcon.findForWidget(this);
		if (icons != null && icons.length > 0){
			return icons[0].getSrc();
		} else {
			return IW3CXMLConfiguration.DEFAULT_ICON_PATH;
		}
	}
	
	/**
	 * Temporary convenience method for getting a widget title; calls to this
	 * method should be replaced with calls to getWidgetTitle(locale)
	 */
	@Deprecated
	public String getWidgetTitle(){
		String title = IW3CXMLConfiguration.UNKNOWN;
		Name name = (Name) LocalizationUtils.getLocalizedElement(Name.findByValue("widget", this),new String[]{"en"});
		if (name != null) title = name.getName();
		return title;
	}
	
	/**
	 * Temporary convenience method for getting a widget description; calls to this
	 * method should be replaced with calls to getWidgetDescription(locale)
	 */
	@Deprecated
	public String getWidgetDescription(){
		String value = null;
		Description desc = (Description) LocalizationUtils.getLocalizedElement(Description.findByValue("widget", this),new String[]{"en"});
		if (desc != null) value = desc.getContent();
		return value;
	}
	
	/**
	 * Temporary convenience method for getting a widget url; calls to this
	 * method should be replaced with calls to locate a localized start file 
	 * for a widget instance
	 * @return
	 */
	@Deprecated
	public String getUrl(){
		StartFile startPage = (StartFile) LocalizationUtils.getLocalizedElement(StartFile.findByValue("widget", this), new String[]{"en"});	
		return startPage.getUrl();
	}
	
	/**
	 * Convenience method for obtaining a title for a widget using the given language tag. This method
	 * will always return a String.
	 * @param locale
	 * @return
	 */
	public String getWidgetTitle(String locale){
		String title = IW3CXMLConfiguration.UNKNOWN;
		Name name = (Name) LocalizationUtils.getLocalizedElement(Name.findByValue("widget", this),new String[]{locale});
		if (name != null) title = name.getName();
		return title;
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
		String sqlQuery = "SELECT widget.*, widgettype.widget_context "
						+ "FROM Widget widget, WidgetType widgettype "
						+ "WHERE widget.id = widgettype.widget_id "
						+ "AND widgettype.widget_context='" + typeToSearch + "'";		
		
		List<?> sqlReturnList = dbManager.createSQLQuery(sqlQuery).addEntity(Widget.class).list();
		Widget[] widgets = sqlReturnList.toArray(new Widget[sqlReturnList.size()]);
		return widgets;		 
	}	
	
	public static Widget findDefaultByType(String typeToSearch) {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		String sqlQuery = "SELECT widget.* " //$NON-NLS-1$
			+ "FROM Widget widget, WidgetDefault widgetdefault " //$NON-NLS-1$
			+ "WHERE widget.id = widgetdefault.widgetId " //$NON-NLS-1$
			+ "AND widgetdefault.widgetContext='" + typeToSearch + "'";		 //$NON-NLS-1$ //$NON-NLS-2$
		Widget widget = (Widget)dbManager.createSQLQuery(sqlQuery).addEntity(Widget.class).uniqueResult();	
		return widget;		 
	}
	
	public static boolean exists(String guid){
		Widget[] widget = Widget.findByValue("guid", guid);
		if (widget == null || widget.length!=1) return false;
		return true;	
	}
}
