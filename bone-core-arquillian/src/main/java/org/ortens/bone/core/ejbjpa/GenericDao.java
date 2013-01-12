/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.ejbjpa;

import java.util.List;


/**
 *
 * @author canatac
 */
public interface GenericDao<T> {
    public void move(T entityToMove, T entityFROM, T entityTO);
    
    public List<T> getList(T entityClass);
}
