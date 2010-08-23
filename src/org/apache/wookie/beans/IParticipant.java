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

/**
 * IParticipant - a participant entity.
 * 
 * @author Scott Wilson
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IParticipant extends IBean
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
     * Get owning widget instance.
     * 
     * @return widget instance
     */
    IWidget getWidget();

    /**
     * Set owning widget instance.
     * 
     * @param widget widget instance
     */
    void setWidget(IWidget widget);

    /**
     * Get participant id.
     * 
     * @return participant id
     */
    String getParticipantId();
    
    /**
     * Set participant id.
     * 
     * @param participantId participant id
     */
    void setParticipantId(String participantId);
    
    /**
     * Get participant display name.
     * 
     * @return participant display name
     */
    String getParticipantDisplayName();
    
    /**
     * Set participant display name.
     * 
     * @param participantDisplayName participant display name
     */
    void setParticipantDisplayName(String participantDisplayName);
    
    /**
     * Get participant thumbnail URL.
     * 
     * @return participant thumbnail URL
     */
    String getParticipantThumbnailUrl();
    
    /**
     * Set participant thumbnail URL.
     * 
     * @param participantThumbnailUrl participant thumbnail URL
     */
    void setParticipantThumbnailUrl(String participantThumbnailUrl);
}
