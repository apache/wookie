package org.tencompetence.widgetservice.beans;

public class SharedData extends AbstractKeyBean {

	private static final long serialVersionUID = 1L;

	public SharedData() {}
	
	private String sharedDataKey;
	private String dkey;
	private String dvalue;	

	public String getDkey() {
		return dkey;
	}
	public void setDkey(String dkey) {
		this.dkey = dkey;
	}
	public String getDvalue() {
		return dvalue;
	}
	public String getSharedDataKey() {
		return sharedDataKey;
	}
	public void setSharedDataKey(String sharedDataKey) {
		this.sharedDataKey = sharedDataKey;
	}
	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}

	
	
}
