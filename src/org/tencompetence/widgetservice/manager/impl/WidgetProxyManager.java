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
package org.tencompetence.widgetservice.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.Whitelist;
import org.tencompetence.widgetservice.manager.IWidgetProxyManager;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.IDBManager;

/**
 * A class to manage the whitelist available to the proxy service
 * @author Paul Sharples
 * @version $Id: WidgetProxyManager.java,v 1.2 2009-05-01 10:40:09 ps3com Exp $
 *
 */
public class WidgetProxyManager implements IWidgetProxyManager {
	
	static Logger _logger = Logger.getLogger(WidgetProxyManager.class.getName());
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetProxyManager#getWhiteList()
	 */
	public synchronized Whitelist[] getWhiteList(){
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			List<?> sqlReturnList = dbManager.createQuery("from Whitelist").list();		
			Whitelist[] whitelist = sqlReturnList.toArray(new Whitelist[sqlReturnList.size()]);
			return whitelist;
		} 
		catch (Exception ex) {
			dbManager.rollbackTransaction();
			_logger.error(ex.getMessage());
			return null;
		}	
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetProxyManager#isAllowed(java.lang.String)
	 */
	public boolean isAllowed(String aUrl){					
		for (Whitelist whiteList : getWhiteList()){
			// TODO - make this better then just comparing the beginning...
			if(aUrl.toLowerCase().startsWith(whiteList.getfUrl().toLowerCase()))			
				return true;
		}
		return false;		
	}
}