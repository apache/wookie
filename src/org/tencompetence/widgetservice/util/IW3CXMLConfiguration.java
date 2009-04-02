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
package org.tencompetence.widgetservice.util;

/**
 * Constants for widget elements found in the config.xml file
 *
 * @author Paul Sharples
 * @version $Id: IW3CXMLConfiguration.java,v 1.5 2009-04-02 13:18:24 scottwilson Exp $
 */
public interface IW3CXMLConfiguration {
	
	public static final String WIDGET_ELEMENT = "widget";

	public static final String NAME_ELEMENT = "name";
	public static final String DESCRIPTION_ELEMENT = "description";
	public static final String AUTHOR_ELEMENT = "author";	
	public static final String LICENCE_ELEMENT = "licence";
	public static final String ICON_ELEMENT = "icon";
	public static final String ACCESS_ELEMENT = "access";
	public static final String CONTENT_ELEMENT = "content";
	public static final String UPDATE_ELEMENT = "update";
	public static final String FEATURE_ELEMENT = "feature";	
	public static final String PREFERENCE_ELEMENT = "preference";
		
	/**
	 * @deprecated
	 */
	public static final String UID_ATTRIBUTE = "uid";
	public static final String ID_ATTRIBUTE = "id";
	public static final String VERSION_ATTRIBUTE = "version";
	public static final String HEIGHT_ATTRIBUTE = "height";
	public static final String WIDTH_ATTRIBUTE = "width";
	public static final String HREF_ATTRIBUTE = "href";
	public static final String EMAIL_ATTRIBUTE = "email";
	public static final String MODE_ATTRIBUTE = "viewmode";
	public static final String IMG_ATTRIBUTE = "img";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String REQUIRED_ATTRIBUTE = "required";
	public static final String NETWORK_ATTRIBUTE = "network";
	/**
	 * @deprecated
	 */
	public static final String PLUGINS_ATTRIBUTE = "plugin";
	public static final String SOURCE_ATTRIBUTE = "src";
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String CHARSET_ATTRIBUTE = "charset";
	public static final String START_ATTRIBUTE = "start";
	public static final String CHROME_ATTRIBUTE = "chrome";
	public static final String VALUE_ATTRIBUTE = "value";
	public static final String READONLY_ATTRIBUTE = "readonly";	
	public static final String MANIFEST_FILE = "config.xml";
	public static final String MANIFEST_NAMESPACE = "http://www.w3.org/ns/widgets";
	
}
