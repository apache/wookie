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

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.manager.IWidgetAPIManager;
import org.tencompetence.widgetservice.manager.impl.WidgetAPIManager;
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
 * @version $Id
 *
 */
public class DefaultForumServiceImpl implements IForumService {
	
	static Logger _logger = Logger.getLogger(DefaultForumServiceImpl.class.getName());
	// string to return when no credential key is supplied by js call
	public static final String UNAUTHORISED_MESSAGE = "Unauthorised";		
	
	private static final long serialVersionUID = 1L;
	
	
	public List<PostNode> getNodeTree(String id_key) {
		System.out.println("DefaultForumServiceImpl: key is"+id_key);
		if(id_key==null){		
			return getErrorList(UNAUTHORISED_MESSAGE);			
		}				
		try {
			// check if instance is valid
			WidgetInstance widgetInstance = checkUserKey(id_key);
			if(widgetInstance!=null){
				IForumManager fManager = new ForumManager();
				String sharedDataKey = widgetInstance.getRunId() + "-" + widgetInstance.getEnvId() + "-" + widgetInstance.getServiceId();	
				return fManager.getNodeTree(sharedDataKey);
			}
			else{
				return getErrorList(UNAUTHORISED_MESSAGE);		
			}
		}
		catch (Exception ex) {			
			_logger.error("Error getting the treenodes in the discussion", ex);
			return getErrorList("General Error");	
		}	
	}
	
	
	public PostNode getPost(String id_key, String postId){
		if(id_key==null){		
			return getErrorPost(UNAUTHORISED_MESSAGE);			
		}					
		try {
			// check if instance is valid
			WidgetInstance widgetInstance = checkUserKey(id_key);
			if(widgetInstance!=null){
				IForumManager fManager = new ForumManager();
				String sharedDataKey = widgetInstance.getRunId() + "-" + widgetInstance.getEnvId() + "-" + widgetInstance.getServiceId();	
				return fManager.getPost(sharedDataKey, Integer.parseInt(postId));	
			}
			else{
				return getErrorPost(UNAUTHORISED_MESSAGE);		
			}
		}
		catch (Exception ex) {			
			_logger.error("Error getting a post in the dicussion", ex);
			return getErrorPost("General Error");	
		}			
	}
	 
	public boolean newPost(String id_key, String parent, String username, String title, String content){
		if(id_key==null){		
			return false;			
		}		
		try {
			// check if instance is valid
			WidgetInstance widgetInstance = checkUserKey(id_key);
			if(widgetInstance!=null){
				IForumManager fManager = new ForumManager();
				String sharedDataKey = widgetInstance.getRunId() + "-" + widgetInstance.getEnvId() + "-" + widgetInstance.getServiceId();
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
			_logger.error("Error adding a new post to the discussion", ex);
			return false;	
		}				
	}
	
	
	private WidgetInstance checkUserKey(String id_key){
		IWidgetAPIManager manager = new WidgetAPIManager();					
		return manager.checkUserKey(id_key);		
	}
	
	private List<PostNode> getErrorList(String reason) {
		List<PostNode> errorList = null;
		errorList = new ArrayList<PostNode>();		
		errorList.add(getErrorPost(reason));
		return errorList;
	}
		
	private PostNode getErrorPost(String reason){
		Date date = new Date();
		return new PostNode(-1, reason, -1, reason, reason, date, date);
	}


}
