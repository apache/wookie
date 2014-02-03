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
package org.apache.wookie.util.html;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Interface for HTML processors used to inject scripts into
 * start pages and commit other modifications.
 * 
 * Instances of this class should be used as follows:
 * 
 * IHtmlProcessor proc = new <em>HtmlProcessorImplClass</em>
 * proc.setFile(file);
 * ... set any modifications ...
 * proc.process();
 * 
 */
public interface IHtmlProcessor {
	
	final String HEAD_TAG = "head";
	final String SCRIPT_TAG = "script";	
	final String LINK_TAG = "link";	
	final String META_TAG = "meta";	
	final String TYPE_ATTRIBUTE = "type";
	final String TYPE_ATTRIBUTE_VALUE = "text/javascript";	
	final String CSS_TYPE_ATTRIBUTE_VALUE = "text/css";	
	final String REL_ATTRIBUTE = "rel";		
	final String CSS_REL_ATTRIBUTE_VALUE = "stylesheet";	
	final String SRC_ATTRIBUTE = "src";	
	final String HREF_ATTRIBUTE = "href";	

	/**
	 * Set the reader for the source of the content to process.
	 * @param reader the Reader for the HTML content
	 * @throws IOException if the reader does not exist, cannot be read from, or its content cannot be parsed
	 */
	public void setReader(Reader reader) throws IOException;
	
	/**
	 * Injects a JavaScript import with the given src attribute
	 * @param src the src attribute of the Script
	 */
	public void injectScript(String src);
	
	/**
	 * Injects a CSS stylesheet reference with the given href attribute
	 * @param href the href attribute of the Link
	 */
	public void injectStylesheet(String href);
	
	/**
	 * Sets the type and charset of the HTML document
	 * @param type
	 * @param charset
	 */
	public void setTypeAndCharset(String type, String charset);
	
	/**
	 * Processes the HTML and writes the output to the specified writer
	 * @throws IOException if the file cannot be processed or written
	 */
	public void process(Writer writer) throws IOException;

}
