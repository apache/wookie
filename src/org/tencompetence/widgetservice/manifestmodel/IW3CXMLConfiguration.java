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
package org.tencompetence.widgetservice.manifestmodel;

/**
 * Constants for widget elements found in the config.xml file
 *
 * @author Paul Sharples
 * @version $Id: IW3CXMLConfiguration.java,v 1.1 2009-06-04 15:05:25 ps3com Exp $
 */
public interface IW3CXMLConfiguration {
	
	public static final String WIDGET_ELEMENT = "widget";
		public static final String ID_ATTRIBUTE = "id";  // widget
		public static final String VERSION_ATTRIBUTE = "version"; // widget
		public static final String MODE_ATTRIBUTE = "viewmodes"; //widget
		// Values of widget view modes
		public static final String[] VIEWMODES = {"application", "floating", "fullscreen", "mini", "all"};
		public static final String DEFAULT_VIEWMODE = "floating";
		
	public static final String NAME_ELEMENT = "name"; // widget[0..*]
	 	public static final String SHORT_ATTRIBUTE = "short"; // name
	
	public static final String DESCRIPTION_ELEMENT = "description"; //widget[0..*]
	
	public static final String AUTHOR_ELEMENT = "author"; // widget[0..1]
	 	public static final String EMAIL_ATTRIBUTE = "email"; // author
	
	// Note that we spelled it differently to W3C originally. Oops.
	public static final String LICENSE_ELEMENT = "license"; // widget [0..*]
	
	public static final String ICON_ELEMENT = "icon"; // widget [0..*]
	
	public static final String ACCESS_ELEMENT = "access"; // widget [0..*]
	 	public static final String URI_ATTRIBUTE = "uri"; // access	
	 	public static final String SUBDOMAINS_ATTRIBUTE = "subdomains"; // access
	 
	public static final String CONTENT_ELEMENT = "content"; // widget [0..*]
	 	public static final String TYPE_ATTRIBUTE = "type"; // content
	 	public static final String CHARSET_ATTRIBUTE = "charset"; // content
	 	public static final String DEFAULT_CHARSET = "UTF-8"; // content
	 	public static final String DEFAULT_MEDIA_TYPE = "text/html"; // content

	public static final String FEATURE_ELEMENT = "feature";	// widget [0..*]
	 	public static final String REQUIRED_ATTRIBUTE = "required"; // feature
	 	public static final String PARAM_ELEMENT = "param"; // feature [0..*]
	
	public static final String PREFERENCE_ELEMENT = "preference"; // widget [0..*]
	 	public static final String READONLY_ATTRIBUTE = "readonly";	// preference


	// Re-used attributes:
	public static final String HEIGHT_ATTRIBUTE = "height"; //widget, icon
	public static final String WIDTH_ATTRIBUTE = "width"; //widget, icon
	public static final String HREF_ATTRIBUTE = "href";  // author, license
	public static final String NAME_ATTRIBUTE = "name"; //feature, param, preference
	public static final String SOURCE_ATTRIBUTE = "src"; // icon, content
	public static final String VALUE_ATTRIBUTE = "value"; // param, preference
	public static final String LANG_ATTRIBUTE = "lang"; // any with xml:lang
	public static final String DEFAULT_LANG = "en"; // any with xml:lang
	
	//
	// Other values used
	public static final String DEFAULT_WIDGET_VERSION = "1.0";
	public static final String UNKNOWN = "unknown";
 	public static final String DEFAULT_SRC_PAGE = "index.htm";
 	public static final String DEFAULT_ICON_PATH = "/wookie/shared/images/cog.gif";
	public static final int DEFAULT_HEIGHT_SMALL = 32;
	public static final int DEFAULT_WIDTH_SMALL = 32;
	public static final int DEFAULT_HEIGHT_LARGE = 300;
	public static final int DEFAULT_WIDTH_LARGE = 150;
	public static final String MANIFEST_FILE = "config.xml";
	public static final String MANIFEST_NAMESPACE = "http://www.w3.org/ns/widgets";
	
	
	// Deprecated: used in early drafts of spec:
	
	/**
	 * @deprecated
	 */
	public static final String UPDATE_ELEMENT = "update";
	/**
	 * @deprecated
	 */
	public static final String UID_ATTRIBUTE = "uid";
	/**
	 * @deprecated
	 */
	public static final String NETWORK_ATTRIBUTE = "network";
	/**
	 * @deprecated
	 */
	public static final String PLUGINS_ATTRIBUTE = "plugin";
	/**
	 * @deprecated
	 */
	public static final String START_ATTRIBUTE = "start";
	/**
	 * @deprecated
	 */
	public static final String CHROME_ATTRIBUTE = "chrome";
	/**
	 * @deprecated
	 */
	public static final String IMG_ATTRIBUTE = "img";
}
