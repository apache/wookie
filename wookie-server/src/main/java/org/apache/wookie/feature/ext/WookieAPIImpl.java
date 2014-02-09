/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.feature.ext;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.auth.AuthTokenUtils;
import org.apache.wookie.auth.InvalidAuthTokenException;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.server.LocaleHandler;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Wookie Extensions API Implementation
 */
public class WookieAPIImpl implements IWookieExtensionAPI {

  static Logger _logger = Logger.getLogger(WookieAPIImpl.class.getName());
  
  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#lock(java.lang.String)
   */
  public String lock(String id_key) {
    HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    Messages localizedMessages = LocaleHandler.localizeMessages(request);
    
    try {
		AuthToken authToken = AuthTokenUtils.decryptAuthToken(id_key);
	    Notifier.callSiblings(authToken,"Widget.onLocked(\""+authToken.getContextId()+"\");");//$NON-NLS-1$
		//TODO
	    //WidgetInstancesController.lockWidgetInstance(widgetInstance);
        return "okay"; //$NON-NLS-1$
	} catch (InvalidAuthTokenException e) {
		return localizedMessages.getString("WidgetAPIImpl.0");
	}
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#unlock(java.lang.String)
   */
  public String unlock(String id_key) {
	  HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
	  Messages localizedMessages = LocaleHandler.localizeMessages(request);
	  try {
		  AuthToken authToken = AuthTokenUtils.decryptAuthToken(id_key);
		  Notifier.callSiblings(authToken,"Widget.onUnlocked(\""+authToken.getContextId()+"\");");//$NON-NLS-1$
		  //TODO
		  //WidgetInstancesController.unlocklockWidgetInstance(widgetInstance);
		  return "okay"; //$NON-NLS-1$
	  } catch (InvalidAuthTokenException e) {
		  return localizedMessages.getString("WidgetAPIImpl.0");
	  }
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#hide(java.lang.String)
   */
  public String hide(String id_key){
	  
	  HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
	  Messages localizedMessages = LocaleHandler.localizeMessages(request);
	  try {
		  AuthToken authToken = AuthTokenUtils.decryptAuthToken(id_key);
		    Notifier.callSiblings(authToken,"window.onHide()");//$NON-NLS-1$
		  return "okay"; //$NON-NLS-1$
	  } catch (InvalidAuthTokenException e) {
		  return localizedMessages.getString("WidgetAPIImpl.0");
	  }
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#show(java.lang.String)
   */
  public String show(String id_key){
	  HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
	  Messages localizedMessages = LocaleHandler.localizeMessages(request);
	  try {
		  AuthToken authToken = AuthTokenUtils.decryptAuthToken(id_key);
		    Notifier.callSiblings(authToken,"window.onShow()");//$NON-NLS-1$
		  return "okay"; //$NON-NLS-1$
	  } catch (InvalidAuthTokenException e) {
		  return localizedMessages.getString("WidgetAPIImpl.0");
	  }
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#openURL(java.lang.String)
   */
  // DEPRICATED - implemented in local js object instead
  // NOTE - might not need this - we can call window.open in a browser -
  // The only reason to send the call to this servlet is if we somehow wish to
  // update other users.
  @Deprecated
  public String openURL(String url) {
    _logger.debug("openurl called with        "+ url ); //$NON-NLS-1$
    WebContext wctx = WebContextFactory.get();
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("window.open(") //$NON-NLS-1$
        .appendData(url)
        .appendScript(");");        //$NON-NLS-1$
        wctx.getScriptSession().addScript(script);
        return ""; //$NON-NLS-1$
  }
  
  /*
   * Note: this method may be deprecated in a future release 
   */
  public String appendSharedDataForKey(String id_key, String key, String value) {

	  HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
	  Messages localizedMessages = LocaleHandler.localizeMessages(request);
	  try {
		  AuthToken authToken = AuthTokenUtils.decryptAuthToken(id_key);
		  //TODO
		  //if(widgetInstance.isLocked()) return localizedMessages.getString("WidgetAPIImpl.2");
		  new SharedContext(authToken).updateSharedData(key, value, true);
		  Notifier.notifySiblings(authToken);
		  return "okay"; //$NON-NLS-1$
	  } catch (InvalidAuthTokenException e) {
		  return localizedMessages.getString("WidgetAPIImpl.0");
	  }
  }

}
