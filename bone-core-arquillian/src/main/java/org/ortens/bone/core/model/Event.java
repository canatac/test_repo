/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.model;

import java.io.Serializable;

import javax.persistence.Transient;

/**
 *
 * @author canatac
 */
public class Event extends BaseEntity implements Serializable{

	@Override
	@Transient
	public String getDisplayText() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
