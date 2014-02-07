package org.apache.wookie.services.impl;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.w3c.impl.WidgetEntity;
import org.jdom.Element;

public class DefaultWidgetImpl extends WidgetEntity implements IWidget{
	
	/**
	 * The path to the original .wgt package
	 */
	private String packagePath;

	@Override
	public Object getId() {
		return this.getIdentifier();
	}

	@Override
	public Element toXml() {
		return null;
	}

	@Override
	public String getPackagePath() {
		return this.packagePath;
	}

	@Override
	public void setPackagePath(String path) {
		this.packagePath = path;
	}

}
