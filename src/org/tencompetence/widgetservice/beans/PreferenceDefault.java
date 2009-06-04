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

import java.util.Map;

/** 
 * @author Scott Wilson
 * @version $Id: PreferenceDefault.java,v 1.3 2009-06-04 15:11:48 ps3com Exp $ 
 */
public class PreferenceDefault extends AbstractKeyBean<PreferenceDefault> {

	private static final long serialVersionUID = 3036952538484789860L;

	private Widget widget;
	private String preference;
	private String value;
	private boolean readOnly;
	
	public Widget getWidget(){
		return widget;
	}
	
	public void setWidget(Widget w){
		widget = w;
	}
	
	/**
	 * @return the preference
	 */
	public String getPreference() {
		return preference;
	}
	/**
	 * @param preference the preference to set
	 */
	public void setPreference(String preference) {
		this.preference = preference;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/// Active record methods
	public static PreferenceDefault findById(Object id){
		return (PreferenceDefault) findById(PreferenceDefault.class, id);
	}
	
	public static PreferenceDefault[] findByValue(String key, Object value) {
		return (PreferenceDefault[]) findByValue(PreferenceDefault.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static PreferenceDefault[] findByValues(Map map) {
		return (PreferenceDefault[]) findByValues(PreferenceDefault.class, map);
	}
	
	public static PreferenceDefault[] findAll(){
		return (PreferenceDefault[]) findAll(PreferenceDefault.class);
	}

}
