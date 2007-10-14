package org.tencompetence.widgetservice.widgets.forum.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.widgets.forum.ForumManager;
import org.tencompetence.widgetservice.widgets.forum.IForumService;
import org.tencompetence.widgetservice.widgets.forum.PostNode;


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
