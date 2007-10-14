package org.tencompetence.widgetservice.beans;

//import java.sql.Blob;

public class Preference extends AbstractKeyBean {

	private static final long serialVersionUID = 1L;
	
	public Preference(){}
		
	private WidgetInstance widgetInstance;
	private String dkey;
	private String dvalue;
	public WidgetInstance getWidgetInstance() {
		return widgetInstance;
	}
	public void setWidgetInstance(WidgetInstance widgetInstance) {
		this.widgetInstance = widgetInstance;
	}
	public String getDkey() {
		return dkey;
	}
	public void setDkey(String dkey) {
		this.dkey = dkey;
	}
	public String getDvalue() {
		return dvalue;
	}
	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}

}