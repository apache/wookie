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

/**
 * A bean to model a post (with optional children)
 * @author Paul Sharples
 * 
 * @version $Id: PostNode.java,v 1.2 2009-07-28 16:05:21 scottwilson Exp $
 */
public class PostNode {
	
	private int id;	
	private String userId;
	private int parentId;
	private String content;
	private String title;
	private Date publishDate;	
	private Date updateDate;
	private String sharedDataKey;

	private List<PostNode> posts;
	
	
	public PostNode(int id, String userId, int parentId, String content,
			String title, Date publishDate, Date updateDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.parentId = parentId;
		this.content = content;
		this.title = title;
		this.publishDate = publishDate;
		this.updateDate = updateDate;
	}

	public List<PostNode> getPosts() {
		if(posts == null) {
			posts = new ArrayList<PostNode>();
		}
		return posts;
	}

	public int getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public int getParentId() {
		return parentId;
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

	public void setSharedDataKey(String sharedDataKey) {
		this.sharedDataKey = sharedDataKey;
	}

}
