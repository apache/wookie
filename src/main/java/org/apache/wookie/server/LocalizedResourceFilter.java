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
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.apache.wookie.w3c.util.WidgetPackageUtils;

import com.ibm.icu.util.ULocale;

/**
 * A Filter for implementing rules for folder-based localization. Redirects
 * requests for widget resources to resources in appropriate locale folders.
 * 
 * Note that this relies on servlet session management, which relies on cookies
 * being enabled in the browser.
 * 
 * This filter runs for a lot of what are otherwise completely static resources,
 * so its a real drag on server performance
 */
public class LocalizedResourceFilter implements Filter {

  private FilterConfig filterConfig = null;

  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    // We use the "localized" querystring parameter to indicate a resource
    // has already been processed by this algorithm once, to prevent hunting and
    // infinite recursion
    String localized = ((HttpServletRequest) request).getParameter("localized");
    
    //
    // Skip if already localized
    //
    if (localized == null) {

      //
      // Get the Widget this resource request is related to
      //
      IWidget widget = getWidgetFromRequest(request);

      //
      // Only if we have a valid instance and a resource which has no
      // localization parameter do we start the locale algorithm
      //
      if (widget != null) {
        
        //
        // Get the original request URL
        //
        String uri = ((HttpServletRequest) request).getRequestURL().toString();
        URL url = new URL(uri);
        String path = url.getPath();

        //
        // Process the resource with the localization algorithm
        //
        String localizedPath = getLocalizedResource(path);

        //
        // Redirect to localized resource URL only if different from the
        // original resource URL
        //
        if (!path.equals(localizedPath)) {
          uri = uri.replace(path, localizedPath);
          if (uri.contains("?")) {
            uri += "&localized=1";
          } else {
            uri += "?localized=1";
          }
          URL newUrl = new URL(uri);
          ((HttpServletResponse) response).sendRedirect(newUrl.toString());
          return;
        }
      }
    }
    chain.doFilter(request, response);
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
  }

  /**
   * Process a resource path in the context of a widget instance, identify the
   * path for any resource which matches the path in a locale folder which
   * matches the locale preference for the widget instance.
   * 
   * If no locale preference is set for the instance, the default server locale
   * is used.
   * 
   * If no localized path can be determined, the method returns the original
   * path supplied.
   * 
   * @param originalPath
   * @param instance
   * @return the localized path for the resource; if none is found, the original
   *         path is returned
   */
  private String getLocalizedResource(String originalPath) {
    
    //
    // Remove the context URI from the path, or this will mess up
    // the algorithm for locating the file using its real path
    //
    String context = filterConfig.getServletContext().getContextPath();
    String basePath = originalPath.substring(originalPath.indexOf(context)
        + context.length());

    //
    // Strip out the widget base path to obtain just the resource itself
    //
    String widgetPath = getWidgetBasePath();
    int start = originalPath.indexOf(widgetPath) + widgetPath.length();
    String resource;
    try {
      resource = originalPath.substring(start);
    } catch (Exception e) {
      return originalPath;
    }

    //
    // If the resource already contains a locale, remove it for the purposes of
    // searching
    // This is because local links within widgets can target localized files -
    // e.g.
    // In the example widget below, there will be a link from
    // locales/es/index.html to "locales/es/images/test.png"
    // but we need to consider this as a request for /images/test.png
    //
    // index.html
    // /images/test.png
    // /locales/es/index.html
    //
    if (resource.startsWith("locales/")) {
      String[] parts = resource.split("/");
      String prefix = "/" + parts[0] + "/" + parts[1] + "/";
      String nonLocalResource = resource.substring(resource.indexOf(prefix)
          + prefix.length());
      basePath = basePath.replace(resource, nonLocalResource);
      resource = nonLocalResource;
    }

    //
    // For each locale in the list see if a matching localized resource exists.
    // If it does, return it and terminate this algorithm
    //
    List<ULocale> locales = LocalizationUtils.getProcessedLocaleList(new String[] { getPreferredLocale() });
    for (ULocale locale : locales) {
      String path = basePath.replace(resource, "locales/" + locale.toLanguageTag().toLowerCase() + "/" + resource);
      String filePath = filterConfig.getServletContext().getRealPath(path);
      if (new File(filePath).exists())
        return context + path;
    }

    //
    // As the next resort, we'll try defautLocale
    //
    String defaultLocale = getWidgetDefaultLocale();
    if (defaultLocale != null) {
      String path = basePath.replace(resource, "locales/" + defaultLocale.toLowerCase() + "/" + resource);
      String filePath = filterConfig.getServletContext().getRealPath(path);
      if (new File(filePath).exists())
        return context + path;
    }

    //
    // All attempts to locate a localized copy have failed, so we must try to
    // find a non-localized version instead
    //
    if (new File(filterConfig.getServletContext().getRealPath(basePath)).exists())
      return context + basePath;

    //
    // No localized or even non-localized file exists, so just return the
    // original. This situation shouldn't arise except
    // where, e.g., the original request was for a non-existing resource
    //
    return originalPath;
  }

  /**
   * Return the base path for a widget instance
   * 
   * @param instance
   * @return
   */
  private String getWidgetBasePath() {
    Configuration config = (Configuration) filterConfig.getServletContext().getAttribute("properties");
    final String localWidgetFolderPath = filterConfig.getServletContext().getContextPath() + config.getString("widget.widgetfolder");
    return WidgetPackageUtils.getURLForWidget(localWidgetFolderPath, getWidgetGuid(), "");
  }
  
  /**
   * Inspect the request path and identify the widget based on the path
   * components
   * 
   * @param request
   * @return
   */
  private IWidget getWidgetFromRequest(ServletRequest request) {
    //
    // If I have an instance key in the query, use it to look up the widget
    // it belongs to, and put the widget URI and localization preferences into the session
    //
    // This will only happen if the resource is the Start File for the Widget
    //
    String idkey = ((HttpServletRequest) request).getParameter("idkey");
    if (idkey != null){
      IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
      IWidgetInstance instance = persistenceManager.findWidgetInstanceByIdKey(idkey);
      if (instance != null) {
        filterConfig.getServletContext().setAttribute("widget-id", instance.getWidget().getIdentifier());
        filterConfig.getServletContext().setAttribute("widget-instance-locale", instance.getLang());
        filterConfig.getServletContext().setAttribute("widget-default-locale", instance.getWidget().getDefaultLocale());
        return instance.getWidget();
      }
    }

    //
    // Get the widget id from the session, and if its not null, obtain the widget it relates to
    //
    String guid = (String) filterConfig.getServletContext().getAttribute("widget-id");
    if (guid != null) {
      IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
      return persistenceManager.findWidgetByGuid(guid);
    }
    
    //
    // No widget found
    //
    return null;
  }
  
  /**
   * Get the GUID of the widget that the resource in the current context belongs to
   * @return
   */
  private String getWidgetGuid(){
    return (String) filterConfig.getServletContext().getAttribute("widget-id");
  }
  
  /**
   * Get the preferred locale of the widget instances that the resource in the current context belongs to
   * @return
   */
  private String getPreferredLocale(){
    return (String) filterConfig.getServletContext().getAttribute("widget-instance-locale");
  }
  
  /**
   * Get the defaultLocale attribute for the widget that the resource in the current context belongs to
   * @return
   */
  private String getWidgetDefaultLocale(){
    return (String) filterConfig.getServletContext().getAttribute("widget-default-locale"); 
  }

}
