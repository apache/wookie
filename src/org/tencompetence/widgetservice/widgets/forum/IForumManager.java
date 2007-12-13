package org.tencompetence.widgetservice.widgets.forum;

import java.util.List;

public interface IForumManager {

	/**
	 * Build the tree using PostNodes
	 * @return
	 */
	List<PostNode> getNodeTree(String sharedKey);

	/**
	 * retrieve a post by given ID
	 * @param postId
	 * @return - the correct postnode
	 */
	PostNode getPost(String sharedKey, int postId);

	/**
	 * Add a new post to the given parent post
	 * @param parent
	 * @param username
	 * @param title
	 * @param content
	 * @return
	 */
	boolean newPost(String sharedDataKey, String parent, String username,
			String title, String content);

}