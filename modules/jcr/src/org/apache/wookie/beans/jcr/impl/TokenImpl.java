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

import org.apache.wookie.beans.IToken;
import org.apache.wookie.beans.jcr.IIdElement;

/**
 * TokenImpl - JCR OCM IToken implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:token", discriminator=false)
public class TokenImpl implements IToken, IIdElement
{
    @Field(id=true, jcrName="wookie:elementId")
    private long elementId = -1;
    
    @Field(jcrName="wookie:requestUrl")
    private String requestUrl;

    @Field(jcrName="wookie:accessUrl")
    private String accessUrl;

    @Field(jcrName="wookie:authzUrl")
    private String authzUrl;

    @Field(jcrName="wookie:requestToken")
    private String requestToken;

    @Field(jcrName="wookie:accessToken")
    private String accessToken;

    @Field(jcrName="wookie:tokenSecret")
    private String tokenSecret;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getAccessToken()
     */
    public String getAccessToken()
    {
        return accessToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setAccessToken(java.lang.String)
     */
    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getAccessUrl()
     */
    public String getAccessUrl()
    {
        return accessUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setAccessUrl(java.lang.String)
     */
    public void setAccessUrl(String accessUrl)
    {
        this.accessUrl = accessUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getAuthzUrl()
     */
    public String getAuthzUrl()
    {
        return authzUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setAuthzUrl(java.lang.String)
     */
    public void setAuthzUrl(String authzUrl)
    {
        this.authzUrl = authzUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IIdElement#getElementId()
     */
    public long getElementId()
    {
        return elementId;
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IIdElement#setElementId(long)
     */
    public void setElementId(long elementId)
    {
        this.elementId = elementId;
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getRequestToken()
     */
    public String getRequestToken()
    {
        return requestToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setRequestToken(java.lang.String)
     */
    public void setRequestToken(String requestToken)
    {
        this.requestToken = requestToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getRequestUrl()
     */
    public String getRequestUrl()
    {
        return requestUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setRequestUrl(java.lang.String)
     */
    public void setRequestUrl(String requestUrl)
    {
        this.requestUrl = requestUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getTokenSecret()
     */
    public String getTokenSecret()
    {
        return tokenSecret;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setTokenSecret(java.lang.String)
     */
    public void setTokenSecret(String tokenSecret)
    {
        this.tokenSecret = tokenSecret;
    }
}
