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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.apache.wookie.w3c.IW3CXMLConfiguration;

/**
 * Utilities for working with Widget packages, i.e. Zip Files with an XML manifest
 * @author scott
 *
 */
public class WidgetPackageUtils {
	static Logger _logger = Logger.getLogger(WidgetPackageUtils.class.getName());

	
	/**
	 * Implements the rule for finding a file within a widget; it returns the first localized version 
	 * first before returning a root version. An exception is thrown if the path points to a folder, and
	 * null is returned if no file is found
	 * @param path
	 * @param locales the supported locales, in list of preference
	 * @param zip
	 * @return the path to the file as a String
	 * @throws Exception
	 */
	public static String locateFilePath(String path, String[] locales, ZipFile zip) throws Exception{
		String[] paths = locateFilePaths(path, locales, zip);
		if (paths != null && paths.length != 0) return paths[0];
		return null;
	}
	
	/**
	 * Returns the set of valid file paths for a given resource. All valid paths are returned, starting
	 * with localized versions for supported locales before the root version (if present).
	 * @param path
	 * @param locales
	 * @param zip
	 * @return an array of paths for the resource
	 * @throws Exception
	 */
	public static String[] locateFilePaths(String path, String[] locales, ZipFile zip) throws Exception{
		ArrayList<String> paths = new ArrayList<String>();
		if (path.startsWith("/")) path = path.substring(1, path.length());
		String[] pathComponents = path.split("/");
		if ("locales".equalsIgnoreCase(pathComponents[0])){
			if (pathComponents.length < 2) return null;
			if (!LocalizationUtils.isValidLanguageTag(pathComponents[1])) return null;
		}
		// Look in localized folders first
		for (String locale:locales){
			String localePath = "locales/"+locale.trim()+"/"+path;
			if (zip.getEntry(localePath) != null){
				if (zip.getEntry(localePath).isDirectory()) throw new Exception();
				paths.add(localePath);
			}
		}
		// Look in root folder
		if (zip.getEntry(path) != null && !zip.getEntry(path).isDirectory()) paths.add(path);
		return (String[]) paths.toArray(new String[paths.size()]);
	}
	
	/**
	 * Return the language tag for a given path
	 * @param path
	 * @return the language tag for the path
	 */
	public static String languageTagForPath(String path){
		if (path == null) return null;
		String locale = null;
		String[] pathComponents = path.split("/");
		if ("locales".equalsIgnoreCase(pathComponents[0])){
			if (pathComponents.length < 2) return null;
			return pathComponents[1];
		}
		return locale;
	}
	
    /*
     * Utility method for creating a temp directory
     * @return a new temp directory
     * @throws IOException
     */
	public static File createTempDirectory() throws IOException {
		final File temp;

		temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}

