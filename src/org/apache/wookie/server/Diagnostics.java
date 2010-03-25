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

package org.apache.wookie.server;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.helpers.FlashMessage;
import org.apache.wookie.w3c.util.WidgetPackageUtils;

/**
 * Runs some basic diagnostic tests, and raises warnings if there are potential issues both
 * in the log and also through flash messages on the Admin page.
 */
public class Diagnostics {
	
	static Logger _logger = Logger.getLogger(Diagnostics.class.getName());
	
	/**
	 * Run the diagnostic tests
	 * @param context
	 * @param configuration
	 */
	public static void run(ServletContext context, Configuration configuration){
		final File UPLOADFOLDER = new File(context.getRealPath(configuration.getString("widget.useruploadfolder")));
		checkFolder("Upload", UPLOADFOLDER);	
		final File WIDGETFOLDER = new File(context.getRealPath(configuration.getString("widget.widgetfolder")));
		checkFolder("Widget", WIDGETFOLDER);	
		final File DEPLOYFOLDER = new File(WidgetPackageUtils.convertPathToPlatform(context.getRealPath(configuration.getString("widget.deployfolder"))));
		checkFolder("Deploy", DEPLOYFOLDER);	
	}
	
	/**
	 * Check that a folder exists, is readable, and is writable
	 * TODO use localized messages
	 * @param name the name of the folder being checked
	 * @param folder the folder to be checked
	 */
	public static void checkFolder(String name, File folder){
		if (!folder.exists()){
			String error = name+" folder does not exist: "+folder.getAbsolutePath(); 
			_logger.error(error);	
			FlashMessage.getInstance().error(error);
		} else {
			if (!folder.canRead()){
				String error = name+" folder cannot be read from: "+folder.getAbsolutePath(); 
				_logger.error(error);	
				FlashMessage.getInstance().error(error);
			}
			if (!folder.canWrite()){
				String error = name+" folder cannot be written to: "+folder.getAbsolutePath(); 
				_logger.error(error);	
				FlashMessage.getInstance().error(error);
			}
		}
	}

}
