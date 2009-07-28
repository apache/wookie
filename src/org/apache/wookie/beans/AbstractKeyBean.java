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

import org.apache.wookie.util.hibernate.DBManagerFactory;
import org.apache.wookie.util.hibernate.IDBManager;

/**
 * Basic id key functionality of a bean to be modelled in the DB  
 * @author Paul Sharples
 * @author sheyenrath
 * @version $Id: AbstractKeyBean.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public class AbstractKeyBean<T> extends ActiveRecord<T> implements Serializable{

	private static final long serialVersionUID = -6480009363953386701L;

	private Integer id;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}
	
	//// active record methods
	
	@SuppressWarnings("unchecked")
	public static boolean  delete(AbstractKeyBean object) {
		if (object == null) return false;
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		try {
			dbManager.deleteObject(object);
			return true;
		} catch (Exception e) {
			_logger.error(e.getMessage());
			return false;
		}
	}
	
	public boolean save(){
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		try {
			dbManager.saveObject(this);
			return true;
		} catch (Exception e) {
			_logger.error(e.getMessage());
			return false;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public static void delete(AbstractKeyBean[] objects) {
		if (objects != null)
			for (AbstractKeyBean object: objects) delete(object);
	}
	
}
