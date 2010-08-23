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

package org.apache.wookie.beans.jcr.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.jackrabbit.ocm.manager.collectionconverter.impl.BeanReferenceCollectionConverterImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import org.apache.wookie.beans.IPost;
import org.apache.wookie.beans.jcr.IInverseRelationship;
import org.apache.wookie.beans.jcr.IPathBean;
import org.apache.wookie.beans.jcr.IUuidBean;
import org.apache.wookie.beans.jcr.InverseRelationshipCollection;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;
import org.apache.wookie.beans.jcr.PersistenceListenerAdapter;
import org.apache.wookie.beans.util.IPersistenceManager;

/**
 * PostImpl - JCR OCM IPost implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:post", jcrMixinTypes="mix:referenceable", discriminator=false)
public class PostImpl extends PersistenceListenerAdapter implements IPost, IPathBean, IUuidBean, IInverseRelationship<PostImpl>
{
    @Field(path=true)
    private String path;
    
    @Field(uuid=true)
    private String uuid;

    @Field(jcrName="wookie:userId")
    private String userId;
    
    @Field(jcrName="wookie:title")
    private String title;

    @Field(jcrName="wookie:content")
    private String content;

    @Field(jcrName="wookie:publishDate")
    private Date publishDate;

    @Field(jcrName="wookie:updateDate")
    private Date updateDate;

    @Field(jcrName="wookie:sharedDataKey")
    private String sharedDataKey;

    @Field(jcrName="wookie:parent")
    private String parent;

    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:posts", elementClassName=PostImpl.class, collectionConverter=BeanReferenceCollectionConverterImpl.class)
    private Collection<PostImpl> postImpls;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.PersistenceListenerAdapter#preDelete(org.apache.wookie.beans.util.IPersistenceManager)
     */
    public boolean preDelete(IPersistenceManager persistenceManager)
    {
        // cascade delete of child posts
        if (postImpls != null)
        {
            for (PostImpl post : postImpls)
            {
                if (!persistenceManager.delete(post))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.PersistenceListenerAdapter#preSave(org.apache.wookie.beans.util.IPersistenceManager)
     */
    public boolean preSave(IPersistenceManager persistenceManager)
    {
        // cascade saves of child posts
        if (postImpls != null)
        {
            for (PostImpl post : postImpls)
            {
                if (!persistenceManager.save(post))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IInverseRelationship#updateInverseRelationship(org.apache.wookie.beans.jcr.IUuidBean)
     */
    public void updateInverseRelationship(PostImpl parentObject)
    {
        parent = ((parentObject != null) ? parentObject.getUuid() : null);
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#getContent()
     */
    public String getContent()
    {
        return content;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#setContent(java.lang.String)
     */
    public void setContent(String content)
    {
        this.content = content;
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
    public Object getId()
    {
        return path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IPathBean#getNodePath()
     */
    public String getNodePath()
    {
        return path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IPathBean#setNodePath(java.lang.String)
     */
    public String setNodePath(String nodeRootPath)
    {
        if (path == null)
        {
            path = nodeRootPath+"/"+JCRPersistenceManager.escapeJCRName(sharedDataKey)+"/"+JCRPersistenceManager.escapeJCRName(userId)+"/"+publishDate.getTime();
        }
        return path;
    }

    /**
     * Get raw persistent parent post UUID.
     * 
     * @return parent post UUID
     */
    public String getParent()
    {
        return parent;
    }

    /**
     * Set raw persistent parent post UUID.
     * 
     * @param parent parent post UUID
     */
    public void setParent(String parent)
    {
        this.parent = parent;
    }

    /**
     * Get raw persistent JCR absolute node path.
     * 
     * @return absolute path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Set raw persistent JCR absolute node path.
     * 
     * @param path absolute path
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#getPosts()
     */
    public Collection<IPost> getPosts()
    {
        if (postImpls == null)
        {
            postImpls = new ArrayList<PostImpl>();
        }
        return new InverseRelationshipCollection<PostImpl,PostImpl,IPost>(this, postImpls);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#setPosts(java.util.Collection)
     */
    public void setPosts(Collection<IPost> posts)
    {
        getPosts().clear();
        if (posts != null)
        {
            for (IPost widgetType : posts)
            {
                getPosts().add((PostImpl)widgetType);
            }
        }        
    }

    /**
     * Get post implementations collection.
     * 
     * @return post implementations collection
     */
    public Collection<PostImpl> getPostImpls()
    {
        return postImpls;
    }

    /**
     * Set post implementations collection.
     * 
     * @param postImpls post implementations collection
     */
    public void setPostImpls(Collection<PostImpl> postImpls)
    {
        this.postImpls = postImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#getPublishDate()
     */
    public Date getPublishDate()
    {
        return publishDate;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#setPublishDate(java.util.Date)
     */
    public void setPublishDate(Date publishDate)
    {
        this.publishDate = publishDate;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#getSharedDataKey()
     */
    public String getSharedDataKey()
    {
        return sharedDataKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#setSharedDataKey(java.lang.String)
     */
    public void setSharedDataKey(String sharedDataKey)
    {
        this.sharedDataKey = sharedDataKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#getTitle()
     */
    public String getTitle()
    {
        return title;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#setTitle(java.lang.String)
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#getUpdateDate()
     */
    public Date getUpdateDate()
    {
        return updateDate;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#setUpdateDate(java.util.Date)
     */
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#getUserId()
     */
    public String getUserId()
    {
        return userId;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#setUserId(java.lang.String)
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IUuidBean#getUuid()
     */
    public String getUuid()
    {
        return uuid;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IUuidBean#setUuid(java.lang.String)
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }
}
