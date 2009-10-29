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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.exceptions.BadWidgetZipFileException;
import org.apache.wookie.manifestmodel.IManifestModel;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;

/**
 * Utilities for working with Widget packages, i.e. Zip Files with an XML manifest
 * @author scott
 *
 */
public class WidgetPackageUtils {
	static Logger _logger = Logger.getLogger(WidgetPackageUtils.class.getName());
	
	public static final String[] START_FILES = {"index.htm","index.html","index.svg","index.xhtml","index.xht"};
	
	/**
	 * Identify the start file for a given zipfile and manifest, or throw an exception
	 * @param widgetModel
	 * @param zip
	 * @param localizedMessages
	 * @return the name of the start file
	 * @throws BadWidgetZipFileException if a custom start file is specified, but is not present
	 * @throws BadManifestException if no custom start file is found, and no default start file can be located
	 */
	public static String locateStartFile(IManifestModel widgetModel, ZipFile zip, Messages localizedMessages) throws BadWidgetZipFileException, BadManifestException{
		String startFile = null;
		// Check for a custom start file
		if (widgetModel.getContent() != null) {
			if (widgetModel.getContent().getSrc() != null){
				startFile = widgetModel.getContent().getSrc();
				// Check that the specified custom start file exists
				if (zip.getEntry(startFile)==null){
					startFile = null;
					throw new BadWidgetZipFileException(localizedMessages.getString("WidgetAdminServlet.22")); //$NON-NLS-1$
				}
			}
		}
		
		// If no custom start file exists, look for defaults
		for (String s: START_FILES){
			if (startFile == null && zip.getEntry(s)!=null){
				startFile = s;
			}
		}
		// If no start file has been found, throw an exception
		if (startFile == null) 
			throw new BadManifestException("WidgetAdminServlet.27"); //$NON-NLS-1$
		return startFile;
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
			deleteDirectory(pFolder);
		}
		catch (Exception ex) {
			_logger.error(ex);
		}
		return true;
	}

	/**
	 * Recursive delete of a folder
	 * Based on a how-to article by RŽal Gagnon at http://www.rgagnon.com/javadetails/java-0483.html
	 * @param path the directory to delete
	 * @return true if the directory was deleted
	 * @throws IOException
	 */
	private static boolean deleteDirectory(File path) throws IOException{
		if( path.exists() ) {
			// This should never be called with a root folder
			assert path.getParentFile()!=null;
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		return( path.delete() );
	}
	
	/**
	 * Checks for the existence of the Manifest.
	 * TODO not sure if this properly handles case-sensitive entries?
	 * @param zipfile
	 * @return true if the zip file has a manifest
	 */
	public static boolean hasManifest(ZipFile zipfile){
		return zipfile.getEntry(IW3CXMLConfiguration.MANIFEST_FILE)!=null;
	}

	/**
	 * Retrieves the Manifest entry as a String
	 * @param zipFile the zip file from which to extract the manifest
	 * @return a String representing the manifest contents
	 * @throws IOException
	 */
	public static String extractManifest(ZipFile zipFile) throws IOException{
		ZipArchiveEntry entry = zipFile.getEntry(IW3CXMLConfiguration.MANIFEST_FILE);
		InputStream content = zipFile.getInputStream(entry);
		return convertStreamToString(content);
	}

	/**
	 * uses apache commons compress to unpack all the zipe entries into a target folder
	 * partly adapted from the examples on the apache commons compress site, and an
	 * example of generic Zip unpacking. Note this iterates over the ZipArchiveEntry enumeration rather
	 * than use the more typical ZipInputStream parsing model, as according to the doco it will
	 * more reliably read the entries correctly. More info here: http://commons.apache.org/compress/zip.html
	 * @param zipfile the Zip File to unpack
	 * @param targetFolder the folder into which to unpack the Zip file
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void unpackZip(ZipFile zipfile, File targetFolder) throws IOException {
		targetFolder.mkdirs();
		BufferedOutputStream out = null;
		InputStream in = null;
		ZipArchiveEntry zipEntry;
		int bytesRead;
		final int bufSize = 512;
		byte buf[] = new byte[bufSize];

		Enumeration entries = zipfile.getEntries();
		try {
			while (entries.hasMoreElements()){
				zipEntry = (ZipArchiveEntry)entries.nextElement();
				// Don't add directories - use mkdirs instead
				if(!zipEntry.isDirectory()) {
					File outFile = new File(targetFolder, zipEntry.getName());

					// Ensure that the parent Folder exists
					if(!outFile.getParentFile().exists()) {
						outFile.getParentFile().mkdirs();
					}
					// Read the entry
					in = zipfile.getInputStream(zipEntry);
					out = new BufferedOutputStream(new FileOutputStream(outFile), bufSize);
					while((bytesRead = in.read(buf)) != -1) {
						out.write(buf, 0, bytesRead);
					}
					// Restore time stamp
					outFile.setLastModified(zipEntry.getTime());
					// Close File
					out.flush();
					out.close();
					// Close Stream
					in.close();
				}
			}

		}
		// We'll catch this exception to close the file otherwise it remains locked
		catch(IOException ex) {
			if (in != null){
				in.close();
			}
			if(out != null) {
				out.flush();
				out.close();
			}
			// And throw it again
			throw ex;
		}
	}

	/**
	 * Taken from an example at http://www.kodejava.org/examples/266.html
	 * @param is the InputStream to convert
	 * @return a String representing the content of the Stream
	 */
	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
}
