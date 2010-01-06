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

import org.apache.commons.lang.StringUtils;

/**
 * Utils for content type sniffing
 */
public class ContentTypeUtils {

	private static final String[] SUPPORTED_IMAGE_TYPES = {"image/png", "image/jpeg", "image/svg+xml", "image/gif", "image/vnd.microsoft.icon"};

	/**
	 * Checks to see if a filename is a supported image type based on its file extension
	 * @param filename the filename to check
	 * @return true if the filename has an extension for a supported image type
	 */
	public static boolean isSupportedImageType(String filename){
		String type = getContentType(filename);
		return isSupported(type, SUPPORTED_IMAGE_TYPES);
	}	

	/**
	 * Checks to see if a file is a supported image type
	 * @param file the file to check
	 * @return true if the file is a supported image type
	 */
	public static boolean isSupportedImageType(File file){
		String type = getContentType(file);
		return isSupported(type, SUPPORTED_IMAGE_TYPES);
	}	

	/**
	 * Gets the content type of a file
	 * TODO actually implement SNIFF algorithm
	 * @param file
	 * @return the matched content type, or null if there is no match
	 */
	private static String getContentType(File file){
		String type = getContentType(file.getName());
		if (type == null){ 
			//TODO implement the SNIFF spec for binary content-type checking
		}
		return type;
	}

	/**
	 * Extracts the file extension from the given filename and looks up the
	 * content type
	 * @param filename
	 * @return the matched content type, or null if there is no match
	 */
	private static String getContentType(String filename){

		if (filename == null) return null;
		if (filename.length() == 0) return null;
		if (filename.endsWith(".")) return null;
		if (filename.startsWith(".") && filename.lastIndexOf(".")==0) return null;
		if (filename.contains(".")){
			String type = null;
			String[] parts = filename.split("\\.");
			if (parts.length == 0) return null;
			String ext = parts[parts.length-1];
			if (ext.length() != 0){
				if (StringUtils.isAlpha(ext)){
					type = getContentTypeForExtension(ext);
					if (type!=null) return type;
				}
			}
		}
		return null;
	}

	/**
	 * @param ext
	 * @return the content-type for the given file extension, or null if there is no match
	 */
	private static String getContentTypeForExtension(String ext){
		if(ext.equals("html")) return "text/html";
		if(ext.equals("htm")) return "text/html";
		if(ext.equals("css")) return "text/css";
		if(ext.equals("js")) return "application/javascript";
		if(ext.equals("xml")) return "application/xml";
		if(ext.equals("txt")) return "text/plain";		
		if(ext.equals("wav")) return "audio/x-wav";
		if(ext.equals("xhtml")) return "application/xhtml+xml";
		if(ext.equals("xht")) return "application/xhtml+xml";
		if(ext.equals("gif")) return "image/gif";
		if(ext.equals("png")) return "image/png";
		if(ext.equals("ico")) return "image/vnd.microsoft.icon";
		if(ext.equals("svg")) return "image/svg+xml";
		if(ext.equals("jpg")) return "image/jpeg";
		return null;

	}

	/**
	 * Checks to see if the supplied value is one of the supported values
	 * @param value
	 * @param supportedValues
	 * @return true if the value is one of the supported values
	 */
	public static boolean isSupported(String value, String[] supportedValues){
		if (value == null) return false;
		boolean supported = false;
		for (String type: supportedValues){
			if (StringUtils.equals(value, type)) supported = true;
		}
		return supported;
	}

}
