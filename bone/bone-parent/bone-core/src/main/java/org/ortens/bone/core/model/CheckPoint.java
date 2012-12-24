package org.ortens.bone.core.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class CheckPoint extends BaseEntity implements Serializable{

	@Override
	public String getDisplayText() {
		// TODO Auto-generated method stub
		return null;
	}

	private String label;
	private String description;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
