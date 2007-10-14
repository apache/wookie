package org.tencompetence.widgetservice.widgets.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostNode {
	
	private int id;
	private String userId;
	private int parentId;
	private String content;
	private String title;
	private Date publishDate;	
	private Date updateDate;

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

}
