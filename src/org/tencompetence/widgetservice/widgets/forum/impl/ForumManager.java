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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.tencompetence.widgetservice.beans.Post;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.IDBManager;
import org.tencompetence.widgetservice.widgets.forum.IForumManager;
import org.tencompetence.widgetservice.widgets.forum.PostNode;

/**
 * The forum manager class.  Methods needed by the forum widget
 * 
 * @author Paul Sharples  
 * @author Phillip Beauvour
 * @version $Id
 *
 */
public class ForumManager implements IForumManager {
	
	static Logger _logger = Logger.getLogger(ForumManager.class.getName());
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.widgets.forum.IForumManager#getNodeTree(java.lang.String)
	 */
	public List<PostNode> getNodeTree(String sharedKey) {				
		IDBManager dbManager = null;
		try {
			List<PostNode> list = new ArrayList<PostNode>();
			LinkedHashMap<Integer, PostNode> postLookupTable = new LinkedHashMap<Integer, PostNode>();
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(Post.class);
			crit.add(Restrictions.eq("sharedDataKey", sharedKey));
			crit.addOrder( Order.desc("publishDate"));
			final List<Post> sqlReturnList =  dbManager.getObjects(Post.class, crit);
			Post[] posts = sqlReturnList.toArray(new Post[sqlReturnList.size()]);
			for(Post post : posts){									
				postLookupTable.put(post.getId(),new PostNode(post.getId(),
						post.getUserId(),post.getParentId(),post.getContent(),post.getTitle(),
						post.getPublishDate(),post.getUpdateDate()));
			}
			// Iterate thru the posts constructing a tree hierarchy
			for(Entry<Integer, PostNode> entry : postLookupTable.entrySet()) {
				PostNode post = entry.getValue();
				// Has a Post as a Parent
				if(post.getParentId()!=0) {
					PostNode parentPost = postLookupTable.get(post.getParentId());
					parentPost.getPosts().add(post);	               
				}
				// No Parent Post so it's a top-level post with the topic as parent
				else {
					list.add(post);	               
				}
			}
			return list;	        
		} 
		catch (Exception ex) {
			dbManager.rollbackTransaction();
			_logger.error(ex.getMessage());
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.widgets.forum.IForumManager#getPost(java.lang.String, int)
	 */
	public PostNode getPost(String sharedKey, int postId){
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(Post.class);
			crit.add(Restrictions.eq("sharedDataKey", sharedKey));
			crit.add(Restrictions.eq("id", postId));
			final List<Post> sqlReturnList =  dbManager.getObjects(Post.class, crit);
			if (sqlReturnList.size() != 1) {
				return null;
			} 
			else {
				Post post = (Post) sqlReturnList.get(0);
				return new PostNode(post.getId(),
						post.getUserId(),post.getParentId(),post.getContent(),post.getTitle(),
						post.getPublishDate(),post.getUpdateDate());
			}			
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.widgets.forum.IForumManager#newPost(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean newPost(String sharedDataKey, String parent, String username, String title, String content){
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		Post post = new Post();
		try {
			post.setParentId(Integer.parseInt(parent));
			post.setTitle(title);
			post.setContent(content);
			post.setUserId(username);
			post.setPublishDate(new Date());
			post.setSharedDataKey(sharedDataKey);
			dbManager.saveObject(post);
			return true;
		} 
		catch (NumberFormatException e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
		}	
		return false;
	}
}
