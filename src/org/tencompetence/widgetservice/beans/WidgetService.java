package org.tencompetence.widgetservice.beans;

/**
 * A type of service - a service provided in the system
 * @author paul
 *
 */
public class WidgetService extends AbstractKeyBean {
	
	private static final long serialVersionUID = 1L;
	
	private String serviceName;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
