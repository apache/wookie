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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.w3c.util.WidgetPackageUtils;

/**
 * Utility for working with Widget files in the Wookie server: uploading,
 * moving, and deleting
 */
public class WidgetFileUtils {

  static Logger _logger = Logger.getLogger(WidgetFileUtils.class.getName());

  /**
   * Upload a widget archive
   * 
   * @param uploadPath
   *          the path to upload files to
   * @param request
   *          the servlet request
   * @return the widget file that was uploaded
   * @throws Exception
   *           if the file could not be uploaded
   */
  public static File upload(String uploadPath, HttpServletRequest request)
      throws Exception {
    String serverPath = WidgetPackageUtils.convertPathToPlatform(uploadPath);

    //
    // Create a factory for disk-based file items
    //
    DiskFileItemFactory factory = new DiskFileItemFactory();

    //
    // Create a new file upload handler
    //
    ServletFileUpload upload = new ServletFileUpload(factory);

    //
    // maximum size before a FileUploadException will be thrown
    //
    upload.setSizeMax(1024 * 1024 * 1024);

    //
    // process upload request
    //
    @SuppressWarnings("unchecked")
    List<FileItem> fileItems = upload.parseRequest(request);

    _logger.debug(serverPath);

    //
    // Only save .wgt files and ignore any others in the POST
    //
    if (!fileItems.isEmpty()) {
      for (FileItem item : fileItems) {
        if (!item.isFormField() && item.getName() != null
            && item.getName().endsWith(".wgt")) {
          return write(item, serverPath);
        }
      }
    }

    return null;
  }

  /**
   * Write a FileItem to a file prefixed with the given path
   * 
   * @param item
   * @param serverPath
   * @return
   * @throws Exception
   */
  private static File write(FileItem item, String path) throws Exception {
    File file = new File(WidgetPackageUtils.convertPathToPlatform(item
        .getName()));
    
    File uFile = getTargetLocation(file, path);
    item.write(uFile);
    
    _logger.debug("Upload completed successfully" + "[" //$NON-NLS-1$ //$NON-NLS-2$
        + file.getName() + "]-" //$NON-NLS-1$
        + (item.isInMemory() ? "M" : "D")); //$NON-NLS-1$ //$NON-NLS-2$

    return uFile;
  }
  
	/**
	 * Identify the target location for an uploaded or dropped file
	 * 
	 * @param file
	 *            the uploaded or dropped file
	 * @param path
	 *            the path to the directory where the file should be created
	 * @return a File object representing the place where a file should be
	 *         written
	 */
	private static File getTargetLocation(File file, String path) {

		//
		// We'll use the current system time to prefix files that
		// overwrite existing files
		//
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SZ");
		
		//
		// Create candidate target
		//
		File uFile = new File(path + File.separator + file.getName());

		//
		// Don't overwrite other uploads, instead use a prefix; this
		// starts out as the current system time, then we attach a
		// count if we have multiple uploads during the same milli;
		// although this is extremely unlikely to ever happen.
		//
		int count = 0;
		String prefixAppend = "";
		while (uFile.exists()) {
			String prefix = df.format(new Date()) + prefixAppend;
			file = new File(WidgetPackageUtils.convertPathToPlatform(prefix
					+ "-" + file.getName()));
			uFile = new File(path + File.separator + file.getName());
			count ++;
			prefixAppend = "-"+String.valueOf(count)+"-";
		}
		
		return uFile;
	}

	/**
	 * Moves a file to the specified path
	 * 
	 * @param uploadPath
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static File dealWithDroppedFile(String uploadPath, File file)
			throws IOException {
		//
		// Identify the location to move the file to
		//
		File uFile = getTargetLocation(file, uploadPath);

		//
		// Copy the file over to its new location
		//
		FileUtils.copyFile(file, uFile);

		//
		// Delete the file from the location it was originally dropped into
		//
		file.delete();

		//
		// Return the copied file
		//
		return uFile;
	}

  /**
   * Delete a widget and its resources
   * 
   * @param WIDGETFOLDER
   * @param widgetGuid
   * @return
   */
  public static void removeWidgetResources(String WIDGETFOLDER,
      String widgetGuid) {
    String folder = WidgetPackageUtils.convertIdToFolderName(widgetGuid);
    String serverPath = WIDGETFOLDER + File.separator + folder;
    File pFolder = new File(
        WidgetPackageUtils.convertPathToPlatform(serverPath));
    try {
      _logger.debug("Deleting folder:" + pFolder.getCanonicalFile().toString()); //$NON-NLS-1$
      if (pFolder.getParent() != null) // never call on a root folder
        FileUtils.deleteDirectory(pFolder);
    } catch (Exception ex) {
      _logger.error(ex);
    }
  }

}
