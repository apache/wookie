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

package org.apache.wookie.util.hibernate.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.wookie.beans.AbstractKeyBean;
import org.apache.wookie.beans.ActiveRecord;
import org.apache.wookie.util.hibernate.HibernateUtil;
import org.apache.wookie.util.hibernate.IDBManager;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 * @author sheyenrath
 *
 * @version $Id: DBManagerImpl.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
 */
public class DBManagerImpl implements IDBManager {

	/**
	 * The session.
	 */
	private Session session = null;
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#beginTransaction()
	 */
	public void beginTransaction() {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#commitTransaction()
	 */
	public void commitTransaction() {
		if (session.isOpen()) {
			session.getTransaction().commit();
		}
	}
	
	/** (non-Javadoc)
	 * @see org.tencompetence.tencs.business.database.IDBManager#rollbackTransaction()
	 */
	public void rollbackTransaction() {
		session.getTransaction().rollback();
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#closeSession()
	 */
	public void closeSession() {
		if (session.isOpen()) {
			session.flush();
			session.close();
		}	
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#createCriteria(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> Criteria createCriteria(final Class<? extends ActiveRecord> baseClass) {
		return session.createCriteria(baseClass);
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#createQuery(java.lang.String)
	 */
	public Query createQuery(final String query) {
		return session.createQuery(query);
	}
	
	public SQLQuery createSQLQuery(final String query){
		return session.createSQLQuery(query);
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#getObject(java.lang.Class, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> T getObject(final Class<T> baseClass,
			final Integer id) throws Exception {

		try {
			return (T) session.get(baseClass, id, LockMode.READ);
		} catch (HibernateException he) {
			// Critical error like table is not present.
			throw new Exception(he);
		}
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#getObject(java.lang.Class, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> T getObject(final Class<T> baseClass,
			final Criteria criteria) throws Exception {

		try {
			return (T) criteria.uniqueResult();
			
		} catch (HibernateException he) {
			/* 
			 * Critical error like table is not present or when
			 * more then 1 result is found using unique result.
			 */
			throw new Exception(he);
		}
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#getObjects(java.lang.Class, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> List<T> getObjects(final Class<T> baseClass,
			final Criteria criteria) throws Exception {
		try {
			return (List<T>) criteria.list();
			
		} catch (HibernateException he) {
			throw new Exception(he);
		}
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#updateObject(java.lang.Class, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractKeyBean> void updateObject(final Class<T> baseClass,
			final AbstractKeyBean changedObject) throws Exception {
	
		final T getObject = getObject(baseClass, changedObject.getId());
		
		if (getObject == null) {
			throw new Exception("null");
		}
		
		try {
			session.saveOrUpdate(getObject);
			
		} catch (HibernateException he) {
			throw new Exception(he);
		}
       
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#saveObject(org.tencompetence.tencs.business.database.beans.DBAbstractBean)
	 */
	@SuppressWarnings("unchecked")
	public Serializable saveObject(final ActiveRecord obj) throws Exception {
		
		try {
			return session.save(obj);
			
		} catch (HibernateException he) {
			throw new Exception(he);
		}
	}
	
	public Serializable saveGenericObject(final Object obj) throws Exception {
		
		try {
			return session.save(obj);
			
		} catch (HibernateException he) {
			throw new Exception(he);
		}
	}

	/**
	 * @see org.tencompetence.tencs.business.database.IDBManager#deleteObject(org.tencompetence.tencs.business.database.beans.DBAbstractBean)
	 */
	@SuppressWarnings("unchecked")
	public void deleteObject(final ActiveRecord obj) throws Exception {
		
		try {
			session.delete(obj);			
		} catch (HibernateException he) {
			throw new Exception(he);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.util.hibernate.IDBManager#deleteGenericObject(java.lang.Object)
	 */
	public void deleteGenericObject(Object obj) throws Exception {
		try {
			session.delete(obj);			
		} catch (HibernateException he) {
			throw new Exception(he);
		}
	}

}
