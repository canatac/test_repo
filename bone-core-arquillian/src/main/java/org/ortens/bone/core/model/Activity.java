package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

import org.ortens.bone.core.model.BaseEntity;

@Entity
public class Activity extends BaseEntity implements Serializable{

	@Override
	public String getDisplayText() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	private String	clientId;
	private String	employeeId;
	private String	description; 
	private String	label;
	private Date	date;
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	

}
