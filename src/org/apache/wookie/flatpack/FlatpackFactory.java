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
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.feature.Features;
import org.apache.wookie.feature.IFeature;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.impl.PreferenceEntity;
import org.apache.wookie.w3c.util.RandomGUID;
import org.apache.wookie.w3c.util.WidgetOutputter;
import org.apache.wookie.w3c.util.WidgetPackageUtils;
import org.apache.wookie.w3c.W3CWidget;

/**
 * Factory class for creating flatpacks - Widgets re-packaged with Widget
 * Instance information and exported as a .wgt package.
 * 
 * <p>
 * For example, this can be used to create an exported Widget for side-loading
 * into a mobile widget runtime.
 * </p>
 * 
 * <p>
 * Factory properties:
 * </p>
 * 
 * <dl>
 * <dt>parser</dt>
 * <dd>The W3CWidgetFactory to use to parse the widget. If this is not
 * specified, DEFAULT_PARSER will be used.</dd>
 * <dt>instance</dt>
 * <dd>The Widget Instance to be flatpacked.</dd>
 * <dt>flatpackFolder</dt>
 * <dd>The folder on the file system where the flatpacked Widget package should
 * be saved. If this is not specified, DEFAULT_FLATPACK_FOLDER will be used</dd>
 * </dl>
 * 
 * <p>
 * When the FlatpackFactory has been created, call the <code>pack()</code>
 * method to pack the widget instance, and return a pointer to the file where
 * the .wgt package has been created
 * </p>
 * 
 */
public class FlatpackFactory {

  // the default parser
  public static final W3CWidgetFactory DEFAULT_PARSER = createDefaultParser();
  
  // the default export path to use
  public static final File DEFAULT_FLATPACK_FOLDER = new File("export");
  
  // the default local widget path to use
  private static final String DEFAULT_LOCAL_PATH = "/widgets";

  // Logger for this class
  static Logger _logger = Logger.getLogger(FlatpackFactory.class.getName()); 

  // the widget parser to use
  private W3CWidgetFactory parser; 
  
  // the instance of the widget to flatpack
  private IWidgetInstance instance; 
  
  //the source .wgt file for the instance
  private File inputWidget; 
  
  // the folder where we put all our flatpack .wgt files once we've created them
  private File flatpackFolder;

  /**
   * Constructor, takes a Widget Instance as its argument
   * 
   * @param instance
   *          the instance to flatpack
   */
  public FlatpackFactory(IWidgetInstance instance) {
    this.instance = instance;
  }

  /**
   * Packages a widget instance into a new .wgt Widget package
   * 
   * @return the widget file
   * @throws Exception
   */
  public File pack() throws Exception {
    //
    // Verify configuration and apply defaults
    //
    if (instance == null)
      throw new Exception("No instance specified");
    if (flatpackFolder == null)
      flatpackFolder = DEFAULT_FLATPACK_FOLDER;
    if (inputWidget == null) {
      //
      // try to locate the widget upload package from the WidgetInstance
      //
      inputWidget = new File(instance.getWidget().getPackagePath());
    }

    //
    // If no parser (W3CWidgetFactory) has been specified, create a default
    // parser
    //
    if (parser == null)
      this.setParser(DEFAULT_PARSER);

    //
    // Verify the file locations we're using exist
    //
    if (!inputWidget.exists())
      throw new Exception("Input widget file does not exist:"
          + inputWidget.getPath());
    if (!flatpackFolder.exists()) {
      //
      // Try to create an output folder if none exists
      //
      if (!flatpackFolder.mkdir())
        throw new Exception("Flatpack folder could not be created:"
            + flatpackFolder.getPath());
      //
      // Create an index.html file; this prevents browsing the exported files by
      // sending a GET to the flatpack folder.
      // This is necessary as servlet containers do not have a consistent
      // configuration mechanism for denying directory browsing.
      //
      new File(flatpackFolder.getPath() + "/index.html").createNewFile();
    }

    //
    // Create tmp working area
    //
    File workingArea = File.createTempFile("wookie-flatpack", "");
    if (workingArea.exists())
      workingArea.delete();
    workingArea.mkdir();

    //
    // Set the working area for unpacking the widget
    //
    parser.setOutputDirectory(workingArea.getAbsolutePath());

    //
    // Parse the widget and unpack it into the working area
    //
    W3CWidget widget = parser.parse(inputWidget);

    //
    // Set the W3CWidget's preferences to the current values of the Widget
    // Instance
    //
    widget = setPreferences(widget);
    
    //
    // Add resources for each flattened feature used by the Widget
    //
    widget = processFeatures(widget, parser.getUnzippedWidgetDirectory());

    //
    // Save the config.xml using WidgetOutputter - this marshalls the W3CWidget
    // object into XML
    // and writes it to a file
    //
    WidgetOutputter outputter = new WidgetOutputter();
    outputter.setWidgetFolder(DEFAULT_LOCAL_PATH);
    File configXml = new File(parser.getUnzippedWidgetDirectory(), "config.xml");
    outputter.outputXML(widget, configXml); 

    //
    // Select a filename using a RandomGUID. Its important that flatpack names
    // are not trivial to guess as currently there are no download restrictions.
    //
    String name = new RandomGUID().toString() + ".wgt";

    //
    // Pack up the widget in a Zip
    //
    File outputWidget = new File(flatpackFolder, name);
    WidgetPackageUtils.repackZip(parser.getUnzippedWidgetDirectory(),
        outputWidget);

    //
    // Delete the working area
    //
    workingArea.delete();

    //
    // Return the File object for the flatpacked widget
    //
    return outputWidget;
  }

