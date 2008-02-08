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
package org.tencompetence.widgetservice.beans;

import java.util.HashSet;
import java.util.Set;


/**
 * Widget - a simple bean to model a widgets attributes
 * 
 * @author Paul Sharples
 * @version $Id: Widget.java,v 1.4 2008-02-08 09:41:39 ps3com Exp $
 */
public class Widget extends AbstractKeyBean {
	
	private static final long serialVersionUID = 1L;
	
	private String widgetTitle;
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
	
}
