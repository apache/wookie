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

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.util.SiblingPageNormalizer;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Propagates events to widget instances using reverse-AJAX (Comet)
 */
public class Notifier {
	
	public static final String STATE_UPDATED = "Widget.onSharedUpdate();";
	public static final String PARTICIPANTS_UPDATED = "wave.onParticipantUpdate();";
	
	/**
	 * Invokes a JS method on widget instances that are siblings of the given
	 * widget instance. Use this method when you need
	 * to push notifications to widgets from outside of a DWR thread.
	 * @param session the servlet session
	 * @param instance the widget instance whose siblings should be invoked
	 * @param method the method to invoke on sibling widget instances
	 */
	public static void notifyWidgets(HttpSession session, WidgetInstance instance, String method){
		ServletContext ctx = session.getServletContext();
		ServerContext sctx = ServerContextFactory.get(ctx);
		String currentPage = new SiblingPageNormalizer().getNormalizedPage(instance);
		Collection<?> pages = sctx.getScriptSessionsByPage(currentPage);
		call(pages,method);
	}	

	/**
	 * Send notifications to other widgets of shared data updates using the
	 * current script session. Only use this method within a DWR thread.
	 * @param widgetInstance the instance that is the source of the update
	 */
	public static void notifySiblings(WidgetInstance widgetInstance){
		String sharedDataKey = widgetInstance.getSharedDataKey();
		String script = "Widget.onSharedUpdate(\""+sharedDataKey+"\");"; //$NON-NLS-1$ //$NON-NLS-2$
		callSiblings(widgetInstance,script);
	}
	
	/**
	 * Calls a script in sibling widget instances within the scope of the current DWR thread
	 * @param call the JS method to call on the widget start file
	 */
	public static void callSiblings(WidgetInstance instance, String call){
		WebContext wctx = WebContextFactory.get();
		String currentPage = new SiblingPageNormalizer().getNormalizedPage(instance);
        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
	    call(pages, call);	
	}
	
	/**
	 * Invoke the specified call on the script sessions of the pages in the supplied collection
	 * @param pages the collection of pages in scope
	 * @param call the method to invoke on the pages
	 */
	private static void call(Collection<?> pages, String call){
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(call);
        for (Iterator<?> it = pages.iterator(); it.hasNext();){
            ScriptSession otherSession = (ScriptSession) it.next();
            otherSession.addScript(script);
        }	
	}
	
	/**
	 * Calls a script in all sibling widget instances within the scope of the current DWR thread (all widgets with same origin URL)
	 * TODO this does not currently work as we don't set the attribute in the ScriptSession. I've included the code
	 * here as the starting point for anyone wanting to implement features using single-user cross-widget messaging
	 * @param call the JS method to call on the widget start file
	 */
	/*
	public static void callSiblingsByUser(WidgetInstance instance, String call){
		WebContext wctx = WebContextFactory.get();
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(call);
        // Loop over all the users on the current page
        Collection<?> pages = wctx.getAllScriptSessions();
        for (Iterator<?> it = pages.iterator(); it.hasNext();){
            ScriptSession otherSession = (ScriptSession) it.next();
            WidgetInstance otherInstance = WidgetInstance.findByIdKey((String)otherSession.getAttribute("idkey"));
            if (otherInstance.getApiKey().equals(instance.getApiKey()) && otherInstance.getUserId().equals(instance.getUserId()))
            	otherSession.addScript(script);
        }	
	}
	*/
}
