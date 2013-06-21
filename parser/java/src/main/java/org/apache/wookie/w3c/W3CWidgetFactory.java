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
package org.apache.wookie.w3c;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.exceptions.InsecuredWidgetContentException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;
import org.apache.wookie.w3c.exceptions.InvalidStartFileException;
import org.apache.wookie.w3c.impl.WidgetManifestModel;
import org.apache.wookie.w3c.util.WidgetPackageUtils;

/**
 * Factory for parsing W3C Widget packages (.wgt files).
 * 
 * <p>To use the factory you MUST supply a valid output directory into which the Factory will unpack the widget. Other factory
 * properties are optional.<p>
 * 
 * <p>Factory properties:</p>
 * 
 * <dl>
 *   <dt>outputDirectory</dt>
 *   <dd>The directory where the widget will be saved. The factory will expand the widget archive into this
 *   directory, using the widget's identifier to generate a directory name and structure in which to place it</dd>
 *   <dt>startPageProcessor</dt>
 *   <dd>An implementation of the IStartPageProcessor interface. Setting this property allows you to inject a class that can pre-process
 *   start pages for the widget; for example to inject javascript, tidy up HTML, insert template code etc. If this is not set, 
 *   no pre-processing is done by the factory.</dd>
 *   <dt>locales</dt>
 *   <dd>The supported locales (as BCP47 language-tags) to be processed for the widget. This determines which start files, icons, and other localized elements
 *   will be processed and expanded. This is set to "en" by default</dd>
 *   <dt>encodings</dt>
 *   <dd>The supported encodings to be processed for the widget. This determines which custom encodings will be allowed for start files. 
 *   This is set to UTF-8 by default.</dd>
 *   <dt>localPath</dt>
 *   <dd>The base URL from which unpacked widgets will be served, e.g. "/myapp/widgets". The URLs of start files will be appended to
 *   this base URL to create the widget URL. The default value of this property is "/widgets"</dd>
 *   <dt>features</dt>
 *   <dd>The features supported by the implementation; this should be supplied as IRIs e.g. "http://wave.google.com". The features
 *   are matched against features requested by the widget; if the widget requires features that are unsupported, an Exception will be
 *   thrown when parsing the widget package. The default value of this property is an empty String array.</dd>
 * </dl>
 * 
 */
public class W3CWidgetFactory {
	
	// Get the logger
	static Logger _logger = Logger.getLogger(W3CWidgetFactory.class.getName());		
	// this value is set by the parser
	private File unzippedWidgetDirectory;
	private File outputDirectory;
	private IStartPageProcessor startPageProcessor;
	private IDigitalSignatureProcessor digitalSignatureParser;

  private String[] locales;
	private String localPath;
	private String[] features;
	private String[] encodings;

	/**
	 * Set the features to be included when parsing widgets
	 * @param features
	 */
	public void setFeatures(String[] features) {
		this.features = features;
	}

	public W3CWidgetFactory(){
		// Defaults
		this.locales = new String[]{"en"};
		this.features = new String[0];
		this.localPath = "/widgets";
		this.outputDirectory = null;
		this.encodings = new String[]{"UTF-8"};
		this.startPageProcessor = new IStartPageProcessor(){
			public void processStartFile(File startFile, W3CWidget model,IContent content)
					throws Exception {
			}
			
		};
	}
	
	/**
	 * Set the directory to use to save widgets.
	 * @param outputDirectory
	 * @throws IOException if the directory does not exist
	 */
	public void setOutputDirectory(final String outputDirectory) throws IOException {
		if (outputDirectory == null) throw new NullPointerException();
		File file = new File(outputDirectory);
		if (!file.exists()) throw new FileNotFoundException("the output directory does not exist");
		if (!file.canWrite()) throw new IOException("the output directory cannot be written to");
		if (!file.isDirectory()) throw new IOException("the output directory is not a folder");
		this.outputDirectory = file;
	}

	/**
	 * Set the start page processor to use when parsing widgets
	 * @param startPageProcessor
	 */
	public void setStartPageProcessor(final IStartPageProcessor startPageProcessor) {
		this.startPageProcessor = startPageProcessor;
	}
	
    public void setDigitalSignatureParser(IDigitalSignatureProcessor digitalSignatureParser) {
        this.digitalSignatureParser = digitalSignatureParser;
    }

	/**
	 * Set the supported locales to be used when parsing widgets
	 * @param locales
	 */
	public void setLocales(final String[] locales) {
		if (locales == null) throw new NullPointerException("locales cannot be specified as Null");
		this.locales = locales;
	}

