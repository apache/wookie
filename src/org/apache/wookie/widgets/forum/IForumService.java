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
 * Methods a forum service will implement
 * @author Paul Sharples
 *
 * @version $Id: IForumService.java,v 1.2 2009-07-28 16:05:21 scottwilson Exp $
 */
public interface IForumService {
	
	/**
	 * Get the tree nodes
	 * @param id_key
	 * @return
	 */
	List<PostNode> getNodeTree(String id_key);
	
	/**
	 * Add a new post
	 * @param id_key
	 * @param parent
	 * @param username
	 * @param title
	 * @param content
	 * @return
	 */
	boolean newPost(String id_key, String parent, String username, String title, String content);
	
	/**
	 * Get specific post 
	 * @param id_key
	 * @param postId
	 * @return
	 */
	PostNode getPost(String id_key, String postId);
}
