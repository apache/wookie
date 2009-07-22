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
