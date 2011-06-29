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
 * Flatpack Processor
 * 
 * This class is used to help create a "Flatpack" - a .wgt archive that can also include WidgetInstance 
 * information.
 * 
 * This class is invoked by the W3CWidgetFactory class when invoked by FlatpackFactory to unpack a
 * Widget. The purpose of this processor is to modify the HTML start files in the Widget package,
 * injecting scripts only for the features set in the includedFeatures array.
 * 
 * NOTE: At the moment this class doesn't actually do _anything_ as we haven't decided how to flatten features
 * 
 * @author scottbw@apache.org
 *
 */
public class FlatpackProcessor  implements	IStartPageProcessor {

	/**
	 * Constructs a FlatpackProcessor 
	 * @param instance
	 */
	public FlatpackProcessor() {
	}

	/**
	 * Processes the start file.
	 * @param startFile the HTML file to process
	 * @param model the Widget object to apply
	 * @content the Content element to apply
	 * TODO implement
	 */
	public void processStartFile(File startFile, W3CWidget model,IContentEntity content) throws Exception {
	   if (startFile == null) throw new Exception("Start file cannot be processed: file is null");
	    if (!startFile.exists()) throw new Exception("Start file cannot be processed:  file does not exist");
	    if (!(startFile.canWrite()&&startFile.canRead())) throw new Exception("Start file cannot be processed: read or write permissions missing");
	    if (model == null) throw new Exception("Start file cannot be processed: widget model is null");
	    IHtmlProcessor engine = new HtmlCleaner();
	    engine.setReader(new FileReader(startFile));
	    addFlattenedFeatures(startFile.getParentFile(), engine, model);
	    FileWriter writer = new FileWriter(startFile);
	    engine.process(writer);
	}
	
	 /**
   * Adds features to widget start file by injecting javascript and stylesheets
   * required by each supported feature in the model.
   * @param engine
   * @param model
   * @throws Exception if a feature cannot be found and instantiated for the widget.
   */
  private void addFlattenedFeatures(File widgetFolder, IHtmlProcessor engine, W3CWidget model) throws Exception{
    ArrayList<IFeatureEntity> featuresToRemove = new ArrayList<IFeatureEntity>();
    for (IFeatureEntity feature: model.getFeatures()){
      for (IFeature theFeature: Features.getFeatures()){
        if (theFeature.getName().equals(feature.getName()) && theFeature.flattenOnExport()){
          addScripts(engine, theFeature);
          addStylesheets(engine, theFeature);  
          addResources(widgetFolder, theFeature);
          featuresToRemove.add(feature);
        }
      }
    }
    // Remove flattened features
    for (IFeatureEntity feature: featuresToRemove){
      model.getFeatures().remove(feature);
    }
  }
  
  /**
   * @param widgetFolder
   * @param theFeature
   * @throws IOException 
   */
  private void addResources(File widgetFolder, IFeature theFeature) throws IOException {
    // Copy everything under the feature to the widgetfolder
    File featureFolder = new File(theFeature.getFolder());
    FileUtils.copyDirectoryToDirectory(featureFolder, widgetFolder);
  }

  /**
   * Adds scripts for a given feature
   * @param engine
   * @param feature
   */
  private void addScripts(IHtmlProcessor engine, IFeature feature){
    if (feature.scripts() != null){
      for (String script: feature.scripts()){
        // remove the "base" path
        // FIXME this is fragile - consider replacing with a better solution
        script = script.replace("/wookie/features/", "");
        engine.injectScript(script);
      }
    }
  }
  
  /**
   * Adds stylesheets for a given feature
   * @param engine
   * @param feature
   */
  private void addStylesheets(IHtmlProcessor engine, IFeature feature){
    if (feature.stylesheets() != null){
      for (String style: feature.stylesheets()){
        // remove the "base" path
        // FIXME this is fragile - consider replacing with a better solution
        style = style.replace("/wookie/features/", "");
        engine.injectStylesheet(style);
      }
    }
  }
}
