/**
 * 
 */
package org.tencompetence.widgetservice.beans;

/**
 * @author scott
 *
 */
public class PreferenceDefault extends AbstractKeyBean {

	private static final long serialVersionUID = 3036952538484789860L;

	private Widget widget;
	private String preference;
	private String value;

	public Widget getWidget(){
		return widget;
	}
	
	public void setWidget(Widget w){
		widget = w;
	}
	
	/**
	 * @return the preference
	 */
	public String getPreference() {
		return preference;
	}
	/**
	 * @param preference the preference to set
	 */
	public void setPreference(String preference) {
		this.preference = preference;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
