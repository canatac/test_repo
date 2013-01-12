/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.ejbjpa;

import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.ortens.bone.core.model.GenericEntity;

/**
 *
 * @author canatac
 */
public class GenericEntityDaoImpl extends GenericDaoImpl<GenericEntity> implements GenericEntityDao{
    @PersistenceContext
    EntityManager em;
 
    @Override
    public void move(GenericEntity entityToMove, GenericEntity entityFROM, GenericEntity entityTO) {
        Iterator<GenericEntity> it = entityFROM.getChildren(entityToMove).iterator();
		String entityToMoveDescription = entityToMove.getDescription();
		
		while (it.hasNext()) {
			//_logger.info("change.getDescription() : " + it.next().getDescription());
			if (entityToMoveDescription.equals(it.next().getDescription())) {
				_logger.info(entityToMove.getClass().getSimpleName()+" treated: "+entityToMoveDescription);
				it.remove();
				_logger.info("					|__ "+entityToMoveDescription+"  : REMOVED !");
			}
		}
		//((Livraison) entityTO).getChangements().add((Changement) entityToMove);
		entityTO.getChildren(entityToMove).add(entityToMove);
		em.flush();
		em.persist(entityFROM);
		em.persist(entityTO);
    }

    @Override
    public List<GenericEntity> getList(GenericEntity entity) {
        String entityName = entity.getClass().getSimpleName();
		// given
		String fetchingAllEntitiesInJpql = "select e from "+entityName+" e order by e.id";
		
		
		// when
		_logger.info("Selecting (using JPQL)...");
		_logger.info("						...>>>> "+fetchingAllEntitiesInJpql);
		List<GenericEntity> entities = em.createQuery(
				fetchingAllEntitiesInJpql, GenericEntity.class).getResultList();

		// then
		_logger.info("Found " + entities.size()
				+ " "+entityName+" (using JPQL):");

		return entities;
    }
    
}
