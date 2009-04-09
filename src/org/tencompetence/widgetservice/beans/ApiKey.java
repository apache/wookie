/**
 * 
 */
package org.tencompetence.widgetservice.beans;

/**
 * @author scott
 *
 */
public class ApiKey extends AbstractKeyBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @return the key
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param key the key to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	private String value;
	private String email;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
