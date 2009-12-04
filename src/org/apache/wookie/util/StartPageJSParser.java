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

package org.apache.wookie.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.ServerFeature;
import org.apache.wookie.feature.IFeature;
import org.apache.wookie.manifestmodel.IFeatureEntity;
import org.apache.wookie.manifestmodel.IManifestModel;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;

/**
 * Parse the HTML start page & add the JS file links
 * 
 * We need to add in the following links to the HTML file.
 *
 * <script type="text/javascript" src="/wookie/dwr/util.js"></script>
 * <script type="text/javascript" src="/wookie/dwr/engine.js"></script>
 * <script type="text/javascript" src="/wookie/dwr/interface/WidgetImpl.js"></script>
 * <script type="text/javascript" src="/wookie/shared/js/wookie-wrapper.js"></script>
 *
 * @author Paul Sharples
 * @version $Id: StartPageJSParser.java,v 1.2 2009-07-28 16:05:21 scottwilson Exp $
 */
public class StartPageJSParser implements IStartPageConfiguration {
	
	static Logger _logger = Logger.getLogger(StartPageJSParser.class.getName());

	private HtmlCleaner fCleaner = null;
	private CleanerProperties fProps = null;
	private File fStartPage = null;
	List<TagNode> fScriptList = null;
	private IManifestModel fWidget = null;
	
	public StartPageJSParser(File startPage, IManifestModel model) {
		fScriptList = new ArrayList<TagNode>();
		fStartPage = startPage;		
		fWidget = model;
	}
	
	private boolean doesAttributeValueExistsInNode(TagNode node, String attrName, String attrValue){
		TagNode foundNode = node.findElementByAttValue(attrName, attrValue, false, true);
		if(foundNode == null){
			return false;
		}
		return true;
	}
	
	private TagNode createScriptTag(String srcAttribute){
		TagNode js = new TagNode(SCRIPT_TAG);
		js.addAttribute(TYPE_ATTRIBUTE, TYPE_ATTRIBUTE_VALUE);
		js.addAttribute(SRC_ATTRIBUTE, srcAttribute);
		return js;
	}
	
	@SuppressWarnings("unchecked")
	private void findNonWookieScriptTags(TagNode headNode){		
		List<TagNode> children = headNode.getChildren();		
		for(TagNode child : children){						
			if(child.getName().equals(SCRIPT_TAG)){				
				String attr = child.getAttributeByName(SRC_ATTRIBUTE);	
				if (attr != null){
				if(!attr.equals(DWR_UTIL_SRC_VALUE) & !attr.equals(DWR_ENGINE_SRC_VALUE) 
						& !attr.equals(WIDGET_IMPL_SRC_VALUE) & !attr.equals(WOOKIE_WRAPPER_SRC_VALUE)){
					fScriptList.add(child);					
				}				
				}
			}			
		}
	}
			
	@SuppressWarnings("unchecked")
	public void doParse(){
		fCleaner = new HtmlCleaner();
		// take default cleaner properties		
		fProps = fCleaner.getProperties();
		fProps.setOmitDoctypeDeclaration(false);
		fProps.setOmitXmlDeclaration(true);
		fProps.setUseCdataForScriptAndStyle(true);
		
		fProps.setUseEmptyElementTags(false);		
		try {
			TagNode htmlNode = fCleaner.clean(fStartPage);			
			TagNode headNode = htmlNode.findElementByName(HEAD_TAG, false);									
			if(headNode != null){
				// find any script tags
				findNonWookieScriptTags(headNode);
				// remove any script tags which are not wookie references
				for(TagNode node : fScriptList){
					headNode.removeChild(node);
				}	
				// look for wookie js links - add them in if not there already
				if(!doesAttributeValueExistsInNode(headNode, SRC_ATTRIBUTE, DWR_UTIL_SRC_VALUE)){
					_logger.debug("DWR_UTIL_SRC_VALUE NOT found");
					TagNode jsTag = createScriptTag(DWR_UTIL_SRC_VALUE);
					headNode.addChild(jsTag);					
				}
				if(!doesAttributeValueExistsInNode(headNode, SRC_ATTRIBUTE, DWR_ENGINE_SRC_VALUE)){
					_logger.debug("DWR_ENGINE_SRC_VALUE NOT found");
					TagNode jsTag = createScriptTag(DWR_ENGINE_SRC_VALUE);
					headNode.addChild(jsTag);
				}
				if(!doesAttributeValueExistsInNode(headNode, SRC_ATTRIBUTE, WIDGET_IMPL_SRC_VALUE)){
					_logger.debug("WIDGET_IMPL_SRC_VALUE NOT found");
					TagNode jsTag = createScriptTag(WIDGET_IMPL_SRC_VALUE);
					headNode.addChild(jsTag);
				}
				if(!doesAttributeValueExistsInNode(headNode, SRC_ATTRIBUTE, WOOKIE_WRAPPER_SRC_VALUE)){
					_logger.debug("WOOKIE_WRAPPER_SRC_VALUE NOT found");
					TagNode jsTag = createScriptTag(WOOKIE_WRAPPER_SRC_VALUE);
					headNode.addChild(jsTag);
				}
				
				// Add features.
				// For each requested feature, check if there is a corresponding installed ServerFeature
				// If so, inject its JS into the header.
				
				for (IFeatureEntity feature: fWidget.getFeatures()){
					ServerFeature sf = ServerFeature.findByName(feature.getName());
					if (sf!=null){
						Class<? extends IFeature> klass;
						try {
							klass = (Class<? extends IFeature>) Class.forName(sf.getClassName());
							IFeature theFeature = (IFeature) klass.newInstance();
							if(theFeature.getJavaScriptImpl() != null){
								if(!doesAttributeValueExistsInNode(headNode, SRC_ATTRIBUTE, theFeature.getJavaScriptImpl())){
									TagNode jsTag = createScriptTag(theFeature.getJavaScriptImpl());
									headNode.addChild(jsTag);
								}
							}
							if(theFeature.getJavaScriptWrapper() != null){
								if(!doesAttributeValueExistsInNode(headNode, SRC_ATTRIBUTE, theFeature.getJavaScriptWrapper())){
									TagNode jsTag = createScriptTag(theFeature.getJavaScriptWrapper());
									headNode.addChild(jsTag);
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				// add back the orginal script tags - at the end - so that the local JS will load last!
				for(TagNode node : fScriptList){
					headNode.addChild(node);
				}				
				PrettyXmlSerializer ser = new PrettyXmlSerializer(fProps);	
				ser.writeXmlToFile(htmlNode, fStartPage.getAbsolutePath());		
				
				// go back and strip out the CDATA sections we wrapped our scripts in
				File file = new File(fStartPage.getAbsolutePath());
				String result = FileUtils.readFileToString(file);
		        String contentResult = result.toString().replaceAll("<!\\[CDATA\\[", "");
		        contentResult = contentResult.toString().replaceAll("\\]\\]>", "");
				FileUtils.writeStringToFile(file, contentResult);

			}
		} 
		catch (IOException ex) {
			_logger.error("doParse() failed:", ex);
		}
	} 
}
