/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.widgetservice.widgets.forum.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.server.LocaleHandler;
import org.tencompetence.widgetservice.widgets.forum.IForumManager;
import org.tencompetence.widgetservice.widgets.forum.IForumService;
import org.tencompetence.widgetservice.widgets.forum.PostNode;

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
 * @version $Id: DefaultForumServiceImpl.java,v 1.7 2009-06-06 20:20:03 scottwilson Exp $
 *
 */
public class DefaultForumServiceImpl implements IForumService {
	
	static Logger _logger = Logger.getLogger(DefaultForumServiceImpl.class.getName());
	// string to return when no credential key is supplied by js call
	//public static final String UNAUTHORISED_MESSAGE = Messages.getString("DefaultForumServiceImpl.0");		 //$NON-NLS-1$
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.widgets.forum.IForumService#getNodeTree(java.lang.String)
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
			WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
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
	 * @see org.tencompetence.widgetservice.widgets.forum.IForumService#getPost(java.lang.String, java.lang.String)
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
			WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
			if(widgetInstance!=null){
				IForumManager fManager = new ForumManager();
				String sharedDataKey = widgetInstance.getSharedDataKey();	
				return fManager.getPost(sharedDataKey, Integer.parseInt(postId));	
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
	 * @see org.tencompetence.widgetservice.widgets.forum.IForumService#newPost(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
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
			WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
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
		Date date = new Date();
		return new PostNode(-1, reason, -1, reason, reason, date, date);
	}


}
