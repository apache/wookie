package org.tencompetence.widgetservice.manager;

import org.tencompetence.widgetservice.beans.Whitelist;

public interface IWidgetProxyManager {

	Whitelist[] getWhiteList();

	boolean isAllowed(String aUrl);

}