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

import java.io.Serializable;
import java.util.List;

import org.apache.wookie.beans.AbstractKeyBean;
import org.apache.wookie.beans.ActiveRecord;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * @author sheyenrath
 *
 * @version $Id: IDBManager.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
 */
public interface IDBManager {

	/**
	 * Create start a transaction.
	 */
	public void beginTransaction();

	/**
	 * Commit the transaction.
	 */
	public void commitTransaction();
	
	/**
	 * Rollback the transaction.
	 */
	public void rollbackTransaction();
	
	/**
	 * Close the session.
	 */
	public void closeSession();
	
	/**
	 * @param baseClass
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> Criteria createCriteria(final Class<? extends ActiveRecord> baseClass);
	
	/**
	 * @param baseClass
	 * @return Object
	 */
	public Query createQuery(final String query);

	public SQLQuery createSQLQuery(final String query);
	/**
	 * Get an object from the database based on key ID;
	 * 
	 * @param <T>
	 * @param baseClass
	 * @param id Identifier (key) from the obejct to search for.
	 * @return object (or null when not found)
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> T getObject(final Class<T> baseClass, final Integer id)
		throws Exception;

	/**
	 * Get an object from the database based on search criterion.
	 * 
	 * @param <T>
	 * @param baseClass
	 * @param criteria 
	 * @return object (or null when not found)
	 * @throws TENCDatabaseException
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> T getObject(final Class<T> baseClass,
			final Criteria criteria) throws Exception;
	
	/**
	 * Get a list from objects from the database based on search criterion.
	 * 
	 * @param <T>
	 * @param baseClass
	 * @param criteria
	 * @return A list from objects or an empty list when no objects are found.
	 * @throws TENCDatabaseException
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> List<T> getObjects(final Class<T> baseClass,
			final Criteria criteria) throws Exception;
	
	/**
	 * Update an object in the database.
	 * @param <T>
	 * @param baseClass
	 * @param object
	 * @throws TENCDatabaseException
	 * @throws TENCObjectNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractKeyBean> void updateObject(final Class<T> baseClass,
			final AbstractKeyBean object) throws Exception;
	
	/**
	 * Insert an object into the database.
	 * 
	 * @param object The object to save.
	 * @return
	 * @throws TENCServerException
	 */
	@SuppressWarnings("unchecked")
	public Serializable saveObject(final ActiveRecord object) throws Exception;
	
	public Serializable saveGenericObject(final Object obj) throws Exception;
	
	/**
	 * Deletes an object from the database.
	 * 
	 * @param object the object to delete.
	 * @throws TENCServerException
	 */
	@SuppressWarnings("unchecked")
	public void deleteObject(final ActiveRecord object) throws Exception;
	
	/**
	 * Deletes an object from the database
	 * @param obj
	 * @throws Exception
	 */
	public void deleteGenericObject(final Object obj) throws Exception;
	
}
