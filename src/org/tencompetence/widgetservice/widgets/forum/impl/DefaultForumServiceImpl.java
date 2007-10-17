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

import java.util.List;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.widgets.forum.ForumManager;
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
	
	private static final long serialVersionUID = 1L;
	
	
	public List<PostNode> getNodeTree() {
		ForumManager manager = new ForumManager();
		return manager.getNodeTree();
	}


	public boolean newPost(String parent, String username, String title, String content){
		ForumManager manager = new ForumManager();
		if(manager.newPost(parent, username, title, content)){
			return true;
		}
		else{
			return false;
		}		
	}
	
	public PostNode getPost(String postId){
		_logger.debug("getPost("+postId+")");
		ForumManager manager = new ForumManager();
		return manager.getPost(Integer.parseInt(postId));		
	}
	 
}
