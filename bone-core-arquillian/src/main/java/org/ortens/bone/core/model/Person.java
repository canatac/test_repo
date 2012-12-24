/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.model;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author canatac
 */
@Entity
public class Person extends BaseEntity implements Serializable{

	private String description;
	
    @Override
    public String getDisplayText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	public String getDescription() {
		System.out.println("PersonTRUE-getter");
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		System.out.println("PersonTRUE");
	}
    
    
}
