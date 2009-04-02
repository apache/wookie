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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 * Manifest Helper class - methods for uploading the zip & parsing a w3c widget manifest.
 * 
 * @author Paul Sharples
 * @version $Id: ManifestHelper.java,v 1.9 2009-04-02 13:18:24 scottwilson Exp $ 
 *
 */
public class ManifestHelper implements IW3CXMLConfiguration {
	
	public static final String ICON_SOURCE = "icon_src";
	public static final String ICON_HEIGHT = "icon_height";
	public static final String ICON_WIDTH = "icon_width";
	public static final String ICON_NAME = "icon_name";
	
	public static final String FEATURE_NAME = "feature_name";
	public static final String FEATURE_REQUIRED = "feature_required";

	static Logger _logger = Logger.getLogger(ManifestHelper.class.getName());

	public static Hashtable<String,String> dealWithManifest(String xmlText) throws JDOMException, IOException {
		String name, description, author, startFile;
		SAXBuilder builder = new SAXBuilder();
		Element root = builder.build(new StringReader(xmlText)).getRootElement();
		if(root.getName().equalsIgnoreCase(WIDGET_ELEMENT)){
			Hashtable<String,String> manifestValues = new Hashtable<String,String>();
			
			if(root.getAttributeValue(ID_ATTRIBUTE)!=null){
				manifestValues.put(UID_ATTRIBUTE, root.getAttributeValue(ID_ATTRIBUTE));
			}
			else{ 
				if(root.getAttributeValue(UID_ATTRIBUTE)!=null){
					manifestValues.put(UID_ATTRIBUTE, root.getAttributeValue(UID_ATTRIBUTE));
				} else {
					// make one up
					manifestValues.put(UID_ATTRIBUTE, RandomGUID.getUniqueID("generated-uid-"));
				}
			}
						
			if(root.getAttributeValue(VERSION_ATTRIBUTE)!=null){
				manifestValues.put(VERSION_ATTRIBUTE, root.getAttributeValue(VERSION_ATTRIBUTE));
			}
			
			if(root.getAttributeValue(HEIGHT_ATTRIBUTE)!=null){
				manifestValues.put(HEIGHT_ATTRIBUTE, root.getAttributeValue(HEIGHT_ATTRIBUTE));
			}
			else{
				manifestValues.put(HEIGHT_ATTRIBUTE, "300");
			}
			
			if(root.getAttributeValue(WIDTH_ATTRIBUTE)!=null){
				manifestValues.put(WIDTH_ATTRIBUTE, root.getAttributeValue(WIDTH_ATTRIBUTE));
			}
			else{
				manifestValues.put(WIDTH_ATTRIBUTE, "150");
			}
			
			if(root.getAttributeValue(MODE_ATTRIBUTE)!=null){
				manifestValues.put(MODE_ATTRIBUTE, root.getAttributeValue(MODE_ATTRIBUTE));
			}
			else{
				manifestValues.put(MODE_ATTRIBUTE, "default");				
			}
			
			
			// name element --------------------------------------------------------------------------------
			Element nameElement = root.getChild(NAME_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));
			if(nameElement==null){
				name = "Unnamed Widget";
			}
			else{
				name = nameElement.getText();
			}
			manifestValues.put(NAME_ELEMENT, name);
			
			
			// description element -------------------------------------------------------------------------
			Element descElement = root.getChild(DESCRIPTION_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));									
			if(descElement==null){
				description = "No Description";
			}
			else{
				description = descElement.getText();
			}
			manifestValues.put(DESCRIPTION_ELEMENT, description);
			
		
			// author element ------------------------------------------------------------------------------
			// specific author attributes
			String authorImage=null, authorHref=null, authorEmail=null;
			Element authorElement = root.getChild(AUTHOR_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));									
			if(authorElement==null){
				author = "Anonymous";
			}
			else{
				author = authorElement.getText();
				authorImage = authorElement.getAttributeValue(IMG_ATTRIBUTE);
				authorHref = authorElement.getAttributeValue(HREF_ATTRIBUTE);
				authorEmail = authorElement.getAttributeValue(EMAIL_ATTRIBUTE);
			}
			manifestValues.put(AUTHOR_ELEMENT, author);
			if ( authorImage != null ) manifestValues.put(NAME_ATTRIBUTE, authorImage);
			if ( authorHref != null ) manifestValues.put(HREF_ATTRIBUTE, authorHref);
			if ( authorEmail != null ) manifestValues.put(EMAIL_ATTRIBUTE, authorEmail);
			
			
			// access element ---------------------------------------------------------------------------
			Element accessElement = root.getChild(ACCESS_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));
			if ( accessElement != null ) {
				String network = accessElement.getAttributeValue(NETWORK_ATTRIBUTE);
				if ( network != null ) manifestValues.put(NETWORK_ATTRIBUTE, network);
			}
			
			// content element --------------------------------------------------------------------------
			// content specific values
			String contentType=null, contentCharset=null;
			Element contentElement = root.getChild(CONTENT_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));
			if ( contentElement == null ) {
				startFile = "index.html";
			}
			else {
				startFile = contentElement.getAttributeValue(SOURCE_ATTRIBUTE);
				if ( startFile == null ) startFile = "index.html";
				contentType = contentElement.getAttributeValue(TYPE_ATTRIBUTE);
				contentCharset = contentElement.getAttributeValue(CHARSET_ATTRIBUTE);
			}
			manifestValues.put(SOURCE_ATTRIBUTE, startFile);
			if ( contentType != null ) manifestValues.put(TYPE_ATTRIBUTE, contentType);
			if ( contentCharset != null ) manifestValues.put(CHARSET_ATTRIBUTE, contentCharset);
			
			
			// licence element ---------------------------------------------------------------------------
			Element licenceElement = root.getChild(LICENCE_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE) );
			if ( licenceElement != null ) {
				manifestValues.put(LICENCE_ELEMENT, licenceElement.getText());
			}
			
			
			// icon elements -----------------------------------------------------------------------------
			// possibility of multiple icons, keys will be indexed for retrieval eg. "icon_name_1", "icon_source_1" etc...
			List<?> icons = root.getChildren(ICON_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));
			ListIterator<?> i_iter = icons.listIterator();
			while ( i_iter.hasNext() ) {
				int i = 1;
				Element anIcon = (Element)i_iter.next();
				String iconName = anIcon.getText();
				String iconSrc = anIcon.getAttributeValue(SOURCE_ATTRIBUTE);
				String iconWidth = anIcon.getAttributeValue(WIDTH_ATTRIBUTE);
				String iconHeight = anIcon.getAttributeValue(HEIGHT_ATTRIBUTE);
				manifestValues.put(ICON_NAME+"_"+i, iconName);
				manifestValues.put(ICON_SOURCE+"_"+i, iconSrc);
				if ( iconWidth != null ) manifestValues.put(ICON_WIDTH+"_"+i, iconWidth);
				if ( iconHeight != null ) manifestValues.put(ICON_HEIGHT+"_"+i, iconHeight);
				i++;
			}
			
			// feature elements ---------------------------------------------------------------------------
			// possibility of multiple features, keys will be indexed for retrieval eg. "feature_name_1", "feature_required_1" etc...
			List<?> features = root.getChildren(FEATURE_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));
			ListIterator<?> f_iter = features.listIterator();
			while ( f_iter.hasNext()) {
				int i = 1;
				Element aFeature = (Element)f_iter.next();
				String featureName = aFeature.getAttributeValue(NAME_ATTRIBUTE);
				String featureRequired = aFeature.getAttributeValue(REQUIRED_ATTRIBUTE);
				manifestValues.put(FEATURE_NAME+"_"+i, featureName);
				if ( featureRequired != null ) manifestValues.put(FEATURE_REQUIRED+"_"+i, featureRequired);
				i++;
			}
			
			builder = null;
			root = null;
			return manifestValues;
		}
		else{
			builder = null;
			root = null;
			return null;
		}
	}

	public static File createUnpackedWidgetFolder(HttpServletRequest request, Configuration properties, String folder) throws IOException{
		folder = convertIdToFolderName(folder);
		String uploadPath = properties.getString("widget.widgetfolder");
		ServletContext context = request.getSession().getServletContext();
		String serverPath = context.getRealPath(uploadPath + File.separator + folder) ;
		File file = new File(convertPathToPlatform(serverPath));
		return file;
	}

	public static String getURLForWidget(Configuration properties, String folder, String file){
		folder = convertIdToFolderName(folder);
		String path = convertPathToRelativeUri("/wookie" + properties.getString("widget.widgetfolder") + File.separator + folder + File.separator + file);
		return path;
	}

	public static String convertIdToFolderName(String folder){
		if(folder.startsWith("http://")){
			folder = folder.substring(7, folder.length());
		}
		folder.replaceAll(" ", "");
		return folder;
	}

	public static File dealWithUploadFile(HttpServletRequest request, Configuration properties) throws Exception {
		File uFile = null;
		String uploadPath = properties.getString("widget.useruploadfolder");
		ServletContext context = request.getSession().getServletContext();
		String serverPath = convertPathToPlatform(context.getRealPath(uploadPath));
		_logger.debug(serverPath);
		String archiveFileName = null;
			if (FileUploadBase.isMultipartContent(request)) {
				_logger.debug("uploading file...");
				DiskFileUpload fu = new DiskFileUpload();
				// maximum size before a FileUploadException will be thrown
				fu.setSizeMax(1024 * 1024 * 1024);
				// maximum size that will be stored in memory
				fu.setSizeThreshold(1024 * 1024);
				// the location for saving data that is larger than
				// getSizeThreshold()
				fu.setRepositoryPath(uploadPath);

				List<?> fileItems = fu.parseRequest(request);
				if (!fileItems.isEmpty()) {
					Iterator<?> i = fileItems.iterator();
					FileItem fi = (FileItem) i.next();
					File file = new File(convertPathToPlatform(fi.getName()));
					archiveFileName = file.getName();

					uFile = new File(serverPath + File.separator + archiveFileName);

					fi.write(uFile);
					_logger.debug("Upload completed successfully ["
							+ archiveFileName + "]-"
							+ (fi.isInMemory() ? "M" : "D"));
				}
			}
		return uFile;
	}


	public static String convertPathToRelativeUri(String path){
		return path.replace('\\', '/');
	}

	  public static String convertPathToPlatform(String path) {
		String result = path.replace('\\', '/')
				.replace('/', File.separatorChar);
		if (result.endsWith(File.separator)) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	 public static boolean removeWidgetResources(HttpServletRequest request, Configuration properties, String folder){
		folder = convertIdToFolderName(folder);
		String uploadPath = properties.getString("widget.widgetfolder");
		ServletContext context = request.getSession().getServletContext();
		String serverPath = context.getRealPath(uploadPath + File.separator + folder) ;
		File pFolder = new File(convertPathToPlatform(serverPath));
		try {
			_logger.debug("Deleting folder:"+pFolder.getCanonicalFile().toString());
			FileUtils.deleteFolder(pFolder);
		}
		catch (Exception ex) {
			_logger.error(ex);
		}
		return true;
	 }


}
