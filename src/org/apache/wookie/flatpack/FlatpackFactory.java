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

import java.io.File;
import java.io.IOException;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.w3c.IPreferenceEntity;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.impl.FeatureEntity;
import org.apache.wookie.w3c.impl.PreferenceEntity;
import org.apache.wookie.w3c.util.RandomGUID;
import org.apache.wookie.w3c.util.WidgetOutputter;
import org.apache.wookie.w3c.util.WidgetPackageUtils;

/**
 * Factory class for creating flatpacks - Widgets re-packaged with Widget Instance information and exported as a .wgt package.
 * 
 * <p>For example, this can be used to create an exported Widget for side-loading into a mobile widget runtime.</p>
 * 
 * <p>Factory properties:</p>
 * 
 * <dl>
 *   <dt>parser</dt>
 *   <dd>The W3CWidgetFactory to use to parse the widget. If this is not specified, DEFAULT_PARSER will be used.</dd>
 *   <dt>instance</dt>
 *   <dd>The Widget Instance to be flatpacked.</dd>
 *   <dt>flatpackFolder</dt>
 *   <dd>The folder on the file system where the flatpacked Widget package should be saved. If this is not specified, DEFAULT_FLATPACK_FOLDER will be used</dd>
 *   <dt>includeWacFeatures</dt>
 *   <dd>If set to true, the exported Widget package will include core WAC/JIL feature elements. This is false by default.</dd>
 *   <dt>featuresToFlatten</dt>
 *   <dd>The features that should be "flattened" rather than omitted - that is, that should be injected into the final package.</dd>
 * </dl>
 * @author scottbw@apache.org
 *
 */
public class FlatpackFactory {
	
	public static final W3CWidgetFactory DEFAULT_PARSER = createDefaultParser();
	public static final File DEFAULT_FLATPACK_FOLDER = new File("export");
	private static final String DEFAULT_LOCAL_PATH = "/widgets"; // The default local path to use
	private static final String WAC_FEATURE_NAME = "http://jil.org/jil/api/1.1/widget";
	
	private W3CWidgetFactory parser; // the widget parser to use
	private IWidgetInstance instance; // the instance of the widget to flatpack
	private File inputWidget; // the source .wgt file for the instance
	private File flatpackFolder; // the folder where we put all our flatpack .wgt files once we've created them
	private boolean includeWacFeatures = false; // set to true if the WAC/JIL core features should be added
	
	/**
	 * Constructor, takes a Widget Instance as its argument
	 * @param instance the instance to flatpack
	 */
	public FlatpackFactory(IWidgetInstance instance){
		this.instance = instance;
	}
	
	/**
	 * Packages a widget instance into a new .wgt Widget package
	 * @return the widget file
	 * @throws Exception
	 */
	public File pack() throws Exception{
		// Verify configuration and apply defaults
		if (instance == null) throw new Exception("No instance specified");
		if (flatpackFolder == null) flatpackFolder = DEFAULT_FLATPACK_FOLDER;
		if (inputWidget == null){
			// try to locate the widget upload package from the WidgetInstance
			inputWidget = new File(instance.getWidget().getPackagePath());
		}
		if (parser == null) parser = DEFAULT_PARSER;
		
		// Verify the file locations we're using exist
		if (!inputWidget.exists()) throw new Exception("Input widget file does not exist:"+inputWidget.getPath());
		if (!flatpackFolder.exists()){
			if (!flatpackFolder.mkdir()) throw new Exception("Flatpack folder could not be created:"+flatpackFolder.getPath());
			// Create an index.html file; this prevents browsing exported files.
			new File(flatpackFolder.getPath()+"/index.html").createNewFile();
		}
		
		// Create tmp working area
		File workingArea = File.createTempFile("wookie-flatpack", "");
		if (workingArea.exists()) workingArea.delete();
		workingArea.mkdir();
		
		// Set the working area for unpacking the widget
		parser.setOutputDirectory(workingArea.getAbsolutePath());
		
		// Parse the widget and unpack it into the working area
		W3CWidget widget = parser.parse(inputWidget);
		
		// Process the config.xml file
		widget = processWidget(widget);
		
		// Save the config.xml
		WidgetOutputter outputter = new WidgetOutputter();
		outputter.setWidgetFolder(DEFAULT_LOCAL_PATH);
		
		File configXml = new File(parser.getUnzippedWidgetDirectory(), "config.xml");
		outputter.outputXML(widget, configXml);
		
		// Select a filename
		String name = new RandomGUID().toString()+".wgt";
		
		// Pack up the widget
		File outputWidget = new File(flatpackFolder,name);
		WidgetPackageUtils.repackZip(parser.getUnzippedWidgetDirectory(), outputWidget);
		
		// Delete the working area
		workingArea.delete();
		
		return outputWidget;
	}
	
