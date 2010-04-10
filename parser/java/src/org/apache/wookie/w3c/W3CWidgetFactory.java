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

import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
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
	
	private File outputDirectory;
	private IStartPageProcessor startPageProcessor;
	private String[] locales;
	private String localPath;
	private String[] features;

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
		this.startPageProcessor = new IStartPageProcessor(){
			public void processStartFile(File startFile, W3CWidget model)
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
		if (outputDirectory == null) throw new Exception("No output directory has been set; use setOutputDirectory(File) to set the location to output widget files");
		return WidgetPackageUtils.processWidgetPackage(zipFile, localPath, outputDirectory, locales, startPageProcessor, features);
	}
}
