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

import java.io.Serializable;
import java.util.Map;

/**
 * A default widget entity
 * 
 * @author Paul Sharples
 * @version $Id: WidgetDefault.java,v 1.4 2009-06-03 10:06:17 scottwilson Exp $
 */
public class WidgetDefault extends ActiveRecord<WidgetDefault> implements Serializable {

	private static final long serialVersionUID = -8585379777119902714L;
	
	private String widgetContext;
	private Integer widgetId;

	public WidgetDefault() {
	}

	public Integer getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(Integer widgetId) {
		this.widgetId = widgetId;
	}

	public String getWidgetContext() {
		return widgetContext;
	}

	public void setWidgetContext(String widgetContext) {
		this.widgetContext = widgetContext;
	}
	/// Active record methods
	public static WidgetDefault findById(String id){
		return (WidgetDefault) findById(WidgetDefault.class, id);
	}
	public static WidgetDefault[] findAll(){
		return (WidgetDefault[]) findAll(WidgetDefault.class);		
	}
	public static WidgetDefault[] findByValue(String key, Object value){
		return (WidgetDefault[]) findByValue(WidgetDefault.class, key, value);		
	}
	@SuppressWarnings("unchecked")
	public static WidgetDefault[] findByValues(Map map){
		return (WidgetDefault[]) findByValues(WidgetDefault.class,map);			
	}

}
