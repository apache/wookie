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

package org.apache.wookie.widgets.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wookie.beans.IPost;

/**
 * A bean to model a post (with optional children)
 * @author Paul Sharples
 * 
 * @version $Id: PostNode.java,v 1.2 2009-07-28 16:05:21 scottwilson Exp $
 */
public class PostNode {
	
	private Object id;	
	private String userId;
	private String content;
	private String title;
	private Date publishDate;	
	private Date updateDate;
	private String sharedDataKey;

	private List<PostNode> posts = new ArrayList<PostNode>();
		
    /**
     * Construct transient post node to post message.
     * 
     * @param message message to post
     */
    public PostNode(String message) {
        super();
        this.content = message;
        this.title = message;
        this.publishDate = new Date();
        this.updateDate = this.publishDate;
    }

	/**
	 * Construct transient from persistent post node.
	 * 
	 * @param post persistent post node
	 */
	public PostNode(IPost post) {
		super();
		this.id = post.getId();
		this.userId = post.getUserId();
		this.content = post.getContent();
		this.title = post.getTitle();
		this.publishDate = post.getPublishDate();
		this.updateDate = post.getUpdateDate();
		for (IPost childPost : post.getPosts())
		{
		    posts.add(new PostNode(childPost));
		}
	}

	public List<PostNode> getPosts() {
		return posts;
	}

	public Object getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public List<PostNode> getFPosts() {
		return posts;
	}

	public String getSharedDataKey() {
		return sharedDataKey;
	}
}
