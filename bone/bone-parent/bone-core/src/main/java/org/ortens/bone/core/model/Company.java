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
public class Company extends BaseEntity implements Serializable{

    @Override
    public String getDisplayText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
