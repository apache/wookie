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

package org.apache.wookie.beans.jpa.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.openjpa.persistence.ElementDependent;
import org.apache.wookie.beans.IPost;
import org.apache.wookie.beans.jpa.IInverseRelationship;
import org.apache.wookie.beans.jpa.InverseRelationshipCollection;

/**
 * PostImpl - JPA IPost implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="Post")
@Table(name="Post")
public class PostImpl implements IPost, IInverseRelationship<PostImpl>
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", nullable=false)
    private int id;

    @Version
    @Column(name="jpa_version")
    @SuppressWarnings("unused")
    private int jpaVersion;

    @Basic(optional=false)
    @Column(name="user_id", nullable=false)
    private String userId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id", referencedColumnName="id")
    @SuppressWarnings("unused")
    private PostImpl parent;
    
    @OneToMany(mappedBy="parent", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<PostImpl> posts;
    
    @Basic(optional=false)
    @Column(name="title", nullable=false)
    private String title;

    @Basic
    @Column(name="content")
    private String content;

    @Basic(optional=false)
    @Column(name="publish_date", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    @Basic
    @Column(name="update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Basic(optional=false)
    @Column(name="sharedDataKey", nullable=false)
    private String sharedDataKey;
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IInverseRelationship#updateInverseRelationship(java.lang.Object)
     */
    public void updateInverseRelationship(PostImpl owningObject)
    {
        parent = owningObject;
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
        return id;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPost#getPosts()
     */
    public Collection<IPost> getPosts()
    {
        if (posts == null)
        {
            posts = new ArrayList<PostImpl>();
        }
        return new InverseRelationshipCollection<PostImpl,PostImpl,IPost>(this, posts);
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
}
