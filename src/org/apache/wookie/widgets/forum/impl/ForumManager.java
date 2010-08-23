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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wookie.beans.IPost;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.widgets.forum.IForumManager;
import org.apache.wookie.widgets.forum.PostNode;

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
	    // query for root posts in most to least recent published order
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	    Map<String,Object> values = new HashMap<String,Object>();
        values.put("sharedDataKey", sharedKey);
        values.put("parent", null);
	    IPost [] posts = persistenceManager.findByValues(IPost.class, values, "publishDate", false);
	    // return PostNode hierarchies to mirror IPost hierarchies
        List<PostNode> list = new ArrayList<PostNode>();
	    for(IPost post : posts){
	        list.add(new PostNode(post));
	    }
	    return list;	        
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.widgets.forum.IForumManager#getPost(java.lang.String, java.lang.String)
	 */
	public PostNode getPost(String sharedKey, String postId){
        // query for post by id
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IPost post = persistenceManager.findById(IPost.class, postId);
        if ((post != null) && post.getSharedDataKey().equals(sharedKey))
        {
            // return PostNode hierarchy to mirror IPost hierarchy
            return new PostNode(post);
        }
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.widgets.forum.IForumManager#newPost(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean newPost(String sharedDataKey, String parent, String username, String title, String content){
        // create and save new post
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IPost post = persistenceManager.newInstance(IPost.class);
        post.setTitle(title);
        post.setContent(content);
        post.setUserId(username);
        post.setPublishDate(new Date());
        post.setSharedDataKey(sharedDataKey);
        boolean saved = persistenceManager.save(post);
        // add as child to parent post and save
	    if (parent != null)
	    {
	        // query for parent post by id
	        IPost parentPost = persistenceManager.findById(IPost.class, parent);
	        if (parentPost != null)
	        {
	            // add as child post to parent post
	            parentPost.getPosts().add(post);
                saved = persistenceManager.save(parentPost);
	        }
	    }
		return saved;
	}
}
