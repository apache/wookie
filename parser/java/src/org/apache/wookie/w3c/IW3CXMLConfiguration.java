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

package org.apache.wookie.w3c;

/**
 * Constants for widget elements found in the config.xml file
 */
public interface IW3CXMLConfiguration {
	
	public static final String WIDGET_ELEMENT = "widget";
	  public static final String DEFAULTLOCALE_ATTRIBUTE = "defaultlocale"; // widget
		public static final String ID_ATTRIBUTE = "id";  // widget
		public static final String VERSION_ATTRIBUTE = "version"; // widget
		public static final String MODE_ATTRIBUTE = "viewmodes"; //widget
		// Values of widget view modes
		public static final String[] VIEWMODES = {"windowed", "floating", "fullscreen", "maximized", "minimized", "all"};
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
	 	public static final String ORIGIN_ATTRIBUTE = "origin"; // access	
	 	public static final String SUBDOMAINS_ATTRIBUTE = "subdomains"; // access
	 	public static final String[] SUPPORTED_SCHEMES = {"http", "https"};
	 
	public static final String CONTENT_ELEMENT = "content"; // widget [0..*]
	 	public static final String TYPE_ATTRIBUTE = "type"; // content
	 	public static final String CHARSET_ATTRIBUTE = "encoding"; // content
	 	public static final String DEFAULT_CHARSET = "UTF-8"; // content
	 	public static final String DEFAULT_MEDIA_TYPE = "text/html"; // content

	public static final String FEATURE_ELEMENT = "feature";	// widget [0..*]
	 	public static final String REQUIRED_ATTRIBUTE = "required"; // feature
	 	public static final String PARAM_ELEMENT = "param"; // feature [0..*]
	
	public static final String PREFERENCE_ELEMENT = "preference"; // widget [0..*]
	 	public static final String READONLY_ATTRIBUTE = "readonly";	// preference

	public static final String UPDATE_ELEMENT = "update-description";	// widget [0..1]
	
	// Re-used attributes:
	public static final String HEIGHT_ATTRIBUTE = "height"; //widget, icon
	public static final String WIDTH_ATTRIBUTE = "width"; //widget, icon
	public static final String HREF_ATTRIBUTE = "href";  // author, license
	public static final String NAME_ATTRIBUTE = "name"; //feature, param, preference
	public static final String SOURCE_ATTRIBUTE = "src"; // icon, content
	public static final String VALUE_ATTRIBUTE = "value"; // param, preference
	public static final String LANG_ATTRIBUTE = "lang"; // any with xml:lang
	public static final String DEFAULT_LANG = "en"; // any with xml:lang
	public static final String DIR_ATRRIBUTE = "dir"; // any with its:dir	
	//
	// Other values used
	public static final String DEFAULT_WIDGET_VERSION = "1.0";
	public static final String UNKNOWN = "unknown";
 	public static final String DEFAULT_ICON_PATH = "/wookie/shared/images/cog.gif";
	public static final int DEFAULT_HEIGHT_SMALL = 32;
	public static final int DEFAULT_WIDTH_SMALL = 32;
	public static final int DEFAULT_HEIGHT_LARGE = 300;
	public static final int DEFAULT_WIDTH_LARGE = 150;
	public static final String MANIFEST_FILE = "config.xml";
	public static final String MANIFEST_NAMESPACE = "http://www.w3.org/ns/widgets";
	public static final String[] SUPPORTED_CONTENT_TYPES = {"text/html", "image/svg+xml","application/xhtml+xml"};
	public static final String[] START_FILES = {"index.htm","index.html","index.svg","index.xhtml","index.xht"};
	public static final String[] DEFAULT_ICON_FILES = {"icon.svg","icon.ico","icon.png","icon.gif","icon.jpg"};
	
	// Deprecated: used in early drafts of spec:
	
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
