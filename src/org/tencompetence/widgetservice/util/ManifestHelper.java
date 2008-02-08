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
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class ManifestHelper implements IW3CXMLConfiguration {

	static Logger _logger = Logger.getLogger(ManifestHelper.class.getName());

	public static String[] dealWithManifest(String xmlText) throws JDOMException, IOException {
		String title, description, author, iconPath;
		SAXBuilder builder = new SAXBuilder();
		Element root = builder.build(new StringReader(xmlText)).getRootElement();
		if(root.getName().equalsIgnoreCase(WIDGET_ELEMENT)){
			String id = root.getAttributeValue(ID_ATTRIBUTE);
			String start = root.getAttributeValue(START_ATTRIBUTE);
			String height = root.getAttributeValue(HEIGHT_ATTRIBUTE);
			String width = root.getAttributeValue(WIDTH_ATTRIBUTE);
			
			Element titleElement = root.getChild(TITLE_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));
			if(titleElement==null){
				title = "Unnamed Widget";
			}
			else{
				title = titleElement.getText();
			}
			
			Element descElement = root.getChild(DESCRIPTION_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));									
			if(descElement==null){
				description = "No Description";
			}
			else{
				description = descElement.getText();
			}
			
			Element authorElement = root.getChild(AUTHOR_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));									
			if(authorElement==null){
				author = "Anonymous";
			}
			else{
				author = authorElement.getText();
			}
			
			Element iconElement = root.getChild(ICON_ELEMENT, Namespace.getNamespace(MANIFEST_NAMESPACE));									
			if(iconElement==null){
				iconPath = "";
			}
			else{
				iconPath = iconElement.getText();
			}
			
			builder = null;
			root = null;
			return new String[]{id, start, height, width, title, description, author, iconPath};
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
