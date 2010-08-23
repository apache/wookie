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
import org.apache.wookie.beans.util.PersistenceCommitException;
import org.apache.wookie.beans.util.PersistenceManagerFactory;

/**
 * Filter to set DB transactions
 */
public class MainFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) 
	throws IOException, ServletException {
	    // Retry filter chain invocation once if persistence commit
	    // exception is caught and response is not committed. This
	    // is intended to help insulate both the server and the
	    // widgets from limited transactional race conditions from
	    // concurrent requests that attempt to side effect the same
	    // server state. Note that this will not protect the widget
	    // or server from more than two concurrent modification
	    // attempts or guarantee sequential operation order. 
	    boolean retryChainInvocation = false;
	    do {
	        // get persistence manager for this thread
	        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	        try {
	            // reset response and pause on retry to help ensure success
	            if (retryChainInvocation) {
	                response.reset();
	                try {
	                    Thread.sleep(50);
	                } catch (InterruptedException ie) {
	                }
	            }
	            // start and commit transaction around servlet invocation
	            persistenceManager.begin();
	            chain.doFilter(request, response);
	            persistenceManager.commit();
	            // terminate retry attempts on success
	            retryChainInvocation = false;
	        } catch (ServletException se) {
	            // rollback transaction on exception
	            persistenceManager.rollback();
	            throw se;
	        } catch (IOException ioe) {
	            // rollback transaction on exception
	            persistenceManager.rollback();
	            throw ioe;
	        } catch (PersistenceCommitException pce) {
                // rollback and retry on commit exception if response not committed	            
	            persistenceManager.rollback();
	            retryChainInvocation = (!retryChainInvocation && !response.isCommitted());
	            if (!retryChainInvocation) {
	                throw new RuntimeException("Persistence commit exception caught for transaction: "+pce, pce);	                
	            }
	        } catch (Throwable t) {
	            // rollback transaction on exception
	            persistenceManager.rollback();
	            throw new RuntimeException("Exception caught for transaction: "+t, t);
	        } finally {
	            // close thread persistence manager
	            PersistenceManagerFactory.closePersistenceManager();
	        }
	    } while (retryChainInvocation);
	}

	public void init(FilterConfig arg0) throws ServletException {		
	}

}
