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
package org.tencompetence.widgetservice.util.hibernate.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.tencompetence.widgetservice.beans.AbstractKeyBean;
import org.tencompetence.widgetservice.util.hibernate.DBManagerInterface;
import org.tencompetence.widgetservice.util.hibernate.HibernateUtil;

/**
 * @author sheyenrath
 *
 * @version $Id: DBManagerImpl.java,v 1.2 2007-10-17 23:11:12 ps3com Exp $
 */
public class DBManagerImpl implements DBManagerInterface {

	/**
	 * The session.
	 */
	private Session session = null;
	
	/**
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#beginTransaction()
	 */
	public void beginTransaction() {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#commitTransaction()
	 */
	public void commitTransaction() {
		if (session.isOpen()) {
			session.getTransaction().commit();
		}
	}
	
	/** (non-Javadoc)
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#rollbackTransaction()
	 */
	public void rollbackTransaction() {
		session.getTransaction().rollback();
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#closeSession()
	 */
	public void closeSession() {
		session.flush();
		session.close();
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#createCriteria(java.lang.Class)
	 */
	public <T extends AbstractKeyBean> Criteria createCriteria(final Class<? extends AbstractKeyBean> baseClass) {
		return session.createCriteria(baseClass);
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#createQuery(java.lang.String)
	 */
	public Query createQuery(final String query) {
		return session.createQuery(query);
	}
	
	public SQLQuery createSQLQuery(final String query){
		return session.createSQLQuery(query);
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#getObject(java.lang.Class, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractKeyBean> T getObject(final Class<T> baseClass,
			final Integer id) throws Exception {

		try {
			return (T) session.get(baseClass, id, LockMode.READ);
		} catch (HibernateException he) {
			// Critical error like table is not present.
			throw new Exception(he);
		}
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#getObject(java.lang.Class, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractKeyBean> T getObject(final Class<T> baseClass,
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
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#getObjects(java.lang.Class, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractKeyBean> List<T> getObjects(final Class<T> baseClass,
			final Criteria criteria) throws Exception {

		try {
			return (List<T>) criteria.list();
			
		} catch (HibernateException he) {
			throw new Exception(he);
		}
	}
	
	/**
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#updateObject(java.lang.Class, java.lang.Object)
	 */
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
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#saveObject(org.tencompetence.tencs.business.database.beans.DBAbstractBean)
	 */
	public Serializable saveObject(final AbstractKeyBean obj) throws Exception {
		
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
	 * @see org.tencompetence.tencs.business.database.DBManagerInterface#deleteObject(org.tencompetence.tencs.business.database.beans.DBAbstractBean)
	 */
	public void deleteObject(final AbstractKeyBean obj) throws Exception {
		
		try {
			session.delete(obj);
		} catch (HibernateException he) {
			throw new Exception(he);
		}
	}

}
