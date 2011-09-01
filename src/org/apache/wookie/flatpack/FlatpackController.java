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
package org.apache.wookie.flatpack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.controller.Controller;
import org.apache.wookie.controller.WidgetInstancesController;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.WidgetKeyManager;

/**
 * Controller for the Flatpack/exports API
 * 
 * This class provides a controller front end for the FlatpackFactory class, enabling the export of Widget Instances via a HTTP POST request.
 * 
 * POST /flatpack/ {params: api_key, instance_params OR id_key} creates a new W3C Widget package (.wgt) with an opaque file name for the specified widget instance, and returns the download URL. 
 * GET /flatpack/id.wgt download a previously created flatpack
 * 
 * If an invalid API key is supplied, a 401 error code is returned. If no instance can be found, or the parameters supplied are invalid, a 400 error code is returned.
 */
public class FlatpackController extends Controller {

	private static final long serialVersionUID = 2907712805939515004L;
	static Logger _logger = Logger.getLogger(FlatpackController.class.getName());	

	/* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#show(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
	/**
	 * Downloads a previously generated export file.
	 * 
	 * <p><b>Note:</b> currently there is no authentication for this method, which relies solely
	 * on the hard-to-guess exported widget name. </p>
	 * 
	 * <p>For the future we may want to:</p>
	 * 
	 * <ul>
	 * <li> require an access token of some kind before allowing download. </ul>
	 * <li> delete exported widgets after they have been downloaded. </ul>
	 * <li> delete exported widgets at a set period after they have been created. </ul>
	 * </ul>
	 */
  @Override
  protected void show(String resourceId, HttpServletRequest request,
      HttpServletResponse response) throws ResourceNotFoundException,
      UnauthorizedAccessException, IOException {
    
    //
    // If there is no resource part of the requested path, or the request is not for a ".wgt" file, return a 404 immediately
    //
    if (resourceId == null || resourceId.trim().length() == 0 || !resourceId.endsWith(".wgt")) {
      throw new ResourceNotFoundException();
    }
    
    //
    // Get the file path for the requested item
    //
    String requestedPackageFilePath = request.getSession().getServletContext().getRealPath(FlatpackFactory.DEFAULT_FLATPACK_FOLDER+"/"+resourceId);
    
    //
    // Get the widget package corresponding to the path, and throw a 404 if it doesn't exist
    //
    File widgetPackage = new File(requestedPackageFilePath);
    if (!widgetPackage.exists()){
      throw new ResourceNotFoundException();
    }
    
    //
    // Log the download and the IP used
    //
    _logger.info("exported widget package " + resourceId + " downloaded; IP:" + request.getRemoteAddr());
    
    //
    // Set the content-type of the response to application/widget, which
    // is the standard MIME type for widgets.
    //
    final String contentType = "application/widget";
    
    //
    // Set buffer size of response to 10k
    //
    final int bufferSize = 10240;
    
    //
    // Initialize the response
    //
    response.reset();
    response.setBufferSize(bufferSize);
    response.setContentType(contentType);
    response.setHeader("Content-Length", String.valueOf(widgetPackage.length()));
    
    //
    // We can override the browser default behaviour to force it to open a save dialog box;
    // however at least Opera will treat this as a Widget package and do something smarter, and
    // perhaps other browsers may do so in future, so leaving this commented out.
    //
    //response.setHeader("Content-Disposition", "attachment; filename=\"" + widgetPackage.getName() + "\"");

    //
    // Prepare streams
    //
    BufferedInputStream input = null;
    BufferedOutputStream output = null;

    //
    // Send the file
    //
    try {
      
        //
        // Open streams
        //
        input = new BufferedInputStream(new FileInputStream(widgetPackage), bufferSize);
        output = new BufferedOutputStream(response.getOutputStream(), bufferSize);

        //
        // Stream the file to the response
        //
        byte[] buffer = new byte[bufferSize];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        
    } finally {
      
        //
        // Close streams
        //
        output.close();
        input.close();
    }

  }

  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#index(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  /**
   * Deny access to the listing of the flatpack folder
   */
  @Override
  protected void index(HttpServletRequest request, HttpServletResponse response)
      throws UnauthorizedAccessException, IOException {
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
  }



  /**
	 * We override the default POST method from Controller as we need to return the package URL in the Response to the client
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			response.getWriter().write(createFlatpack(request));
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (InvalidParametersException e){
			_logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST); 
		} catch (UnauthorizedAccessException e){
			_logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	/**
	 * Create a flatpack for the request
	 * @param request
	 * @return the URL for the flatpack
	 * @throws UnauthorizedAccessException if there is no valid API key supplied
	 * @throws InvalidParametersException if there is no valid widget instance
	 */
	private String createFlatpack(HttpServletRequest request) throws UnauthorizedAccessException, InvalidParametersException{
		if (!WidgetKeyManager.isValidRequest(request)) throw new UnauthorizedAccessException();
		String path;
		try {
		    //
		    // Construct a FlatpackFactory for the instance identified in the request
		    // If no instance can be found, throw an exception
		    //
			IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
			if (instance == null) throw new InvalidParametersException();
			FlatpackFactory fac = new FlatpackFactory(instance);
			
			//
			// Set the folder to save the flatpack to an appropriate location on this server
			//
			fac.setFlatpackFolder(new File(request.getSession().getServletContext().getRealPath(FlatpackFactory.DEFAULT_FLATPACK_FOLDER.getPath())));
			
			// 
			// Pack the widget instance and get the resulting File
			//
			File flatpack = fac.pack();
			
            //
            // Construct the URL pointing to the exported .wgt file
            //  Note that the resource begins with the servlet path, typically "/wookie"
			//
            String resource = request.getSession().getServletContext().getContextPath() + "/" + FlatpackFactory.DEFAULT_FLATPACK_FOLDER + "/" + flatpack.getName();
            URL url =  getWookieServerURL(request, resource);
      
            //
            // Return the String version of the URL pointing to the exported .wgt file
            //
			path = url.toString();
			
		} catch (Exception e) {
			throw new InvalidParametersException();
		}
		return path;
	}


}
