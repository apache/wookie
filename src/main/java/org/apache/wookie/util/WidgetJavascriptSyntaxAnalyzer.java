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
package org.apache.wookie.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import org.apache.log4j.Logger;

/**
 * A class designed to be used for parsing javascript in files and reporting any problems
 * 
 * @author Paul Sharples
 * @version $Id$ 
 *
 */
public class WidgetJavascriptSyntaxAnalyzer {
	// Get the logger
	static Logger _logger = Logger.getLogger(WidgetJavascriptSyntaxAnalyzer.class.getName());
	// Collected messages
	private List<String> _messages = null;
	// Search 'parent' folder
	private File _searchFolder = null;

	public WidgetJavascriptSyntaxAnalyzer(File folderToSearch) throws IOException {
		_searchFolder = folderToSearch;
		_messages = new ArrayList<String>();
		if(_searchFolder != null){
			parse();
		}
	}
	
	/**
	 * @return a list of messages found
	 */
	public List<String> getMessages() {
		return _messages;
	}
	
	/**
	 * Parse all sub parsers
	 * @throws IOException
	 */
	public void parse() throws IOException {
		if(_searchFolder != null){
			parseIEIncompatibilities();
			parseDeprecatedMethods();
			parseNonstandardMethods();
			// parse for browser X 
			// parse for browser Y
			// other things to parse for....
		}
	}
	
	/**
	 * Raise a warning for deprecated API calls in the Widget
	 * @throws IOException
	 */
	private void parseDeprecatedMethods() throws IOException {
	  // Pattern match on the syntax 'widget.preferemces.name=value' - including optional quotes & spaces around the value
    Pattern pattern = Pattern.compile("Widget.preferenceForKey|Widget.setPreferenceForKey|Widget.sharedDataForKey|Widget.setSharedDataForKey|Widget.openURL", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher("");
    // Search .js files, but also any html files
    Iterator<?> iter =  FileUtils.iterateFiles(_searchFolder, new String[]{"js","htm","html"}, true);
    while(iter.hasNext()) {
      File file = (File) iter.next();
      LineNumberReader lineReader = new LineNumberReader(new FileReader(file));
      String line = null;
      while ((line = lineReader.readLine()) != null){
        matcher.reset(line);
        if (matcher.find()){                
          String message= "\n(Line " + lineReader.getLineNumber() + ") in file " + file;
          message+= "\n\t "+line+"\n";
          message+= "This file contains calls to Widget API methods that are deprecated and likely to be removed in future releases.\n";                             
          _logger.warn(message);
        }
      }
      lineReader.close();
    }
	}
	
	 /**
   * Raise a warning for API calls in the Widget that use non-standard Wookie-specific extensions
   * @throws IOException
   */
  private void parseNonstandardMethods() throws IOException {
    // Pattern match on the syntax 'widget.preferemces.name=value' - including optional quotes & spaces around the value
    Pattern pattern = Pattern.compile("Widget.appendSharedDataForKey|Widget.lock|Widget.unlock|Widget.show|Widget.hide", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher("");
    // Search .js files, but also any html files
    Iterator<?> iter =  FileUtils.iterateFiles(_searchFolder, new String[]{"js","htm","html"}, true);
    while(iter.hasNext()) {
      File file = (File) iter.next();
      LineNumberReader lineReader = new LineNumberReader(new FileReader(file));
      String line = null;
      while ((line = lineReader.readLine()) != null){
        matcher.reset(line);
        if (matcher.find()){                
          String message= "\n(Line " + lineReader.getLineNumber() + ") in file " + file;
          message+= "\n\t "+line+"\n";
          message+= "This file contains calls to Widget API methods that are Wookie-specific extensions to the W3C Widget API, making it less likely the widget will work in other environments\n";                             
          _logger.warn(message);
        }
      }
      lineReader.close();
    }
  }
	
	/**
	 * Find occurrences of incompatible setter syntax for Internet explorer
	 * i.e. Widget.preferences.foo=bar;
	 * 
	 * @throws IOException
	 */
	private void parseIEIncompatibilities() throws IOException {
		// Pattern match on the syntax 'widget.preferemces.name=value' - including optional quotes & spaces around the value
		Pattern pattern = Pattern.compile("widget.preferences.\\w+\\s*=\\s*\\\"??\\'??.+\\\"??\\'??", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher("");
		// Search .js files, but also any html files
		Iterator<?> iter =  FileUtils.iterateFiles(_searchFolder, new String[]{"js","htm","html"}, true);
		while(iter.hasNext()) {
			File file = (File) iter.next();
			LineNumberReader lineReader = new LineNumberReader(new FileReader(file));
			String line = null;
			while ((line = lineReader.readLine()) != null){
				matcher.reset(line);
				if (matcher.find()){			          
					String message=	"\n(Line " + lineReader.getLineNumber() + ") in file " + file;
					message+= "\n\t "+line+"\n";
					message+= "This file contains preference setter syntax which may not behave correctly in Internet Explorer version 8 and below.\n";
					message+= "See https://cwiki.apache.org/confluence/display/WOOKIE/FAQ#FAQ-ie8prefs for more information.\n";															
					_logger.warn(message);
				}
			}
			lineReader.close();
		}
	}
	
	private String formatWebMessage(String inputString){
		inputString = inputString.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		inputString = inputString.replaceAll("\n", "<br>");
		return "Warning:&nbsp;" + inputString;
	}

}
