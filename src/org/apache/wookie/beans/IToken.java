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
 * IToken - a widget instance token.
 * 
 * @author Scott Wilson
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IToken
{
    /**
     * Get widget instance token request URL.
     * 
     * @return request URL
     */
    String getRequestUrl();
    
    /**
     * Set widget instance token request URL.
     * 
     * @param requestUrl request URL
     */
    void setRequestUrl(String requestUrl);
    
    /**
     * Get widget instance token access URL.
     * 
     * @return access URL
     */
    String getAccessUrl();
    
    /**
     * Set widget instance token access URL.
     * 
     * @param accessUrl access URL
     */
    void setAccessUrl(String accessUrl);
    
    /**
     * Get widget instance token authorization URL.
     * 
     * @return authorization URL
     */
    String getAuthzUrl();
    
    /**
     * Set widget instance token authorization URL.
     * 
     * @param authzUrl authorization URL
     */
    void setAuthzUrl(String authzUrl);
    
    /**
     * Get widget instance token request token.
     * 
     * @return request token
     */
    String getRequestToken();
    
    /**
     * Set widget instance token request token.
     * 
     * @param requestToken request token
     */
    void setRequestToken(String requestToken);
    
    /**
     * Get widget instance token access token.
     * 
     * @return access token
     */
    String getAccessToken();
    
    /**
     * Set widget instance token access token.
     * 
     * @param accessToken access token
     */
    void setAccessToken(String accessToken);
    
    /**
     * Get widget instance token token secret.
     * 
     * @return token secret
     */
    String getTokenSecret();
    
    /**
     * Set widget instance token token secret.
     * 
     * @param tokenSecret token secret
     */
    void setTokenSecret(String tokenSecret);
}
