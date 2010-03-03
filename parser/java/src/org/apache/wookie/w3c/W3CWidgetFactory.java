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

import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.util.WidgetPackageUtils;

/**
 * Factory for parsing W3C Widget packages (.wgt files).
 */
public class W3CWidgetFactory {
	
	private String outputDirectory;
	private IStartPageProcessor startPageProcessor;
	private String[] locales;
	private String localPath;
	private String[] features;

	public String[] getFeatures() {
		return features;
	}

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

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public IStartPageProcessor getStartPageProcessor() {
		return startPageProcessor;
	}

	public void setStartPageProcessor(IStartPageProcessor startPageProcessor) {
		this.startPageProcessor = startPageProcessor;
	}

	public String[] getLocales() {
		return locales;
	}

	public void setLocales(String[] locales) {
		this.locales = locales;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
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
	public W3CWidget parse(File zipFile) throws BadWidgetZipFileException, BadManifestException{
		return WidgetPackageUtils.processWidgetPackage(zipFile, localPath, outputDirectory, locales, startPageProcessor, features);
	}
}
