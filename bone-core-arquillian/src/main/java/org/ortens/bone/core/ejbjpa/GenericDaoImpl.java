/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.ejbjpa;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.ortens.bone.core.model.BaseEntity;

/**
 *
 * @author canatac
 */
public abstract class GenericDaoImpl<T> implements GenericDao<T> {
    public static Logger _logger = Logger
			.getLogger(GenericDaoImpl.class.getName());
    
    
    @PersistenceContext
    EntityManager em;
    
    private Class<T> type;
    
    public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }
    
    @Override
    public abstract void move(T entityToMove, T entityFROM, T entityTO);
}
