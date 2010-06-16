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

package org.apache.wookie.widgets.forum.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.server.LocaleHandler;
import org.apache.wookie.widgets.forum.IForumManager;
import org.apache.wookie.widgets.forum.IForumService;
import org.apache.wookie.widgets.forum.PostNode;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Implementation of the forum API.  This class models the the javascript implementation of
 * forum API.  Using DWR - a javascript/HTML client which has included the correct js file...
 * 
 *   <script type='text/javascript' src='/wookie/dwr/interface/forum.js'> </script>  
 *   
 *   ...can then access this classes methods via a call. For example...
 *   
 *   forum.getNodeTree();
 * 
 * @author Paul Sharples
 * @version $Id: DefaultForumServiceImpl.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 *
 */
public class DefaultForumServiceImpl implements IForumService {
	
	static Logger _logger = Logger.getLogger(DefaultForumServiceImpl.class.getName());
	// string to return when no credential key is supplied by js call
	//public static final String UNAUTHORISED_MESSAGE = Messages.getString("DefaultForumServiceImpl.0");		 //$NON-NLS-1$
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.widgets.forum.IForumService#getNodeTree(java.lang.String)
	 */
	public List<PostNode> getNodeTree(String id_key) {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = (Messages)session.getAttribute(Messages.class.getName());
		if(localizedMessages == null){
			Locale locale = request.getLocale();
			localizedMessages = LocaleHandler.getInstance().getResourceBundle(locale);
			session.setAttribute(Messages.class.getName(), localizedMessages);			
		}

		if(id_key==null){		
			return getErrorList(localizedMessages.getString("DefaultForumServiceImpl.1"));			
		}
		
		try {
			// check if instance is valid
		    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
			IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
			if(widgetInstance!=null){
				IForumManager fManager = new ForumManager();
				String sharedDataKey = widgetInstance.getSharedDataKey();	
				return fManager.getNodeTree(sharedDataKey);
			}
			else{
				return getErrorList(localizedMessages.getString("DefaultForumServiceImpl.1"));		
			}
		}
		catch (Exception ex) {			
			_logger.error(localizedMessages.getString("DefaultForumServiceImpl.2"), ex); //$NON-NLS-1$
			return getErrorList(localizedMessages.getString("DefaultForumServiceImpl.1"));	 //$NON-NLS-1$
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.widgets.forum.IForumService#getPost(java.lang.String, java.lang.String)
	 */
	public PostNode getPost(String id_key, String postId){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = (Messages)session.getAttribute(Messages.class.getName());
		if(localizedMessages == null){
			Locale locale = request.getLocale();
			localizedMessages = LocaleHandler.getInstance().getResourceBundle(locale);
			session.setAttribute(Messages.class.getName(), localizedMessages);			
		}
		if(id_key==null){		
			return getErrorPost(localizedMessages.getString("DefaultForumServiceImpl.1"));			
		}					
		try {
			// check if instance is valid
            IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
			IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
			if(widgetInstance!=null){
				IForumManager fManager = new ForumManager();
				String sharedDataKey = widgetInstance.getSharedDataKey();	
				return fManager.getPost(sharedDataKey, postId);	
			}
			else{
				return getErrorPost(localizedMessages.getString("DefaultForumServiceImpl.1"));		
			}
		}
		catch (Exception ex) {			
			_logger.error(localizedMessages.getString("DefaultForumServiceImpl.3"), ex); //$NON-NLS-1$
			return getErrorPost(localizedMessages.getString("DefaultForumServiceImpl.1"));	 //$NON-NLS-1$
		}			
	}
	 
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.widgets.forum.IForumService#newPost(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean newPost(String id_key, String parent, String username, String title, String content){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = (Messages)session.getAttribute(Messages.class.getName());
		if(localizedMessages == null){
			Locale locale = request.getLocale();
			localizedMessages = LocaleHandler.getInstance().getResourceBundle(locale);
			session.setAttribute(Messages.class.getName(), localizedMessages);			
		}
		if(id_key==null){		
			return false;			
		}		
		try {
			// check if instance is valid
            IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
			IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
			if(widgetInstance!=null){
				IForumManager fManager = new ForumManager();
				String sharedDataKey = widgetInstance.getSharedDataKey();
					if(fManager.newPost(sharedDataKey, parent, username, title, content)){
						return true;
					}
					else{
						return false;
					}	
			}
			else{
				return false;		
			}
		}
		catch (Exception ex) {			
			_logger.error(localizedMessages.getString("DefaultForumServiceImpl.5"), ex); //$NON-NLS-1$
			return false;	
		}				
	}
	
	/**
	 * Get error messages
	 * @param reason
	 * @return
	 */
	private List<PostNode> getErrorList(String reason) {
		List<PostNode> errorList = null;
		errorList = new ArrayList<PostNode>();		
		errorList.add(getErrorPost(reason));
		return errorList;
	}
	
	/**
	 * single bad post
	 * @param reason
	 * @return
	 */
	private PostNode getErrorPost(String reason){
		return new PostNode(reason);
	}


}
