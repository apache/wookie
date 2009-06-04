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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.exceptions.BadManifestException;
import org.tencompetence.widgetservice.manifestmodel.IManifestModel;
import org.tencompetence.widgetservice.manifestmodel.IW3CXMLConfiguration;
import org.tencompetence.widgetservice.manifestmodel.impl.WidgetManifestModel;

/**
 * Manifest Helper class - methods for uploading the zip & parsing a w3c widget manifest.
 * 
 * @author Paul Sharples
 * @version $Id: ManifestHelper.java,v 1.13 2009-06-04 15:07:02 ps3com Exp $ 
 *
 */
public class ManifestHelper implements IW3CXMLConfiguration {
	
	static Logger _logger = Logger.getLogger(ManifestHelper.class.getName());
	
	public static IManifestModel dealWithManifest(String xmlText, Messages localizedMessages) throws JDOMException, IOException, BadManifestException {
		SAXBuilder builder = new SAXBuilder();
		Element root = builder.build(new StringReader(xmlText)).getRootElement();				
		IManifestModel manifestModel = new WidgetManifestModel();
		manifestModel.fromJDOM(root);
		return manifestModel;		
	}

	public static File createUnpackedWidgetFolder(HttpServletRequest request, Configuration properties, String folder) throws IOException{
		folder = convertIdToFolderName(folder);
		String uploadPath = properties.getString("widget.widgetfolder"); //$NON-NLS-1$
		ServletContext context = request.getSession().getServletContext();
		String serverPath = context.getRealPath(uploadPath + File.separator + folder) ;
		File file = new File(convertPathToPlatform(serverPath));
		return file;
	}

	public static String getURLForWidget(Configuration properties, String folder, String file){
		folder = convertIdToFolderName(folder);
		String path = convertPathToRelativeUri("/wookie" + properties.getString("widget.widgetfolder") + File.separator + folder + File.separator + file); //$NON-NLS-1$ //$NON-NLS-2$
		return path;
	}

	public static String convertIdToFolderName(String folder){
		if(folder.startsWith("http://")){ //$NON-NLS-1$
			folder = folder.substring(7, folder.length());
		}
		folder.replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
		return folder;
	}

	public static File dealWithUploadFile(HttpServletRequest request, Configuration properties) throws Exception {
		File uFile = null;
		String uploadPath = properties.getString("widget.useruploadfolder"); //$NON-NLS-1$
		ServletContext context = request.getSession().getServletContext();
		String serverPath = convertPathToPlatform(context.getRealPath(uploadPath));
		_logger.debug(serverPath);
		String archiveFileName = null;
			if (FileUploadBase.isMultipartContent(request)) {
				_logger.debug("uploading file..."); //$NON-NLS-1$
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
					_logger.debug("Upload completed successfully" +  "[" //$NON-NLS-1$ //$NON-NLS-2$
							+ archiveFileName + "]-" //$NON-NLS-1$
							+ (fi.isInMemory() ? "M" : "D")); //$NON-NLS-1$ //$NON-NLS-2$
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
		String uploadPath = properties.getString("widget.widgetfolder"); //$NON-NLS-1$
		ServletContext context = request.getSession().getServletContext();
		String serverPath = context.getRealPath(uploadPath + File.separator + folder) ;
		File pFolder = new File(convertPathToPlatform(serverPath));
		try {
			_logger.debug("Deleting folder:"+pFolder.getCanonicalFile().toString()); //$NON-NLS-1$
			FileUtils.deleteFolder(pFolder);
		}
		catch (Exception ex) {
			_logger.error(ex);
		}
		return true;
	 }


}
