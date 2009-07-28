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

import org.apache.log4j.Logger;
import org.apache.wookie.util.hibernate.DBManagerFactory;
import org.apache.wookie.util.hibernate.IDBManager;

/**
 * Filter to set DB transactions
 * @author Paul Sharples
 * @version $Id: MainFilter.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
 *
 */
public class MainFilter implements Filter {
	
	static final private Logger logger = Logger.getLogger(MainFilter.class);

	public void destroy() {
	}

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) 
	throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml;charset=\"UTF-8\"");
		/** Get a DBManager for this thread. */
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		try {							
			dbManager.beginTransaction();
			chain.doFilter(request, response);		
			dbManager.commitTransaction();
		}
		catch (Exception e) {
			logger.error("error: " + e.getCause());
			e.printStackTrace();
		}
		finally {
			// Close the session [This method checks if the session is open]
			dbManager.closeSession();
		}
	}

	public void init(FilterConfig arg0) throws ServletException {		
	}

}