	/**
	 * Processes the Widget object, for example adding any preferences
	 * set in the Widget Instance
	 * @param widget the widget to process
	 * @return the processed widget
	 */
	private W3CWidget processWidget(W3CWidget widget){
		// Add each preferences from the instance to the widget
		for (IPreference pref: instance.getPreferences()){
			PreferenceEntity newPref = (PreferenceEntity)getPreference(pref.getDkey(), widget);
			newPref.setValue(pref.getDvalue());
			newPref.setReadOnly(pref.isReadOnly());
			widget.getPrefences().add(newPref);
		}
		
		// TODO Remove any flattened features
		
		// Add WAC/JIL features if needed
		if (includeWacFeatures){
			FeatureEntity wac = new FeatureEntity(WAC_FEATURE_NAME, true);
			widget.getFeatures().add(wac);
		}
		
		return widget;
	}
	
	/**
	 * Get the preference entity for the named preference; either use the existing
	 * one from the configuration or create a new instance if there was no existing
	 * entity.
	 * @param name
	 * @param widget
	 * @return a preference entity for the named preference
	 */
	private IPreferenceEntity getPreference(String name, W3CWidget widget){
		for (IPreferenceEntity pref:widget.getPrefences()){
			if (pref.getName().equals(name)) return pref;
		}
		PreferenceEntity pref = new PreferenceEntity();
		pref.setName(name);
		return pref;
	}

	/**
	 * Set the Widget file to flatpack. TODO remove this as the WidgetInstance should be capable of being used to locate the Widget.
	 * @param inputWidget
	 */
	public void setInputWidget(File inputWidget) {
		this.inputWidget = inputWidget;
	}

	/**
	 * Set the folder where flatpacked Widgets should be exported, e.g. "/flatpack" or "/exports"
	 * @param flatpackFolder
	 */
	public void setFlatpackFolder(File flatpackFolder) {
		this.flatpackFolder = flatpackFolder;
	}
	
	/**
	 * Set whether to include WAC/JIL features in the flatpacked Widget
	 * @param includeWacFeatures
	 */
	public void setIncludeWacFeatures(boolean includeWacFeatures){
		this.includeWacFeatures = includeWacFeatures;
	}
	
	/**
	 * Sets the W3CWidgetFactory to use as the widget parser
	 * Note that we override the startPageProcessor with FlatpackProcessor
	 * and rewrite the local path to DEFAULT_LOCAL_PATH.
	 * @param factory
	 * @throws IOException 
	 */
	public void setParser(W3CWidgetFactory factory) throws IOException{
		parser = factory;
		parser.setStartPageProcessor(new FlatpackProcessor(this.instance));
		parser.setLocalPath(DEFAULT_LOCAL_PATH);
	}
	
	/*
	 * Construct a standard W3CWidgetFactory parser for testing
	 */
	private static W3CWidgetFactory createDefaultParser() {
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocalPath(DEFAULT_LOCAL_PATH);
		try {
			fac.setEncodings(new String[]{"UTF-8"});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fac;
	}	

}
