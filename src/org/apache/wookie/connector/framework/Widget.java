package org.apache.wookie.connector.framework;

/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A client side representation of a widget. 
 * 
 * @refactor this duplicates data stored in the Widget bean on the server side.
 */
public class Widget { 
  String identifier;
  String title;
  String description;
  URL icon;
  HashMap<String, WidgetInstance> instances = new HashMap<String, WidgetInstance>();

  public Widget(String identifier, String title, String description, URL icon) {
    this.identifier = identifier;
    this.title = title;
    this.description = description;
    this.icon = icon;
  }
  
  /**
   * Get a unique identifier for this widget type.
   * 
   * @return
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Get the human readable title of this widget.
   * @return
   */
  public String getTitle() {
    return title;
  }

  /**
   * Get the location of a logo for this widget.
   * @return
   */
  public URL getIcon() {
    return icon;
  }
  
  /**
   * Get the description of the widget.
   * 
   * @return
   */
  public String getDescription() {
    return description;
  }

  /**
   * Record an instance of the given widget.
   * 
   * @param xml description of the instance as returned by the widget server when the widget was instantiated.
   * @return the identifier for this instance
   */
  public WidgetInstance addInstance(Document xml) {
    Element rootEl = xml.getDocumentElement();
    String url = rootEl.getElementsByTagName("url").item(0).getTextContent();
    String id = getIdentifier();
    String title = rootEl.getElementsByTagName("title").item(0).getTextContent();
    String height = rootEl.getElementsByTagName("height").item(0).getTextContent();
    String width = rootEl.getElementsByTagName("width").item(0).getTextContent();
    String maximize = rootEl.getElementsByTagName("maximize").item(0).getTextContent();
    WidgetInstance instance = new WidgetInstance(url, id, title, height, width, maximize);
    instances.put(id, instance);
    
    return instance;
  }

  /**
   * Get all instances of a widget available in this server.
   * @return
   */
  public Collection<WidgetInstance> getInstances() {
    return instances.values();
  }
}