		return (temp);
	}

	
	/**
	 * Given a zipfile, iterate over the contents of the "locales" folder within the
	 * archive, should it exist, and return the names of all the subdirectories, 
	 * assumed to be localized content folders.
	 * 
	 * Note that this method does not validate the directory names as being valid
	 * language tags.
	 * @param zipFile
	 * @return an array of locale names
	 * @throws IOException
	 */
	public static String[] getLocalesFromZipFile(ZipFile zipFile) throws IOException{
		File temp = createTempDirectory();
		unpackZip(zipFile, temp);
		return getLocalesFromPackage(temp);
	}
	
	
	/**
	 * Give a directory, assumed to be the root of a widget package, iterate over
	 * the contents of the "locales" folder, should it exist, and return the names
	 * of all the subdirectories, assumed to be localized content folders.
	 * 
	 * Note that this method does not validate the directory names as being valid
	 * language tags.
	 * @param file
	 * @return an array of locale names
	 */
	public static String[] getLocalesFromPackage(File file){
		ArrayList<String> locales = new ArrayList<String>();
		
		if (file.exists()){
			File localesFolder = new File(file + File.separator + "locales");
			if (localesFolder.exists() && localesFolder.isDirectory()){
				for (String localeName : localesFolder.list()){
					File localeFolder = new File(localesFolder + File.separator + localeName);
					if (localeFolder.isDirectory()){
						locales.add(localeName);
					}
				}
			}
		}
		
		return (String[])locales.toArray(new String[locales.size()]);
	}
	
	/**
	 * Return the set of valid default files for each locale in the zip
	 * @param zip
	 * @param locales
	 * @param defaults
	 * @return an array of file paths as Strings
	 */
	public static String[] getDefaults(ZipFile zip, String[] locales, String[] defaults){
		ArrayList<String> content = new ArrayList<String>();
		for (String start: defaults){
			try {
				String[] paths = locateFilePaths(start, locales, zip);
				if (paths != null){
					for (String path:paths) content.add(path);
				}
			} catch (Exception e) {
				// ignore and move onto next
			}
		}
		return (String[]) content.toArray(new String[content.size()]);	
	}
	
	/**
	 * Returns a File representing the unpacked folder for a widget with the given identifier.
	 * @param widgetFolder the folder that contains unpacked widgets
	 * @param id the widget identifier URI
	 * @return a File where the widget may be unpacked
	 * @throws IOException
	 */
	public static File createUnpackedWidgetFolder(File widgetFolder, String id) throws IOException{
		String folder = convertIdToFolderName(id);
		String serverPath = widgetFolder.getPath() + File.separator + folder;
		File file = new File(convertPathToPlatform(serverPath));
		return file;
	}

	/**
	 * Returns the local URL for a widget 
	 * @param widgetFolder the folder that contains unpacked widgets
	 * @param id the widget identifier URI
	 * @param file a file in the widget package; use "" to obtain the root of the widget folder
	 * @return the URL for the widget as a String
	 */
	public static String getURLForWidget(String widgetFolder, String id, String file){
		String folder = convertIdToFolderName(id);
		String path = convertPathToRelativeUri(widgetFolder + File.separator + folder + File.separator + file); //$NON-NLS-1$ //$NON-NLS-2$
		return path;
	}
	
	/**
	 * Converts a widget identifier (usually a URI) into a form suitable for use as a file system folder name
	 * @param id the widget identifier URI
	 * @return the folder name to use for the widget
	 */
	public static String convertIdToFolderName(String id){
		if(id.startsWith("http://")){ //$NON-NLS-1$
			 id = id.substring(7, id.length());
		}
		//id = id.replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
		id = id.replaceAll("[ \\:\"*?<>|`\n\r\t\0]+", "");
		return id;
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
		return IOUtils.toString(zipFile.getInputStream(entry), "UTF-8");
	}

	/**
	 * Gets the input stream for an entry in the zip 
	 * @param entry the name of the entry
	 * @param zipFile
	 * @return the input stream
	 * @throws ZipException
	 * @throws IOException
	 */
	public static InputStream getEntryStream(String entry, ZipFile zipFile) throws ZipException, IOException{
	  ZipArchiveEntry zipEntry;
	  zipEntry = zipFile.getEntry(entry);
	  if (zipEntry == null) return null;
	  return zipFile.getInputStream(zipEntry);
	}
	
	/**
	 * uses apache commons compress to unpack all the zip entries into a target folder
	 * partly adapted from the examples on the apache commons compress site, and an
	 * example of generic Zip unpacking. Note this iterates over the ZipArchiveEntry enumeration rather
	 * than use the more typical ZipInputStream parsing model, as according to the doco it will
	 * more reliably read the entries correctly. More info here: http://commons.apache.org/compress/zip.html
	 * @param zipfile the Zip File to unpack
	 * @param targetFolder the folder into which to unpack the Zip file
	 * @throws IOException
	 */
	public static void unpackZip(ZipFile zipfile, File targetFolder) throws IOException {
	  //
	  // Delete directory and contents if it already exists. See WOOKIE-239
	  //
	  if (targetFolder.exists() && targetFolder.isDirectory()) FileUtils.deleteDirectory(targetFolder);
	  
		targetFolder.mkdirs();
		BufferedOutputStream out = null;
		InputStream in = null;
		ZipArchiveEntry zipEntry;

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
					in = new BufferedInputStream(zipfile.getInputStream(zipEntry));
					out = new BufferedOutputStream(new FileOutputStream(outFile));
					IOUtils.copy(in, out);
					// Restore time stamp
					outFile.setLastModified(zipEntry.getTime());
					
					// Close File
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
	 * Packages the source file/folder up as a new Zip file
	 * @param source the source file or folder to be zipped
	 * @param target the zip file to create
	 * @throws IOException
	 */
	public static void repackZip(File source, File target) throws IOException{
		ZipArchiveOutputStream out = new ZipArchiveOutputStream(target);
		out.setEncoding("UTF-8");
        for(File afile: source.listFiles()){
            pack(afile,out, "");
        }
		out.flush();
		out.close();
	}
	
	/**
	 * Recursively locates and adds files and folders to a zip archive
	 * @param file
	 * @param out
	 * @param path
	 * @throws IOException
	 */
	private static void pack(File file, ZipArchiveOutputStream out, String path) throws IOException {
        if(file.isDirectory()){
            path = path + file.getName() +"/";
            for(File afile: file.listFiles()){
                pack(afile,out, path);
            }
        } else {
        	ZipArchiveEntry entry = (ZipArchiveEntry) out.createArchiveEntry(file, path + file.getName());
    		out.putArchiveEntry(entry);
            byte[] buf = new byte[1024];
            int len;
            FileInputStream in = new FileInputStream(file);
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
              }
    		out.closeArchiveEntry();
        }
    }
}
