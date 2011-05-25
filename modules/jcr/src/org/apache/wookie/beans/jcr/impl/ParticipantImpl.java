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

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.jcr.IPathBean;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;

/**
 * ParticipantImpl - JCR OCM IParticipant implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:participant", discriminator=false)
public class ParticipantImpl implements IParticipant, IPathBean
{
    @Field(path=true)
    private String path;
    
    @Field(jcrName="wookie:participantId")
    private String participantId;

    @Field(jcrName="wookie:participantDisplayName")
    private String participantDisplayName;

    @Field(jcrName="wookie:participantThumbnailUrl")
    private String participantThumbnailUrl;

    @Field(jcrName="wookie:sharedDataKey")
    private String sharedDataKey;

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
            path = nodeRootPath+"/"+JCRPersistenceManager.escapeJCRName(sharedDataKey)+"/"+JCRPersistenceManager.escapeJCRName(participantId);
        }
        return path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IParticipant#getParticipantDisplayName()
     */
    public String getParticipantDisplayName()
    {
        return participantDisplayName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParticipant#setParticipantDisplayName(java.lang.String)
     */
    public void setParticipantDisplayName(String participantDisplayName)
    {
        this.participantDisplayName = participantDisplayName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParticipant#getParticipantId()
     */
    public String getParticipantId()
    {
        return participantId;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParticipant#setParticipantId(java.lang.String)
     */
    public void setParticipantId(String participantId)
    {
        this.participantId = participantId;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParticipant#getParticipantThumbnailUrl()
     */
    public String getParticipantThumbnailUrl()
    {
        return participantThumbnailUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParticipant#setParticipantThumbnailUrl(java.lang.String)
     */
    public void setParticipantThumbnailUrl(String participantThumbnailUrl)
    {
        this.participantThumbnailUrl = participantThumbnailUrl;
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
     * @see org.apache.wookie.beans.IParticipant#getSharedDataKey()
     */
    public String getSharedDataKey()
    {
        return sharedDataKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParticipant#setSharedDataKey(java.lang.String)
     */
    public void setSharedDataKey(String sharedDataKey)
    {
        this.sharedDataKey = sharedDataKey;
    }    

}
