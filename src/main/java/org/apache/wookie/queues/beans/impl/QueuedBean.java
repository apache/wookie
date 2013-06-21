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
package org.apache.wookie.queues.beans.impl;

import org.apache.log4j.Logger;
import org.apache.wookie.queues.beans.IQueuedBean;
/**
 * QueuedBean - model the values needed by the consumer thread to update the database
 * 
 * @author Paul Sharples
 *
 */
public class QueuedBean implements IQueuedBean {
	
	static Logger logger = Logger.getLogger(QueuedBean.class);
	// instance key
	protected String id_key;
	// key to be updated
	protected String key;
	// value to update with
	protected String value;
	// should it be appended?
	protected boolean append;
	
	public QueuedBean(String idKey, String key, String value, boolean append) {
		super();
		id_key = idKey;
		this.key = key;
		this.value = value;
		this.append = append;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.beans.IQueuedBean#getId_key()
	 */
	public String getId_key() {
		return id_key;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.beans.IQueuedBean#getKey()
	 */
	public String getKey() {
		return key;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.beans.IQueuedBean#getValue()
	 */
	public String getValue() {
		return value;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.beans.IQueuedBean#append()
	 */
	public boolean append() {
		return append;
	}

}
