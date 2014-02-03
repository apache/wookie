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

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

/**
 * IdCollection - collection that manages collection element ids.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class IdCollection<I extends IIdElement,M> extends AbstractList<M> implements List<M>
{
    private List<I> managedCollection;
    
    public IdCollection(List<I> managedList){
    	this.managedCollection = managedList;
    }
    
	/* (non-Javadoc)
     * @see java.util.AbstractCollection#add(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public boolean add(M member)
    {
        long maxElementId = -1;
        for (I idElement : managedCollection)
        {
            if (idElement.getElementId() > maxElementId)
            {
                maxElementId = idElement.getElementId();
            }
        }
        I idElement = (I)member;
        idElement.setElementId(maxElementId+1);
        return managedCollection.add(idElement);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#iterator()
     */
    public Iterator<M> iterator()
    {
        return new Iterator<M>()
        {
            Iterator<I> idElementIter = managedCollection.iterator();
            I lastIdElement = null;
            
            /* (non-Javadoc)
             * @see java.util.Iterator#hasNext()
             */
            public boolean hasNext()
            {
                return idElementIter.hasNext();
            }

            /* (non-Javadoc)
             * @see java.util.Iterator#next()
             */
            @SuppressWarnings("unchecked")
            public M next()
            {
                lastIdElement = idElementIter.next();
                M member = (M)lastIdElement;
                return member;
            }

            /* (non-Javadoc)
             * @see java.util.Iterator#remove()
             */
            public void remove()
            {
                idElementIter.remove();
                lastIdElement.setElementId(-1);
            }
        };
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#remove(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public boolean remove(Object member)
    {
        I idElement = (I)member;
        if (managedCollection.remove(idElement))
        {
            idElement.setElementId(-1);
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.util.AbstractCollection#size()
     */
    public int size()
    {
        return managedCollection.size();
    }

	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public M get(int arg0) {
		return (M) managedCollection.get(arg0);
	}    
}
