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

import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.w3c.IContentEntity;
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
	
	private IWidgetInstance instance;

	/**
	 * Constructs a FlatpackProcessor taking a WidgetInstance as the constructor argument.
	 * @param instance
	 */
	public FlatpackProcessor(IWidgetInstance instance) {
		this.instance = instance;
	}

	/**
	 * Processes the start file.
	 * @param startFile the HTML file to process
	 * @param model the Widget object to apply
	 * @content the Content element to apply
	 * TODO implement
	 */
	public void processStartFile(File startFile, W3CWidget model,IContentEntity content) throws Exception {
	}
}
