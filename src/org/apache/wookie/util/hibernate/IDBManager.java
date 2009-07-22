/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 * @version $Id: IDBManager.java,v 1.1 2009-07-22 09:31:30 scottwilson Exp $
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