	/**
	 * Set the base URL to use
	 * @param localPath
	 * @throws Exception 
	 */
	public void setLocalPath(final String localPath){
		if (localPath == null) throw new NullPointerException("local path cannot be set to Null");
		this.localPath = localPath;
	};
	
	/**
	 * Parse a given ZipFile and return a W3CWidget object representing the processed information in the package.
	 * The widget will be saved in the outputFolder.
	 * 
	 * @param zipFile
	 * @return the widget model
	 * @throws BadWidgetZipFileException if there is a problem with the zip package
	 * @throws BadManifestException if there is a problem with the config.xml manifest file in the package
	 */
	public W3CWidget parse(final File zipFile) throws Exception, BadWidgetZipFileException, BadManifestException{
		return parse(zipFile, null);
	}
	
	/**
	 * Parse a given ZipFile and return a W3CWidget object representing the processed information in the package.
	 * The widget will be saved in the outputFolder.
	 * 
	 * @param zipFile
	 * @param defaultIdentifier a default identifier to use if the widget package does not have an identifier
	 * @return the widget model
	 * @throws BadWidgetZipFileException if there is a problem with the zip package
	 * @throws BadManifestException if there is a problem with the config.xml manifest file in the package
	 */
	public W3CWidget parse(final File zipFile, String defaultIdentifier) throws Exception, BadWidgetZipFileException, BadManifestException{
	  if (outputDirectory == null) throw new Exception("No output directory has been set; use setOutputDirectory(File) to set the location to output widget files");
		return processWidgetPackage(zipFile, defaultIdentifier);
	}
	
	/**
	 * Parse a widget at a given URL and return a W3CWidget object representing the processed information in the package.
	 * The widget will be saved in the outputFolder.
	 * @param url
	 * @return the widget model
	 * @throws BadWidgetZipFileException if there is a problem with the zip package
	 * @throws BadManifestException if there is a problem with the config.xml manifest file in the package
	 * @throws InvalidContentTypeException if the widget has an invalid content type
	 * @throws IOException if the widget cannot be downloaded
	 */
	public W3CWidget parse(final URL url) throws BadWidgetZipFileException, BadManifestException, InvalidContentTypeException, IOException, Exception{
		File file = download(url,false);
		return parse(file);
	}
	
	/**
	 * Parse a widget at a given URL and return a W3CWidget object representing the processed information in the package.
	 * The widget will be saved in the outputFolder.
	 * @param url
	 * @param ignoreContentType set to true to instruct the parser to ignore invalid content type exceptions
	 * @return the widget model
	 * @throws BadWidgetZipFileException if there is a problem with the zip package
	 * @throws BadManifestException if there is a problem with the config.xml manifest file in the package
	 * @throws InvalidContentTypeException if the widget has an invalid content type
	 * @throws IOException if the widget cannot be downloaded
	 */
	public W3CWidget parse(final URL url, boolean ignoreContentType) throws BadWidgetZipFileException, BadManifestException, InvalidContentTypeException, IOException, Exception{
		File file = download(url,ignoreContentType);
		return parse(file);
	}
	
	/**
	 * Parse a widget at a given URL and return a W3CWidget object representing the processed information in the package.
	 * The widget will be saved in the outputFolder.
	 * @param url
	 * @param ignoreContentType set to true to instruct the parser to ignore invalid content type exceptions
	 * @param defaultIdentifier an identifier to use if the downloaded widget has no identifier - for example when updating a widget
	 * @return the widget model
	 * @throws BadWidgetZipFileException if there is a problem with the zip package
	 * @throws BadManifestException if there is a problem with the config.xml manifest file in the package
	 * @throws InvalidContentTypeException if the widget has an invalid content type
	 * @throws IOException if the widget cannot be downloaded
	 */
	public W3CWidget parse(final URL url, boolean ignoreContentType, String defaultIdentifier) throws BadWidgetZipFileException, BadManifestException, InvalidContentTypeException, IOException, Exception{
		File file = download(url,ignoreContentType);
		return parse(file, defaultIdentifier);
	}
	
	/**
	 * The standard MIME type for a W3C Widget
	 */
	private static final String WIDGET_CONTENT_TYPE = "application/widget";
	
