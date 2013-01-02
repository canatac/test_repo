package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.ortens.bone.core.model.BaseEntity;

@Entity
public class Activity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6652125573817337161L;

	private String displayText;
    
    public String getDisplayText() {
        
        return displayText;
    }    

    public void setDisplayText(String text){
    	
    	this.displayText = text;
    }
	
	private String	clientId;
	private String	employeeId;
	private String	label;
	
	
	//@MapKeyTemporal(value = null)
	
	private Date	prestationDate;
	
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

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPrestationDate() {
		return prestationDate;
	}
	public void setPrestationDate(Date prestationDate) {
		this.prestationDate = prestationDate;
	}
}