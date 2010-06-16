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
 * limitations under the License.
 */

package org.apache.wookie.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;

/**
 * Filter to set DB transactions
 */
public class MainFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) 
	throws IOException, ServletException {
		// get persistence manager for this thread
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	    try {
	        // start and commit transaction around servlet invocation
			persistenceManager.begin();
			chain.doFilter(request, response);
			persistenceManager.commit();
	    } catch (Throwable t) {
	        // rollback transaction on exception
            persistenceManager.rollback();
            throw new RuntimeException();
	    } finally {
            // close thread persistence manager
	        PersistenceManagerFactory.closePersistenceManager();
	    }
	}

	public void init(FilterConfig arg0) throws ServletException {		
	}

}
