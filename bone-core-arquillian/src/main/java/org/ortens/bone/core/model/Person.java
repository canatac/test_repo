/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author canatac
 */
@Entity
public class Person extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3270127104902998688L;
	private String displayText;
    
    public String getDisplayText() {
        
        return displayText;
    }    

    public void setDisplayText(String text){
    	
    	this.displayText = text;
    }
}
