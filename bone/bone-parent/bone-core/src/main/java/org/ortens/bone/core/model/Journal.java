/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * 
 * @author canatac
 */
@Entity
public class Journal extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5286890048573502363L;
	private static final int DESCRIPTION_COLUMN_LENGTH = 25;
	private static final int DESCRIPTION_MAX_COLUMN_SIZE = 25;


	@Column(length = DESCRIPTION_COLUMN_LENGTH, nullable = false)
	@Size(max = DESCRIPTION_MAX_COLUMN_SIZE)
	private String description;

	private Date entryDate;
	
	@Override
	public String getDisplayText() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
