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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.wookie.feature.Features;
import org.apache.wookie.feature.IFeature;
import org.apache.wookie.util.html.HtmlCleaner;
import org.apache.wookie.util.html.IHtmlProcessor;
import org.apache.wookie.w3c.IContentEntity;
import org.apache.wookie.w3c.IFeatureEntity;
import org.apache.wookie.w3c.IStartPageProcessor;
import org.apache.wookie.w3c.W3CWidget;

/**
 * <p>
 * This class is used to help create a "Flatpack" - a .wgt archive that can also
 * include WidgetInstance information.
 * </p>
 * 
 * <p>
 * This class is invoked by the W3CWidgetFactory class when invoked by
 * FlatpackFactory to unpack a Widget.
 * </p>
 * 
 * <p>
 * The purpose of this processor is to modify the Widget package, for Features
 * that are flagged as being suitable for "flattening", it adds their resources
 * to the exported package, injects script and stylesheet references in each
 * start file, and removes the &lt;feature&gt; element from the Widget's
 * config.xml
 * </p>
 * 
 */
public class FlatpackProcessor implements IStartPageProcessor {

  /**
   * Constructs a FlatpackProcessor
   * 
   * @param instance
   */
  public FlatpackProcessor() {
  }

  /**
   * Processes the start file.
   * 
   * @param startFile the HTML file to process
   * @param model the Widget object to apply
   * @content the Content element to apply TODO implement
   */
  public void processStartFile(File startFile, W3CWidget model,
      IContentEntity content) throws Exception {
    if (startFile == null)
      throw new Exception("Start file cannot be processed: file is null");
    if (!startFile.exists())
      throw new Exception(
          "Start file cannot be processed:  file does not exist");
    if (!(startFile.canWrite() && startFile.canRead()))
      throw new Exception(
          "Start file cannot be processed: read or write permissions missing");
    if (model == null)
      throw new Exception(
          "Start file cannot be processed: widget model is null");
    //
    // Set the HTML processing engine to use to modify the Widget start files
    // and pass it a reference to a FileReader it can use to read the start file
    //
    IHtmlProcessor engine = new HtmlCleaner();
    engine.setReader(new FileReader(startFile));
    //
    // Process Features
    //
    addFlattenedFeatures(startFile.getParentFile(), engine, model);
    FileWriter writer = new FileWriter(startFile);
    engine.process(writer);
  }

  /**
   * Adds features to widget start file by injecting javascript and stylesheets
   * required by each supported feature in the model.
   * 
   * @param engine
   * @param model
   * @throws Exception if a feature cannot be found and instantiated for the widget.
   */
  private void addFlattenedFeatures(File widgetFolder, IHtmlProcessor engine,
      W3CWidget model) throws Exception {
    for (IFeatureEntity feature : model.getFeatures()) {
      for (IFeature theFeature : Features.getFeatures()) {
        if (theFeature.getName().equals(feature.getName())
            && theFeature.flattenOnExport()) {
          addScripts(engine, theFeature);
          addStylesheets(engine, theFeature);
        }
      }
    }
  }

  /**
   * Adds scripts for a given feature
   * 
   * @param engine
   * @param feature
   */
  private void addScripts(IHtmlProcessor engine, IFeature feature) {
    if (feature.scripts() != null) {
      for (String script : feature.scripts()) {
        // remove the "base" path
        // FIXME this is fragile - consider replacing with a better solution
        script = script.replace("/wookie/features/", "");
        engine.injectScript(script);
      }
    }
  }

  /**
   * Adds stylesheets for a given feature
   * 
   * @param engine
   * @param feature
   */
  private void addStylesheets(IHtmlProcessor engine, IFeature feature) {
    if (feature.stylesheets() != null) {
      for (String style : feature.stylesheets()) {
        // remove the "base" path
        // FIXME this is fragile - consider replacing with a better solution
        style = style.replace("/wookie/features/", "");
        engine.injectStylesheet(style);
      }
    }
  }
}
