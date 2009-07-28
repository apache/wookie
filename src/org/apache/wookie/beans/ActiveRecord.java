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
 * @version $Id: ActiveRecord.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $ 
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