  /**
   * For each Preference setting in the Widget Instance, either add or update a
   * Preference for the exported Widget set in the Widget Instance
   * 
   * @param widget
   *          the widget to process
   * @return the processed widget
   */
  private W3CWidget setPreferences(W3CWidget widget) {
    //
    // Add each preferences from the instance to the widget
    //
    for (IPreference pref : instance.getPreferences()) {
      PreferenceEntity newPref = (PreferenceEntity) getPreference(
          pref.getName(), widget);
      newPref.setValue(pref.getValue());
      newPref.setReadOnly(pref.isReadOnly());
      widget.getPreferences().add(newPref);
    }
    return widget;
  }

  /**
   * Get the preference entity for the named preference; either use the existing
   * one from the configuration or create a new instance if there was no
   * existing entity.
   * 
   * @param name
   * @param widget
   * @return a preference entity for the named preference
   */
  private org.apache.wookie.w3c.IPreference getPreference(String name, W3CWidget widget) {
    //
    // Check for an existing PreferenceEntity for the W3CWidget with the given
    // name, if so,
    // return it.
    //
    for (org.apache.wookie.w3c.IPreference pref : widget.getPreferences()) {
      if (pref.getName().equals(name))
        return (org.apache.wookie.w3c.IPreference) pref;
    }
    //
    // Create a new PreferenceEntity and return it
    //
    PreferenceEntity pref = new PreferenceEntity();
    pref.setName(name);
    return (org.apache.wookie.w3c.IPreference) pref;
  }

  /**
   * Set the Widget file to flatpack. TODO remove this as the WidgetInstance
   * should be capable of being used to locate the Widget.
   * 
   * @param inputWidget
   */
  public void setInputWidget(File inputWidget) {
    this.inputWidget = inputWidget;
  }

  /**
   * Set the folder where flatpacked Widgets should be exported, e.g.
   * "/flatpack" or "/exports"
   * 
   * @param flatpackFolder
   */
  public void setFlatpackFolder(File flatpackFolder) {
    this.flatpackFolder = flatpackFolder;
  }
  
  /**
   * for each Feature that is flagged as being suitable for "flattening", add
   * its resources (e.g. JS, CSS) to the exported package and remove the &lt;feature&gt; element from
   * the W3CWidget. Note that we put the complete contents of the feature in the exported package -
   * this should ensure any required README, LICENSE and NOTICE files are exported too.
   * 
   * @param widget the widget being exported
   * @param folder the folder to add resources to
   * @return widget with flattened feature elements removed
   * @throws IOException
   */
  private W3CWidget processFeatures(W3CWidget widget, File folder)
      throws IOException {
    ArrayList<org.apache.wookie.w3c.IFeature> featuresToRemove = new ArrayList<org.apache.wookie.w3c.IFeature>();
    for (org.apache.wookie.w3c.IFeature feature : widget.getFeatures()) {
      for (IFeature theFeature : Features.getFeatures()) {
        if (theFeature.getName().equals(feature.getName())
            && theFeature.flattenOnExport()) {
          //
          // Copy everything under the feature to the working area
          //
          File featureFolder = new File(theFeature.getFolder());
          FileUtils.copyDirectoryToDirectory(featureFolder, folder);
          featuresToRemove.add((org.apache.wookie.w3c.IFeature) feature);
        }
      }
    }
    //
    // Remove flattened features from the W3CWidget
    //
    for (org.apache.wookie.w3c.IFeature feature : featuresToRemove) {
      widget.getFeatures().remove(feature);
    }
    return widget;
  }

  /**
   * Sets the W3CWidgetFactory to use as the widget parser Note that we override
   * the startPageProcessor with FlatpackProcessor and rewrite the local path to
   * DEFAULT_LOCAL_PATH.
   * 
   * @param factory
   * @throws IOException
   */
  public void setParser(W3CWidgetFactory factory) throws IOException {
    parser = factory;
    parser.setStartPageProcessor(new FlatpackProcessor());
    parser.setLocalPath(DEFAULT_LOCAL_PATH);
  }

  /*
   * Construct a standard W3CWidgetFactory parser for testing
   */
  private static W3CWidgetFactory createDefaultParser() {
    W3CWidgetFactory fac = new W3CWidgetFactory();
    //
    // Set the local path to the default value
    //
    fac.setLocalPath(DEFAULT_LOCAL_PATH);
    //
    // Set the features to all locally installed features
    //
    fac.setFeatures(Features.getFeatureNames());
    //
    // Set UTF-8 encoding
    //
    try {
      fac.setEncodings(new String[] { "UTF-8" });
    } catch (Exception e) {
      _logger.error("Problem setting character encoding when creating default parser: ", e);
    }

    return fac;
  }

}
