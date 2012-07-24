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
package org.apache.wookie.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.wookie.Messages;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ServiceUnavailableException;
import org.apache.wookie.feature.Features;
import org.apache.wookie.helpers.WidgetImportHelper;
import org.apache.wookie.server.LocaleHandler;
import org.apache.wookie.util.WidgetFileUtils;
import org.apache.wookie.util.WidgetJavascriptSyntaxAnalyzer;
import org.apache.wookie.util.html.StartPageProcessor;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;
import org.apache.wookie.w3c.exceptions.InvalidStartFileException;

public class ValidatorController  extends Controller {

    private static final long serialVersionUID = -3969001338861831480L;

    /* (non-Javadoc)
     * @see org.apache.wookie.controller.Controller#create(java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected boolean create(String resourceId, HttpServletRequest request, HttpServletResponse response)
        throws ResourceDuplicationException, InvalidParametersException, ServiceUnavailableException{
        
        Configuration properties = (Configuration) getServletContext().getAttribute("properties"); //$NON-NLS-1$
        // check validation is enabled
        boolean shouldValidate = properties.getBoolean("widget.enable.validator");
        if(!shouldValidate){
            throw new ServiceUnavailableException();
        }
        //
        // Get localized messages so we can return errors
        //
        Messages localizedMessages = LocaleHandler.localizeMessages(request);
        String tempUploadFolder = System.getProperty("java.io.tmpdir");
        //
        // Try to obtain a zipfile from the request. 
        //
        File zipFile;
        try {
          zipFile = WidgetFileUtils.upload(tempUploadFolder, request);
        } catch (Exception ex) {
          throw new InvalidParametersException(localizedMessages.getString("widgets.invalid-config-xml") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ 
        }
        
        //
        // No file uploaded
        //
        if(zipFile == null || !zipFile.exists()){
          throw new InvalidParametersException(localizedMessages.getString("widgets.no-widget-file-uploaded")); //$NON-NLS-1$
        }
        
        W3CWidget widgetModel = null;
        W3CWidgetFactory fac = null;
        try {
            //
            // Parse and validate the zip as a widget
            //
            final String[] locales = properties.getStringArray("widget.locales");
            fac = new W3CWidgetFactory();
            fac.setLocales(locales);
            fac.setLocalPath(tempUploadFolder);
            fac.setOutputDirectory(tempUploadFolder);
            fac.setFeatures(Features.getFeatureNames());
            fac.setStartPageProcessor(new StartPageProcessor());
            widgetModel = fac.parse(zipFile);
            new WidgetJavascriptSyntaxAnalyzer(fac.getUnzippedWidgetDirectory());
            returnXml(WidgetImportHelper.createXMLWidgetDocument(widgetModel, new File(fac.getUnzippedWidgetDirectory(), "config.xml"), getWookieServerURL(request, "").toString(), false), response);
            //send back a 200 ok.
            return false;
            //
            // Catch specific parsing and validation errors and throw exception with error message
            //
        } catch (InvalidStartFileException ex) {
            _logger.error(ex);
            throw new InvalidParametersException(
                    localizedMessages.getString("widgets.no-start-file") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$     
        } catch (BadManifestException ex) {
            _logger.error(ex);
            String message = ex.getMessage();
            if (ex.getMessage() == null || ex.getMessage().equals(""))message = localizedMessages.getString("widgets.invalid-config-xml"); //$NON-NLS-1$
            if (ex instanceof InvalidContentTypeException)
                message = localizedMessages.getString("widgets.unsupported-content-type");//$NON-NLS-1$
            throw new InvalidParametersException(message);
        } catch (BadWidgetZipFileException ex) {
            _logger.error(ex);
            String message = ex.getMessage();
            if (ex.getMessage() == null || ex.getMessage().equals(""))message = localizedMessages.getString("widgets.bad-zip-file"); //$NON-NLS-1$
            throw new InvalidParametersException(message);
        } catch (Exception ex) {
            _logger.error(ex);
            throw new InvalidParametersException(
                    localizedMessages.getString("widgets.cant-parse-config-xml") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } finally {
            // ** No matter what always remove all the resources **
            if(fac.getUnzippedWidgetDirectory() != null){
                WidgetFileUtils.removeWidgetResources(tempUploadFolder, fac.getUnzippedWidgetDirectory().getName());
            }
            // also delete the uploaded wgt file
            if(zipFile.exists()){
                zipFile.delete();
            }
        }
    }
}
