package org.tencompetence.widgetservice.util.hibernate;

import org.tencompetence.widgetservice.util.hibernate.impl.DBManagerImpl;


public class DBManagerFactory {

	public static final ThreadLocal<DBManagerImpl> threadSession = new ThreadLocal<DBManagerImpl>();

	/**
	 * @return DBManagerImplementation
	 */
	public static DBManagerInterface getDBManager() {

		DBManagerImpl manager = (DBManagerImpl) threadSession.get();
		// Create a new DbManager, if this Thread has none yet
		if (manager == null) {
			manager = new DBManagerImpl();
			threadSession.set(manager);
		}
		return manager;
	}
	

}
