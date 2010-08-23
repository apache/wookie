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

package org.apache.wookie.beans.jcr;

/**
 * IPathBean - base JCR bean with node path.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IPathBean
{
    /**
     * Get JCR node path.
     * 
     * @return node path
     */
    String getNodePath();

    /**
     * Set JCR node path if not previously set.
     *
     * @param nodeRootPath node root path for instance class
     * @return new or exiting node path
     */
    String setNodePath(String nodeRootPath);
}
