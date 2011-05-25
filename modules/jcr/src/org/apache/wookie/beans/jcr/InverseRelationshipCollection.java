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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

/**
 * InverseRelationshipCollection - collection that manages inverse relationships.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class InverseRelationshipCollection<O extends IUuidBean,I extends IInverseRelationship<O>,M> extends AbstractCollection<M>
{
    private O owningObject;
    private Collection<I> inverseCollection;

    /**
     * Construct managing collection.
     * 
     * @param owningObject owning object
     * @param inverseCollection managed inverse collection
     */
    public InverseRelationshipCollection(O owningObject, Collection<I> inverseCollection)
    {
        super();
        this.owningObject = owningObject;
        this.inverseCollection = inverseCollection;
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#add(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public boolean add(M member)
    {
        I inverse = (I)member;
        inverse.updateInverseRelationship(owningObject);
        return inverseCollection.add(inverse);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#iterator()
     */
    public Iterator<M> iterator()
    {
        return new Iterator<M>()
        {
            Iterator<I> inverseIter = inverseCollection.iterator();
            I lastInverse = null;
            
            /* (non-Javadoc)
             * @see java.util.Iterator#hasNext()
             */
            public boolean hasNext()
            {
                return inverseIter.hasNext();
            }

            /* (non-Javadoc)
             * @see java.util.Iterator#next()
             */
            @SuppressWarnings("unchecked")
            public M next()
            {
                lastInverse = inverseIter.next();
                M member = (M)lastInverse;
                return member;
            }

            /* (non-Javadoc)
             * @see java.util.Iterator#remove()
             */
            public void remove()
            {
                inverseIter.remove();
                lastInverse.updateInverseRelationship(null);
            }
        };
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#remove(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public boolean remove(Object member)
    {
        I inverse = (I)member;
        if (inverseCollection.remove(inverse))
        {
            inverse.updateInverseRelationship(null);
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#size()
     */
    public int size()
    {
        return inverseCollection.size();
    }  
}
