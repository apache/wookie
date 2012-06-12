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
import java.io.IOException;

/**
 * Processes widget start pages to inject scripts and other assets required for widget runtime operation.
 */
public interface IStartPageProcessor {

	/**
	 * Process a start file ready for deployment. 
	 * @param startFile the start file
	 * @param model the widget information model
	 * @throws IOException if there is a problem reading or writing to the start file, or if the start file cannot be parsed
	 * with the HTML processor used
	 */
	public abstract void processStartFile(File startFile, W3CWidget model, IContent content)
			throws Exception;

}