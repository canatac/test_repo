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

import org.ortens.bone.core.model.BaseEntity;

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

	private String displayText;
    
    public String getDisplayText() {
        
        return displayText;
    }    

    public void setDisplayText(String text){
    	
    	this.displayText = text;
    }

}
