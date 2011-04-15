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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.wookie.beans.IParticipant;

/**
 * ParticipantImpl - JPA IParticipant implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="Participant")
@Table(name="Participant")
@NamedQueries({@NamedQuery(name="PARTICIPANTS", query="SELECT p FROM Participant p WHERE p.sharedDataKey = :sharedDataKey"),
               @NamedQuery(name="PARTICIPANT_VIEWER", query="SELECT p FROM Participant p WHERE p.sharedDataKey = :sharedDataKey AND p.participantId = :userId")})
public class ParticipantImpl implements IParticipant
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
    @Column(name="participant_id", nullable=false)
    private String participantId;

    @Basic(optional=false)
    @Column(name="participant_display_name", nullable=false)
    private String participantDisplayName;

    @Basic(optional=false)
    @Column(name="participant_thumbnail_url", nullable=false)
    private String participantThumbnailUrl;

    @Basic(optional=false)
    @Column(name="sharedDataKey", nullable=false)
    private String sharedDataKey;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
    public Object getId()
    {
        return id;
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
