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
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.w3c.IIcon;
import org.apache.wookie.w3c.xml.IElement;
import org.apache.wookie.w3c.W3CWidget;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
/**
 * Class used to build a hybrid version of the original config.xml, but with the id attribute updated 
 * (if the parser did so) and updated icon entries
 */
public class WidgetImportHelper {
  
  static Logger logger = Logger.getLogger(WidgetImportHelper.class.getName());
  
  private static String ERROR_RESPONSE = "<error>unable to read config.xml</error>";

  public static String createXMLWidgetDocument(W3CWidget widgetModel, File configXml, String localPath, boolean updateOriginal){
    if(configXml.exists()){
      String xmlContents = null;
      try {
        xmlContents = FileUtils.readFileToString(configXml, "UTF-8");
        if(updateOriginal){
          return updatePaths(widgetModel, xmlContents, localPath);
        }
        else {
          return xmlContents;
        }
      } catch (IOException e) {
        return ERROR_RESPONSE;
      }
    }else{
      return ERROR_RESPONSE;
    }
  }
  
  private static String updatePaths(W3CWidget widgetModel, String configXml, String localPath){
    String updatedConfigXml = null;
    try {
      SAXBuilder builder = new SAXBuilder();
      StringReader reader = new StringReader(configXml);
      String id = null;
      String generatedId = null;
      Document doc = null;
      Element tempElement = null;
      doc = builder.build(reader);
      Element widget = doc.getRootElement();
      id = widget.getAttributeValue("id");
      generatedId = widgetModel.getIdentifier();
      if(id == null || !id.equals(generatedId)){
        widget.setAttribute("id",  widgetModel.getIdentifier());
      }
      int idx = 0;
      tempElement = widget.getChild("icon", widget.getNamespace());
      if(tempElement != null){
        idx = widget.indexOf(tempElement);
        //remove original icon entries
        widget.removeChildren("icon", widget.getNamespace());
        // get the model icons
      }
      List<IIcon> generatedIcons = widgetModel.getIcons();
      for(IIcon icon : generatedIcons){
        String parserSrc = icon.getSrc();
        icon.setSrc(localPath + parserSrc);
        widget.addContent(idx++ , ((IElement) icon).toXml());
        icon.setSrc(parserSrc);
      }

      XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
      updatedConfigXml = outputter.outputString(doc);
    } catch (JDOMException e) {
     logger.error("Error parsing config.", e);
    } catch (IOException e) {
      logger.error("Problem building parser config.", e);
    }
    return updatedConfigXml;
  }

}