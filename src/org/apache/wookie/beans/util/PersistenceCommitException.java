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

package org.apache.wookie.beans.util;

/**
 * PersistenceCommitException - persistence exception during commit
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class PersistenceCommitException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public PersistenceCommitException()
    {
    }

    /**
     * Message constructor.
     * 
     * @param message exception message
     */
    public PersistenceCommitException(String message)
    {
        super(message);
    }

    /**
     * Cause constructor.
     * 
     * @param cause exception cause
     */
    public PersistenceCommitException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Message and cause constructor.
     * 
     * @param message exception message
     * @param cause exception cause
     */
    public PersistenceCommitException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
