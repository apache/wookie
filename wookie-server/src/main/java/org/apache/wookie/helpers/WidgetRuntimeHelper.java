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
/**
 * Server utililies
 */
public class WidgetRuntimeHelper {
  
  // should be overridden in contextlistener
  private static String webContextPath = "/wookie";
  public static String USE_PREFERENCE_INSTANCE_QUEUES = "widget.preferences.useinstancequeues";
  public static String USE_SHAREDDATA_INSTANCE_QUEUES = "widget.shareddata.useinstancequeues";
  public static String DWR_SET_PREFERENCE_CALL = getWebContextPath() + "/dwr/call/plaincall/WidgetImpl.setPreferenceForKey.dwr";
  public static String DWR_SET_SHAREDDATA_CALL = getWebContextPath() + "/dwr/call/plaincall/WookieImpl.setSharedDataForKey.dwr"; 
  public static String DWR_APPEND_SHAREDDATA_CALL = getWebContextPath() + "/dwr/call/plaincall/WookieImpl.appendSharedDataForKey.dwr"; 
  // The default widget icon
  public static String DEFAULT_ICON_PATH = getWebContextPath() + "/shared/images/cog.gif";  
  // The default Gadget icon   
  public static String DEFAULT_GADGET_ICON = getWebContextPath() + "/shared/images/defaultwidget.png"; 


  public static String getWebContextPath() {
    return webContextPath;
  }

  public static void setWebContextPath(String webContextPath) {    
    WidgetRuntimeHelper.webContextPath = webContextPath;
  }
   
}
