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
import org.apache.wookie.Messages;
import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.manifestmodel.IManifestModel;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.manifestmodel.impl.WidgetManifestModel;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Manifest Helper class - methods for uploading the zip & parsing a w3c widget manifest.
 * 
 * @author Paul Sharples
 * @version $Id: ManifestHelper.java,v 1.2 2009-07-28 16:05:21 scottwilson Exp $ 
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
