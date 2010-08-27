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
 *  limitations under the License.
 */
package org.apache.wookie.beans;

import java.util.Collection;
import java.util.Date;

public interface IPost extends IBean
{
    /**
     * Get widget shared data key.
     * 
     * @return data key
     */
    String getSharedDataKey();
    
    /**
     * Set widget shared data key.
     * 
     * @param sharedDataKey data key
     */
    void setSharedDataKey(String sharedDataKey);

    /**
     * Get widget instance user id.
     * 
     * @return user id
     */
    String getUserId();
    
    /**
     * Set widget instance user id.
     * 
     * @param userId user id
     */
    void setUserId(String userId);
    
    /**
     * Get collection of child posts.
     * 
     * @return posts collection
     */
    Collection<IPost> getPosts();
    
    /**
     * Set collection of child posts.
     * 
     * @param posts collection
     */
    void setPosts(Collection<IPost> posts);
    
    /**
     * Get post content string.
     * 
     * @return content string
     */
    String getContent();
    
    /**
     * Set post content string.
     * 
     * @param content content string
     */
    void setContent(String content);
    
    /**
     * Get post title.
     * 
     * @return title
     */
    String getTitle();
    
    /**
     * Set post title
     * 
     * @param title title
     */
    void setTitle(String title);
    
    /**
     * Get post publish date.
     * 
     * @return publish date
     */
    Date getPublishDate();
    
    /**
     * Set post publish date.
     * 
     * @param publishDate publish date
     */
    void setPublishDate(Date publishDate);
    
    /**
     * Get post update date.
     * 
     * @return update date
     */
    Date getUpdateDate();
    
    /**
     * Set post update date.
     * 
     * @param updateDate update date
     */
    void setUpdateDate(Date updateDate);
}
