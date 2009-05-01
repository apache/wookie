package org.tencompetence.widgetservice.manager;

import org.tencompetence.widgetservice.beans.Whitelist;

/**
 * Interface for proxy functions
 * 
 * @author Paul Sharples
 * @version $Id: IWidgetProxyManager.java,v 1.2 2009-05-01 10:40:09 ps3com Exp $
 *
 */
public interface IWidgetProxyManager {

	/**
	 * Return an array of Whitelist entries
	 * @return
	 */
	Whitelist[] getWhiteList();

	/**
	 * Check to see if a given url appears in the whitelist
	 * @param aUrl
	 * @return
	 */
	boolean isAllowed(String aUrl);

}