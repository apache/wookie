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
package org.tencompetence.widgetservice.widgets.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A bean to model a post (with optional children)
 * @author Paul Sharples
 *
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
