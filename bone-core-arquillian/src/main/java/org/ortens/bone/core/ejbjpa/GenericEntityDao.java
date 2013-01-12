/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.ejbjpa;

import java.util.List;
import org.ortens.bone.core.model.BaseEntity;
import org.ortens.bone.core.model.GenericEntity;


/**
 *
 * @author canatac
 */
public interface GenericEntityDao extends GenericDao<GenericEntity>{
    @Override
    public void move(GenericEntity entityToMove, GenericEntity entityFROM, GenericEntity entityTO);
    
    @Override
    public List<GenericEntity> getList(GenericEntity entityClass);
    
}
