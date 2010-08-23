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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * IAccessRequest - access request control for a particular widget.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IAccessRequest extends IBean
{
    /**
     * Get widget access granted flag.
     * 
     * @return access granted flag
     */
    boolean isGranted();
    
    /**
     * Set widget access granted flag.
     * 
     * @param granted access granted flag
     */
    void setGranted(boolean granted);
    
    /**
     * Get widget access origin.
     * 
     * @return access origin
     */
    String getOrigin();
    
    /**
     * Set widget access origin.
     * 
     * @param origin access origin
     */
    void setOrigin(String origin);
    
    /**
     * Get widget access subdomains flag.
     * 
     * @return access subdomains flag
     */
    boolean isSubdomains();
    
    /**
     * Set widget access subdomains flag.
     * 
     * @param subdomains access subdomains flag
     */
    void setSubdomains(boolean subdomains);

    /**
     * Get default widget instance.
     * 
     * @return widget instance
     */
    IWidget getWidget();

    /**
     * Set default widget instance.
     * 
     * @param widget widget instance
     */
    void setWidget(IWidget widget);
    
    /**
     * Implementation of the W3C WARP algorithm for a single access request.
     * 
     * @param requestedUri the URI requested
     * @return true if this access request grants access, otherwise false
     */
    boolean isAllowed(URI requestedUri);
    
    /**
     * Shared implementation utilities.
     */
    public static class Utilities
    {
        /**
         * Implementation of the W3C WARP algorithm for a single access request.
         * 
         * @param requestedUri the URI requested
         * @return true if this access request grants access, otherwise false
         */
        public static boolean isAllowed(IAccessRequest accessRequest, URI requestedUri)
        {
            // check origin wildcard
            String origin = accessRequest.getOrigin();
            if (origin.equals("*"))
            {
                return true;
            }
            // origins other than "*" MUST be valid URIs
            URI accessUri = null;
            try
            {
                accessUri = new URI(origin);
            }
            catch (URISyntaxException e)
            {
                return false;
            }
            // schemes must match
            if (!accessUri.getScheme().equalsIgnoreCase(requestedUri.getScheme()))
            {
                return false;                
            }
            // check host/subdomain
            boolean subdomains = accessRequest.isSubdomains();
            if (subdomains)
            {
                // host must match or match with subdomains
                if (!accessUri.getHost().equalsIgnoreCase(requestedUri.getHost()) &&
                    !requestedUri.getHost().endsWith("."+accessUri.getHost()))
                {
                    return false;
                }
            }
            else
            {
                // hosts must match
                if (!accessUri.getHost().equalsIgnoreCase(requestedUri.getHost()))
                {
                    return false;
                }
            }
            // ports must match
            if (accessUri.getPort()==requestedUri.getPort() || requestedUri.getPort() == -1 && accessUri.getPort() == 80)
            {
                return true;
            }
            // no match
            return false;
        }
    }
}
