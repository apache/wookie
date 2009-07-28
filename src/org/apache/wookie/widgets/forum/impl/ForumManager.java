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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.wookie.beans.Post;
import org.apache.wookie.util.hibernate.DBManagerFactory;
import org.apache.wookie.util.hibernate.IDBManager;
import org.apache.wookie.widgets.forum.IForumManager;
import org.apache.wookie.widgets.forum.PostNode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * The forum manager class.  Methods needed by the forum widget
 * 
 * @author Paul Sharples  
 * @author Phillip Beauvour
 * @version $Id: ForumManager.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 *
 */
public class ForumManager implements IForumManager {
	
	static Logger _logger = Logger.getLogger(ForumManager.class.getName());
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.widgets.forum.IForumManager#getNodeTree(java.lang.String)
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
				if(post.getParentId()!=-1) {
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
	 * @see org.apache.wookie.widgets.forum.IForumManager#getPost(java.lang.String, int)
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
	 * @see org.apache.wookie.widgets.forum.IForumManager#newPost(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
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
