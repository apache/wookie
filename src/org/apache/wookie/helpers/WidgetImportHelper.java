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
package org.apache.wookie.helpers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.wookie.w3c.W3CWidget;

//TODO improve this so it more than a basic stub
public class WidgetImportHelper {
  
  private static String ERROR_RESPONSE = "<error>unable to read config.xml</error>";

  public static String createXMLWidgetDocument(W3CWidget widgetModel, File configXml) {
    if(configXml.exists()){
      String xmlContents;
      try {
        xmlContents = FileUtils.readFileToString(configXml, "UTF-8");
        return updatePaths(widgetModel, xmlContents);
      } catch (IOException e) {
        return ERROR_RESPONSE;
      }
    }else{
      return ERROR_RESPONSE;
    }
    
  }
  
  private static String updatePaths(W3CWidget widgetModel, String configXml){
    //TODO - some jdom stuff to update the paths to start files & icons
    return configXml;
  }

}
