/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.model;

import java.io.Serializable;
import javax.persistence.Entity;

import org.ortens.bone.core.model.BaseEntity;

/**
 *
 * @author canatac
 */
@Entity
public class Problem extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1516628698364307231L;
	private String displayText;
    
    public String getDisplayText() {
        System.out.println("Problem.getDisplayText()");
        return displayText;
    }    

    public void setDisplayText(String text){
    	System.out.println("Problem.setDisplayText()");
    	this.displayText = text;
    }
    
}
