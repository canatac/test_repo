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
public class Solution extends BaseEntity implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = -2544710073604384858L;
	private String displayText;
    
    public String getDisplayText() {
        System.out.println("Solution.getDisplayText()");
        return displayText;
    }    

    public void setDisplayText(String text){
    	System.out.println("Solution.setDisplayText()");
    	this.displayText = text;
    }
    
}
