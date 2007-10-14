package org.tencompetence.widgetservice.beans;

import java.util.HashSet;
import java.util.Set;


/**
 * Widget - a simple bean to model a widgets attributes
 * 
 * @author Paul Sharples
 * @version $Id: Widget.java,v 1.1 2007-10-14 10:58:31 ps3com Exp $
 */
public class Widget extends AbstractKeyBean {
	
	private static final long serialVersionUID = 1L;
	
	private String widgetName;
	private String url;
	private int height;
	private int width;
	private boolean maximize;
	
	@SuppressWarnings("unchecked")
	private Set widgetTypes = new HashSet();	
	
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

	public String getWidgetName() {
		return widgetName;
	}

	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}


	public boolean isMaximize() {
		return maximize;
	}


	public void setMaximize(boolean maximize) {
		this.maximize = maximize;
	}
	
}
