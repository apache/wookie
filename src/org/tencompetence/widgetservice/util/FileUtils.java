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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Vector;

/**
 * Some useful File Utilities
 *
 * @author Phillip Beauvoir
 * @author Paul Sharples
 * @version $Id: FileUtils.java,v 1.3 2007-12-13 20:31:33 ps3com Exp $
 */
public final class FileUtils  {
	
	private FileUtils() {}
	
	/**
	 * Get the extension portion of a filename.
	 * @param file The File in question
	 * @return The extension part of the filename excluding the "." or "" if no extension
	 */
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if(i > 0 && i < fileName.length() - 1) {
			return fileName.substring(i + 1).toLowerCase();
		}
		return "";
	}
	
	/**
	 * Get the short name portion of a filename not including the extension.
	 * @param file The File in question
	 * @return The name part of a file name excluding the extension
	 */
	public static String getFileNameWithoutExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if(i > 0 && i < fileName.length() - 1) {
		    return fileName.substring(0, i);
		}
		else {
		    return fileName;
		}
	}
	
	
	/**
	 * Copy a Folder and all its files and sub-folder to target Folder which will be created if not there.
	 * @param srcFolder The Source Folder
	 * @param destFolder The Destination Folder
	 * @param progressMonitor An optional DweezilProgressMonitor.  Can be null.
	 * @throws IOException On error or if there is a DweezilProgressMonitor and user pressed Cancel
	 */
	public static void copyFolder(File srcFolder, File destFolder) throws IOException {
	    if(srcFolder.equals(destFolder)) {           
	        throw new IOException("Source and target folders cannot be the same.");
	    }
	    
	    // Check that destFolder is not a child of srcFolder
	    for(File dest = destFolder.getParentFile(); dest != null; dest = dest.getParentFile()) {
	        if(dest.equals(srcFolder)) {	            
	            throw new IOException("The destination folder cannot be a subfolder of the source folder.");
	        }
	    }
	        
	    destFolder.mkdirs();
	    File[] srcFiles = srcFolder.listFiles();
	    for(int i = 0; i < srcFiles.length; i++) {
	        File srcFile = srcFiles[i];
	        // If we have a Progress Monitor...
	        
	        if(srcFile.isDirectory()) {
	            copyFolder(srcFile, new File(destFolder, srcFile.getName()));
	        }
	        else {
	            copyFile(srcFile, new File(destFolder, srcFile.getName()));
	        }
	    }
	}
	
	
	/**
	 * Copy a File.  The Source file must exist.
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
	    if(srcFile.equals(destFile)) {
	        throw new IOException("Source and Target Files cannot be the same");
	    }
	    
	    int bufSize = 1024;
	    byte[] buf = new byte[bufSize];
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile), bufSize);
	    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile), bufSize);
	    int size;
	    while((size = bis.read(buf)) != -1) bos.write(buf, 0, size);
	    bos.flush();
	    bos.close();
	    bis.close();
	}
	
	/**
	 * Move a File
	 */
	public static void moveFile(File srcFile, File destFile) throws IOException {
		copyFile(srcFile, destFile);
		srcFile.delete();
	}
	
	/**
	 * Delete a folder and its contents
	 * @param afolder -  a folder
	 */
	public static void deleteFolder(File afolder) throws IOException {
	    if(afolder == null) {
	        return;
	    }
	    
	    // Not root directories
	    // This way does not work where afolder = new File("aFolder");
	    // File parent = afolder.getParentFile();
	    File parent = new File(afolder.getAbsolutePath()).getParentFile();
	    if(parent == null) {
	        throw new IOException("Cannot delete root folder");
	    }
	    
	    if(afolder.exists() && afolder.isDirectory()) {
	        //delete content of directory:
	        File[] files = afolder.listFiles();
	        int count = files.length;
	        for(int i = 0; i < count; i++) {
	            File f = files[i];
	            if(f.isFile()) {
	                f.delete();
	            }
	            else if(f.isDirectory()) {
	                deleteFolder(f);
	            }
	        }
	        afolder.delete();
	    }
	}
	
    /**
     * Test if can write to file
     * 
     * @param file A file to write
     * @return true if the medium is writeable
     */
    public static boolean canWriteFile(File file) {
        try {
            file.createNewFile();
            file.delete();
        }
        catch(IOException e) {
            return false;
        }
        return true;
    }

    /**
	 * Sort a list of files into Folders first, files second
	 * @param files
	 * @return
	 */
	public static File[] sortFiles(File[] files) {
	    if(files == null || files.length == 0) {
	        return files;
	    }
	    
		Vector<File> v = new Vector<File>();
		
		// Folders
		for(int i = 0; i < files.length; i++) {
			File file = files[i];
			if(file.isDirectory()) {
				v.add(file);
			}
		}
		
		// Files
		for(int i = 0; i < files.length; i++) {
			File file = files[i];
			if(!file.isDirectory()){
				v.add(file);
			}
		}
		
		File[] f = new File[v.size()];
		v.copyInto(f);
		v = null;
		return f;
	}
	
	/**
	 * Get a relative path for a file given its relationship to rootFolder
	 */
	public static String getRelativePath(File rootFolder, File file) {
        // Important - URI.relativize() is case sensitive
        // So relativing d:\myfolder and D:\myfolder\afile.text will fail
        // So we use the Canonical path
		try {
            rootFolder = rootFolder.getCanonicalFile();
    	    file = file.getCanonicalFile();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
		
		URI uriRoot = rootFolder.toURI();
		URI uriFile = file.toURI();
		
		URI result = uriRoot.relativize(uriFile);
		if(result == null) {
		    return file.getName();
		}
		
		String str = result.getPath();
		return str == null ? file.getName() : str;
	}
	
	/**
	 * @return The path to the jar that is currently executing.
	 */	
	public static String getJarFilePath (Class aClass) {
		// ugly hack to get name of jarfile
		//Class myClass = aClass.getClass();
		String myClassName = aClass.getName().replace('.','/') + ".class";
		ClassLoader myClassLoader = aClass.getClassLoader();
		URL urlJar = myClassLoader.getResource(myClassName);
		String urlStr = urlJar.toString();
		if (urlStr.indexOf("jar:file:") == -1) {
			System.out.println("Not a jar");
			return null;
		}
		int from = "jar:file:".length();
		int to = urlStr.indexOf("!/");
		return urlStr.substring(from, to);
	}
	
}

