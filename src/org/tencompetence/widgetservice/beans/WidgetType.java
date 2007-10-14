package org.tencompetence.widgetservice.beans;

public class WidgetType extends AbstractKeyBean {

	private static final long serialVersionUID = 1L;
	
	private Widget widget;
	private String widgetContext;

	public String getWidgetContext() {
		return widgetContext;
	}

	public void setWidgetContext(String widgetContext) {
		this.widgetContext = widgetContext;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}


	
	
}
