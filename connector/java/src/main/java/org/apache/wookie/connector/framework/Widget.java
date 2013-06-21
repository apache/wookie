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

/**
 * A client side representation of a widget. 
 * 
 * @refactor this duplicates data stored in the Widget bean on the server side.
 * 
 * @refactor additional properties
 */
public class Widget { 
	
  String identifier;
  String name;
  String description;
  URL icon;
  String width;
  String height;
  String version;
  String author;
  String license;

  public Widget(String identifier, String name, String description, URL icon,
		  String width, String height, String version, String author, String license) {
    this.identifier = identifier;
    this.name = name;
    this.description = description;
    this.icon = icon;
    this.width = width;
    this.height = height;
    this.version = version;
    this.author = author;
    this.license = license;
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
   * Get the human readable name of this widget.
   * @return the widget name
   */
  public String getName() {
    return name;
  }
  
  /**
   * Get the human readable title of this widget.
   * @deprecated Use "getName" instead
   * @return the widget title
   */
  public String getTitle() {
    return name;
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

public String getWidth() {
	return width;
}

public void setWidth(String width) {
	this.width = width;
}

public String getHeight() {
	return height;
}

public void setHeight(String height) {
	this.height = height;
}

public String getVersion() {
	return version;
}

public void setVersion(String version) {
	this.version = version;
}

public String getAuthor() {
	return author;
}

public void setAuthor(String author) {
	this.author = author;
}

public String getLicense() {
	return license;
}

public void setLicense(String license) {
	this.license = license;
}
  
  
  
}
