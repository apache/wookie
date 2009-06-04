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
 * @version $Id: ApiKey.java,v 1.3 2009-06-04 15:09:15 ps3com Exp $
 *
 */
public class ApiKey extends AbstractKeyBean<ApiKey> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @return the key
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param key the key to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	private String value;
	private String email;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/// Active record methods
	public static ApiKey findById(Object id){
		return (ApiKey) findById(ApiKey.class, id);
	}
	
	public static ApiKey[] findByValue(String key, Object value) {
		return (ApiKey[]) findByValue(ApiKey.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static ApiKey[] findByValues(Map map) {
		return (ApiKey[]) findByValues(ApiKey.class, map);
	}
	
	public static ApiKey[] findAll(){
		return (ApiKey[]) findAll(ApiKey.class);
	}

}
