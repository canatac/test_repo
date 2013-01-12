/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.ejbjpa;

import org.ortens.bone.core.model.BaseEntity;


/**
 *
 * @author canatac
 */
public interface BaseEntityDao extends GenericDao<BaseEntity>{
    @Override
    public void move(BaseEntity o1, BaseEntity o2, BaseEntity o3);
}
