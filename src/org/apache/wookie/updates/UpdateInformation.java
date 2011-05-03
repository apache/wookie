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
package org.apache.wookie.updates;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.helpers.WidgetHelper;
import org.apache.wookie.w3c.updates.UpdateDescriptionDocument;
import org.jdom.Element;

/**
 * Simple class for passing update information, linking an IWidget reference to an UpdateDescriptionDocument
 */
public class UpdateInformation {

	private IWidget widget;
	
	private UpdateDescriptionDocument udd;

	/**
	 * @return the widget
	 */
	public IWidget getWidget() {
		return widget;
	}

	/**
	 * @param widget the widget to set
	 */
	public void setWidget(IWidget widget) {
		this.widget = widget;
	}

	/**
	 * @return the document
	 */
	public UpdateDescriptionDocument getUpdateDescriptionDocument() {
		return udd;
	}

	/**
	 * @param document the document to set
	 */
	public void setUpdateDescriptionDocument(UpdateDescriptionDocument udd) {
		this.udd = udd;
	}
	
	public Element toXml(){
		Element element = this.udd.toXml();
		element.setAttribute("widget", this.widget.getId().toString());
		element.setAttribute("widget_title", WidgetHelper.getEncodedWidgetTitle(this.widget, null));
		return element;
	}
	
}
