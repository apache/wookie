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

import java.util.List;

/**
 * The forum manager interface.
 * 
 * @author Paul Sharples  
 * @version $Id: IForumManager.java,v 1.2 2009-07-28 16:05:21 scottwilson Exp $
 *
 */
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
	PostNode getPost(String sharedKey, String postId);

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