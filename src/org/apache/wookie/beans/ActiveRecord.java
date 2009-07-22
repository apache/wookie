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
package org.apache.wookie.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.wookie.util.hibernate.DBManagerFactory;
import org.apache.wookie.util.hibernate.IDBManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * ActiveRecord pattern implementation for beans 
 * @author Scott Wilson
 * @version $Id: ActiveRecord.java,v 1.1 2009-07-22 09:31:30 scottwilson Exp $ 
 */
public class ActiveRecord<T> implements Serializable{
	
	private static final long serialVersionUID = 8882549280324353036L;
	public static Logger _logger = Logger.getLogger(ActiveRecord.class.getName());
	
	@SuppressWarnings("unchecked")
	public static <T extends ActiveRecord> Object findById(Class<? extends ActiveRecord> clazz, Object id){
		Object[] objects = findByValue(clazz, "id", id);
		if (objects == null||objects.length!=1) return null;
		return objects[0];
	}
	
	@SuppressWarnings("unchecked")
	protected static <T extends ActiveRecord> Object[] findAll(Class<? extends ActiveRecord> clazz){
		return findByValues(clazz, null);
	}
	@SuppressWarnings("unchecked")	
	protected static <T extends ActiveRecord> Object[] findByValue(Class<? extends ActiveRecord> clazz, String key, Object value){
		final Map map = new HashMap<String, Object>();
		map.put(key, value);
		return findByValues(clazz, map);
	}
	@SuppressWarnings("unchecked")
	protected static <T extends ActiveRecord> Object[] findByValues(Class<? extends ActiveRecord> clazz, Map<String, Object> map){
		try {
			final IDBManager dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(clazz);
			if (map != null){
				Iterator<Entry<String, Object>> it = map.entrySet().iterator();

				while (it.hasNext()){
					Entry<String, Object> e = it.next();
					crit.add( Restrictions.eq( (String) e.getKey(), e.getValue() ) );				
				}
			}
			final List<? extends ActiveRecord> sqlReturnList =  dbManager.getObjects(clazz, crit);		
			return sqlReturnList.toArray((T[]) java.lang.reflect.Array.newInstance(clazz,sqlReturnList.size()));
		} catch (Exception e) {
			_logger.error(e.getMessage());
			return null;
		}
	}
	
	public static boolean delete(Object object) {
		if (object == null) return false;
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		try {
			dbManager.deleteGenericObject(object);
			return true;
		} catch (Exception e) {
			_logger.error(e.getMessage());
			return false;
		}
	}
	
	public boolean save() {
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		try {
			dbManager.saveGenericObject(this);
			return true;
		} catch (Exception e) {
			_logger.error(e.getMessage());
			return false;
		}	
	}
	
	public static void delete(Object[] objects) {
		if (objects!= null) for (Object object: objects) delete(object);
	}
	
	public boolean delete(){
		return delete(this);
	}
	
}
