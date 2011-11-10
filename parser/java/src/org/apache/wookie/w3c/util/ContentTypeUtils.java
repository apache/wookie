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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
		if (file == null) return false;
		String type = getContentType(file);
		return isSupported(type, SUPPORTED_IMAGE_TYPES);
	}	
	
	/**
	 * Checks to see if an inputstream contains a supported image type
	 * @param inputStream
	 * @return true if the file is a supported image type 
	 * @throws IOException
	 */
	public static boolean isSupportedImageType(InputStream inputStream) throws IOException{
	  String type = sniffContentType(inputStream);
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
			try {
				type = sniffContentType(file);
			} catch (IOException e) {
				type = null;
			}
		}
		return type;
	}

	/**
	 * Sniffs the content type for images and other common types
	 * @param file the file to sniff
	 * @return the content type of the file if it matches a known signature, otherwise Null
	 * @throws IOException 
	 */
	protected static String sniffContentType(File file) throws IOException{
		FileInputStream stream = new FileInputStream(file);
		return sniffContentType(stream);
	}
	
	 /**
   * Sniffs the content type for images and other common types
   * @param inpuStream the inputStream to sniff
   * @return the content type of the stream if it matches a known signature, otherwise Null
   * @throws IOException 
   */
	protected static String sniffContentType(InputStream inputStream) throws IOException{
	  if (inputStream == null) return null;
	  byte[] bytes = new byte[8];
    inputStream.read(bytes);
    String[] hex = new String[8];
    String hexString = "";  
    for (int i=0;i<8;i++){
      hex[i]= getHexValue(bytes[i]);
      hexString += hex[i]+" ";
    } 
    String prefix = new String(bytes);
    if (prefix.startsWith("GIF87") || prefix.startsWith("GIF89")) return "image/gif";
    if (hex[0].equals("ff") && hex[1].equals("d8")) return "image/jpeg";
    if (hex[0].equals("42") && hex[1].equals("4d")) return "image/bmp";
    if (hex[0].equals("00") && hex[1].equals("00") && hex[2].equals("01") && hex[3].equals("00")) return "image/vnd.microsoft.icon";
    if (hexString.trim().equals("89 50 4e 47 0d 0a 1a 0a")) return "image/png"; 
    return null;
	}
	
	/**
	 * Get a normalized two-character hex value for a byte 
	 * @param b
	 * @return a two-character hex string
	 */
	private static String getHexValue(byte b){
		String hex;
		hex =Integer.toHexString(0x00 | b);
		if (hex.length()==1) hex = "0" + hex;
		if (hex.length()>2) hex =hex.substring(hex.length()-2);
		return hex;
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
