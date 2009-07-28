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

package org.apache.wookie.util.hibernate;

import org.apache.wookie.util.hibernate.impl.DBManagerImpl;

/**
 * @author sheyenrath
 *
 * @version $Id: DBManagerFactory.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
 */
public class DBManagerFactory {

	public static final ThreadLocal<DBManagerImpl> threadSession = new ThreadLocal<DBManagerImpl>();

	/**
	 * @return DBManagerImplementation
	 */
	public static IDBManager getDBManager() {

		DBManagerImpl manager = (DBManagerImpl) threadSession.get();
		// Create a new DbManager, if this Thread has none yet
		if (manager == null) {
			manager = new DBManagerImpl();
			threadSession.set(manager);
		}
		return manager;
	}
	

}
