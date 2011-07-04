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
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.TagNode;

/**
 * A HTML processor implemented using HtmlCleaner
 */
public class HtmlCleaner implements IHtmlProcessor{
	
	// The HTML root node
	private TagNode htmlNode;
	// The HTML <HEAD> tag
	private TagNode headNode;
	// The HtmlCleaner instance
	private org.htmlcleaner.HtmlCleaner cleaner;
	// Properties of the cleaner
	private CleanerProperties properties;
	// The reader for the HTML to process
	private Reader reader;
	// User-specified scripts 
	private ArrayList<TagNode> scriptList;
	
	/**
	 * Creates a new HtmlCleaner
	 */
	public HtmlCleaner(){
		cleaner = new  org.htmlcleaner.HtmlCleaner();
		// set cleaner properties	
		properties  = cleaner.getProperties();
		properties.setOmitDoctypeDeclaration(true);
		properties.setOmitXmlDeclaration(true);
		properties.setUseCdataForScriptAndStyle(true);
		properties.setUseEmptyElementTags(false);	
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.util.html.IHtmlProcessor#setFile(java.io.File)
	 */
	public void setReader(Reader reader) throws IOException{
		if (reader == null) throw new IOException("Reader was null");
		this.reader = reader;
		htmlNode = cleaner.clean(this.reader);			
		headNode = htmlNode.findElementByName(HEAD_TAG, false);	
		// remove widget-specific scripts. These will be replaced
		// after processing, so that the injected scripts come first
		removeUserScripts();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.util.html.IHtmlProcessor#injectScript(java.lang.String)
	 */
	public void injectScript(String script) {
		TagNode js = new TagNode(SCRIPT_TAG);
		js.addAttribute(TYPE_ATTRIBUTE, TYPE_ATTRIBUTE_VALUE);
		js.addAttribute(SRC_ATTRIBUTE, script);
		headNode.addChild(js);
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.util.html.IHtmlProcessor#injectStylesheet(java.lang.String)
	 */
	public void injectStylesheet(String stylesheet) {
		TagNode js = new TagNode(LINK_TAG);
		js.addAttribute(TYPE_ATTRIBUTE, CSS_TYPE_ATTRIBUTE_VALUE);
		js.addAttribute(REL_ATTRIBUTE, CSS_REL_ATTRIBUTE_VALUE);
		js.addAttribute(HREF_ATTRIBUTE, stylesheet);
		headNode.addChild(js);
	}

  /* (non-Javadoc)
   * @see org.apache.wookie.util.html.IHtmlProcessor#setCharset(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public void setTypeAndCharset(String type, String charset) {
    // NB This overrides any existing encoding information in the HTML file.
    
    //
    // Check if the page already has a META http-equiv=content-type tag,
    // if it doesn't create one and add it to the head node
    //
    TagNode meta = headNode.findElementByAttValue("http-equiv", "content-type", true, false);
    if (meta == null) {
      meta = new TagNode(META_TAG);
      meta.addAttribute("http-equiv", "Content-Type");
      headNode.getChildren().add(0, meta);
    }
    //
    // Force UTF into lowercase
    //
    if (charset.equals("UTF-8")) charset = "utf-8";
    
    //
    // Override the charset and content-type values for the 
    // META http-equiv=content-type tag
    //
    meta.addAttribute("content", type + ";charset=" + charset);
  }
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.util.html.IHtmlProcessor#process()
	 */
	public void process(Writer writer) throws IOException{
		if (reader == null) throw new IOException("No file has been specified to process");
		if (writer == null) throw new IOException("No writer provided");
		replaceUserScripts();
		HtmlSerializer ser = new HtmlSerializer(properties);	
		ser.writeXml(htmlNode, writer, "UTF-8");
	}
	

	/**
	 * Removes any widget-specific scripts and stores them to
	 * be replaced after injecting any dependencies
	 */
	private void removeUserScripts(){
		scriptList = new ArrayList<TagNode>();
		getUserScripts();
		for(TagNode node : scriptList){
			headNode.removeChild(node);
		}	
	}
	
	/**
	 * Finds any user script imports and saves them to
	 * the scriptList
	 */
	@SuppressWarnings("unchecked")
	private void getUserScripts(){
		List<TagNode> children = headNode.getChildren();		
		for(TagNode child : children){						
			if(child.getName().equals(SCRIPT_TAG)){				
				scriptList.add(child);	
			}			
		}
	}
	
	/**
	 * Appends widget-specific scripts to the end of the HEAD tag
	 */
	private void replaceUserScripts(){
		for(TagNode node : scriptList){
			headNode.addChild(node);
		}
	}

}