	/**
	 * Download widget from given URL
	 * @param url the URL to download
	 * @param ignoreContentType if set to true, will ignore invalid content types (not application/widget)
	 * @return the File downloaded
	 * @throws InvalidContentTypeException 
	 * @throws HttpException
	 * @throws IOException
	 */
	private File download(URL url, boolean ignoreContentType) throws InvalidContentTypeException, HttpException, IOException {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url.toString());
		client.executeMethod(method);
		if (!ignoreContentType){
		    Header header = method.getResponseHeader("Content-Type");
		    if (header == null) throw new InvalidContentTypeException("Problem downloading widget: expected a content type of "+WIDGET_CONTENT_TYPE+" but received no content type description.");
			String type = header.getValue();
			if (!type.startsWith(WIDGET_CONTENT_TYPE)) throw new InvalidContentTypeException("Problem downloading widget: expected a content type of "+WIDGET_CONTENT_TYPE+" but received:"+type);
		}
		File file = File.createTempFile("wookie", null);
		FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(method.getResponseBodyAsStream()));
		method.releaseConnection();
		return file;
	}

	public void setEncodings(final String[] encodings) throws Exception {
		if (encodings == null) throw new NullPointerException("Supported encodings cannot be set to null");
		if (encodings.length == 0) throw new Exception("At least one encoding must be specified");
		this.encodings = encodings;
	}
	
	/**
	 * Process a widget package for the given zip file
	 * @param zipFile
	 * @return a W3CWidget representing the widget
	 * @throws BadWidgetZipFileException
	 * @throws BadManifestException
	 */
	private W3CWidget processWidgetPackage(File zipFile, String defaultIdentifier) throws BadWidgetZipFileException,
	BadManifestException, InsecuredWidgetContentException {
		ZipFile zip;
		try {
			zip = new ZipFile(zipFile);
		} catch (IOException e) {
			throw new BadWidgetZipFileException();
		}
		if (WidgetPackageUtils.hasManifest(zip)){
			try {
				
				//
				// If locales is set to "*" then look in the package and process all locales found
				//
				
				if (locales.length == 1 && locales[0].equals("*")){
					locales = WidgetPackageUtils.getLocalesFromZipFile(zip);
				}
				
				// build the model
				WidgetManifestModel widgetModel = new WidgetManifestModel(WidgetPackageUtils.extractManifest(zip), locales, features, encodings, zip, defaultIdentifier);															

				// get the widget identifier
				String manifestIdentifier = widgetModel.getIdentifier();						
				// create the folder structure to unzip the zip into
				unzippedWidgetDirectory = WidgetPackageUtils.createUnpackedWidgetFolder(outputDirectory, manifestIdentifier);
				// now unzip it into that folder
				WidgetPackageUtils.unpackZip(zip, unzippedWidgetDirectory);
				// checks for validity of widget using digital signatures
				if (digitalSignatureParser != null) {
					digitalSignatureParser
					.processDigitalSignatures(unzippedWidgetDirectory
							.getAbsolutePath());
				}
				// Iterate over all start files and update paths
				for (IContent content: widgetModel.getContentList()){
					// now update the js links in the start page
					File startFile = new File(unzippedWidgetDirectory.getCanonicalPath() + File.separator + content.getSrc());
					String relativestartUrl = (WidgetPackageUtils.getURLForWidget(localPath, manifestIdentifier, content.getSrc())); 					
					content.setSrc(relativestartUrl);
					if(startFile.exists() && startPageProcessor != null){		
						startPageProcessor.processStartFile(startFile, widgetModel, content);
					}
				}
				if (widgetModel.getContentList().isEmpty()){
					throw new InvalidStartFileException("Widget has no start page");
				}

				// get the path to the root of the unzipped folder
				String thelocalPath = WidgetPackageUtils.getURLForWidget(localPath, manifestIdentifier, "");
				// now pass this to the model which will prepend the path to local resources (not web icons)
				widgetModel.updateIconPaths(thelocalPath);

				// check to see if this widget already exists in the DB - using the ID (guid) key from the manifest
				return widgetModel;

			}catch(InsecuredWidgetContentException ex){
				throw ex;
			}catch (InvalidStartFileException e) {
				throw e;
			} catch (BadManifestException e) {
				throw e;
			} catch (Exception e){
				throw new BadManifestException(e);
			} finally {	
				try {
					zip.close();
				} catch (IOException e) {
					_logger.error("Unable to close wgt file:" + e.getMessage());
				}
			}

		}
		else{
			try {
				zip.close();
			} catch (IOException e) {
				_logger.error("Unable to close wgt file (without manifest):" + e.getMessage());
			}
			// no manifest file found in zip archive
			throw new BadWidgetZipFileException(); //$NON-NLS-1$ 
		}
	}
	
	public File getUnzippedWidgetDirectory() {
		return unzippedWidgetDirectory;
	}

}
