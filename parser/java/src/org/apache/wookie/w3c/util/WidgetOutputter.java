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
package org.apache.wookie.w3c.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.wookie.w3c.IContentEntity;
import org.apache.wookie.w3c.IIconEntity;
import org.apache.wookie.w3c.W3CWidget;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

/**
 * Used for creating a config.xml file from a Widget object
 */
public class WidgetOutputter {
	
	/**
	 * This should be set to the local path which was prepended to widgets when they were originally parsed
	 */
	private String widgetFolder;
	
	public WidgetOutputter(){
	}
	
	/**
	 * Output config.xml to a File
	 * @param widget
	 * @param file
	 * @throws IOException
	 */
	public void outputXML(W3CWidget widget, File file) throws IOException{
		Document doc = createWidgetDocument(widget);
		XMLOutputter out = new XMLOutputter();
		FileOutputStream fos = new FileOutputStream(file);
		out.output(doc, fos);
	}
	
	/**
	 * Output config.xml to an OutputStream
	 * @param widget
	 * @param out
	 * @throws IOException
	 */
	public void outputXML(W3CWidget widget, java.io.OutputStream out) throws IOException{
		Document doc = createWidgetDocument(widget);
		XMLOutputter outputter = new XMLOutputter();
		outputter.output(doc, out);		
	}
	
	/**
	 * Output config.xml as a String
	 * @param widget
	 * @return
	 */
	public String outputXMLString(W3CWidget widget){
		Document doc = createWidgetDocument(widget);
		XMLOutputter outputter = new XMLOutputter();
		return outputter.outputString(doc);
	}
	
	/**
	 * Create an XML document for the Widget
	 * @param widget
	 * @return a Document representing the Widget's config.xml
	 */
	private Document createWidgetDocument(W3CWidget widget){
		widget = replacePaths(widget);
		return new Document(widget.toXml());
	}
	
	/**
	 * Makes paths in the config.xml point to local resources rather than their installed path
	 * @param widget
	 * @return
	 */
	private W3CWidget replacePaths(W3CWidget widget){
		
		// Get folder name for widget id
		String folder = WidgetPackageUtils.convertIdToFolderName(widget.getIdentifier());
		String installedPath = widgetFolder + File.separator + folder + File.separator;
		
		W3CWidget localWidget = widget;
		
		// Replace Content Src attributes
		for (IContentEntity content:localWidget.getContentList()){
			String src = content.getSrc();
			src = src.replace(installedPath, "");
			content.setSrc(src);
		}
		
		// Replace Icon Src attributes
		for (IIconEntity icon:localWidget.getIconsList()){
			String src = icon.getSrc();
			src = src.replace(installedPath, "");
			icon.setSrc(src);
		}
		
		return localWidget;
	}

	public String getWidgetFolder() {
		return widgetFolder;
	}

	public void setWidgetFolder(String widgetFolder) {
		this.widgetFolder = widgetFolder;
	}

}
