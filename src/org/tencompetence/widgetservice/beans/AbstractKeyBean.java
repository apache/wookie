package org.tencompetence.widgetservice.beans;

import java.io.Serializable;

public class AbstractKeyBean implements Serializable{

	private static final long serialVersionUID = -6480009363953386701L;

	private Integer id;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}
}
