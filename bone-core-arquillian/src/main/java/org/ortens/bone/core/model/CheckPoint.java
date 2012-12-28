package org.ortens.bone.core.model;

import java.io.Serializable;

import javax.persistence.Entity;

import org.ortens.bone.core.model.BaseEntity;

@Entity
public class CheckPoint extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3228476214430949468L;
	private String displayText;
    
    public String getDisplayText() {
        System.out.println("CheckPoint.getDisplayText()");
        return displayText;
    }    

    public void setDisplayText(String text){
    	System.out.println("CheckPoint.setDisplayText()");
    	this.displayText = text;
    }

	private String label;

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	
}
