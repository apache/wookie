package org.apache.wookie.connector.framework;

/**
 * An instance of a widget for use on the client.
 * 
 * @refactor this class duplicates code in the widget bean o nthe server side
 *
 */
public class WidgetInstance {
  String url;

  String id;
  String title;
  String height;
  String width;
  String maximize;

  public WidgetInstance(String url, String id, String title, String height,
      String width, String maximize) {
    setId(id);
    setUrl(url);
    setTitle(title);
    setHeight(height);
    setWidth(width);
    setMaximize(maximize);
  }
  
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getMaximize() {
    return maximize;
  }

  public void setMaximize(String maximize) {
    this.maximize = maximize;
  }
  
  public boolean isMaximizable() {
    return this.maximize.toLowerCase().equals("true");
  }
}