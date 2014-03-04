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
package org.apache.wookie.util.html;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.wookie.feature.Features;
import org.apache.wookie.feature.IFeature;
import org.apache.wookie.w3c.IContent;
import org.apache.wookie.w3c.IStartPageProcessor;
import org.apache.wookie.w3c.W3CWidget;

/**
 * Processes widget start pages to inject scripts and other assets required for widget runtime operation.
 */
public class StartPageProcessor implements IStartPageProcessor {
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.util.html.IStartPageProcessor#processStartFile(java.io.File, org.apache.wookie.w3c.IManifestModel)
	 */
	public void processStartFile(File startFile, W3CWidget model, IContent content) throws Exception{
		if (startFile == null) throw new Exception("Start file cannot be processed: file is null");
		if (!startFile.exists()) throw new Exception("Start file cannot be processed:  file does not exist");
		if (!(startFile.canWrite()&&startFile.canRead())) throw new Exception("Start file cannot be processed: read or write permissions missing");
		if (model == null) throw new Exception("Start file cannot be processed: widget model is null");
		IHtmlProcessor engine = new HtmlCleaner();
		engine.setReader(new FileReader(startFile));
		addDefaultScripts(engine);
		addFeatures(engine, model);
		setContentType(engine, content);
		FileWriter writer = new FileWriter(startFile);
		engine.process(writer);
		writer.close();
	}
	
	private void setContentType(IHtmlProcessor engine, IContent content){
		engine.setTypeAndCharset(content.getType(), content.getCharSet());
	}
	
	/**
	 * Injects default wrapper and utilities
	 * @param engine
	 */
	private void addDefaultScripts(IHtmlProcessor engine){
		for (IFeature theFeature: Features.getFeatures()){
			if (theFeature.getName().equals("http://wookie.apache.org/feature/default")){
				addScripts(engine,theFeature);
			     addStylesheets(engine, theFeature);
			}
		}
	}
	
	/**
	 * Adds features to widget start file by injecting javascript and stylesheets
	 * required by each supported feature in the model.
	 * @param engine
	 * @param model
	 * @throws Exception if a feature cannot be found and instantiated for the widget.
	 */
	private void addFeatures(IHtmlProcessor engine,W3CWidget model) throws Exception{
		for (org.apache.wookie.w3c.IFeature feature: model.getFeatures()){
			for (IFeature theFeature: Features.getFeatures()){
			  if (theFeature.getName().equals(feature.getName())){
		      addScripts(engine, theFeature);
		      addStylesheets(engine, theFeature);			    
			  }
			}
			
		}
	}
	
	/**
	 * Adds scripts for a given feature
	 * @param engine
	 * @param feature
	 */
	private void addScripts(IHtmlProcessor engine, IFeature feature){
		if (feature.scripts() != null){
			for (String script: feature.scripts()) engine.injectScript(script);
		}
	}
	
	/**
	 * Adds stylesheets for a given feature
	 * @param engine
	 * @param feature
	 */
	private void addStylesheets(IHtmlProcessor engine, IFeature feature){
		if (feature.stylesheets() != null){
			for (String style: feature.stylesheets()) engine.injectStylesheet(style);
		}
	}
	
}
