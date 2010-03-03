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
	public abstract void processStartFile(File startFile, W3CWidget model)
			throws Exception;

}