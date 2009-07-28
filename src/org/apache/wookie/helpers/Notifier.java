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
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;

/**
 * Propagates server-driven events out to sibling widget instances
 * TODO only propagate to real siblings - at moment tells too many instances
 * @author scott
 *
 */
public class Notifier {
	
	public static final String STATE_UPDATED = "Widget.onSharedUpdate();";
	public static final String PARTICIPANTS_UPDATED = "wave.onParticipantUpdate();";
	
	/**
	 * Notifies widgets that states have been updated
	 * @param session
	 * @param manager
	 * @param instance
	 */
	public static void notifyWidgets(HttpSession session, WidgetInstance instance, String method){
		ServletContext ctx = session.getServletContext();
		ServerContext sctx = ServerContextFactory.get(ctx);
		String currentPage = instance.getWidget().getUrl();
		ScriptBuffer script = new ScriptBuffer();
		script.appendScript(method);
		Collection<?> pages = sctx.getScriptSessionsByPage(currentPage);
		for (Iterator<?> it = pages.iterator(); it.hasNext();){
			ScriptSession otherSession = (ScriptSession) it.next();
			otherSession.addScript(script);
		}
	}	
}
