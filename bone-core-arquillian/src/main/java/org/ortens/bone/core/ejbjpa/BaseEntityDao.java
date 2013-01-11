package org.ortens.bone.core.ejbjpa;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ortens.bone.core.model.BaseEntity;


public abstract class BaseEntityDao {
	public static Logger _logger = Logger
			.getLogger(BaseEntityDao.class.getName());

	@PersistenceContext
	static
	EntityManager em;
	
	public static List<BaseEntity> getList(BaseEntity entity) {
		String entityName = entity.getClass().getSimpleName();
		// given
		String fetchingAllEntitiesInJpql = "select e from "+entityName+" e order by e.id";
		
		
		// when
		_logger.info("Selecting (using JPQL)...");
		_logger.info("						...>>>> "+fetchingAllEntitiesInJpql);
		List<BaseEntity> entities = em.createQuery(
				fetchingAllEntitiesInJpql, BaseEntity.class).getResultList();

		// then
		_logger.info("Found " + entities.size()
				+ " "+entityName+" (using JPQL):");

		return entities;
	};

	public abstract BaseEntity update(BaseEntity entity);
	
	public abstract boolean delete(BaseEntity entity);
	
	public abstract boolean create(BaseEntity entity);
	
	public abstract BaseEntity get(String description);
	
	public void move(BaseEntity entityToMove, BaseEntity entityFROM, BaseEntity entityTO){
		//BaseEntity changement, BaseEntity livraison,BaseEntity livraisonNEW
		
		Iterator<BaseEntity> it = entityFROM.getChildren(entityToMove).iterator();
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
	};
	
}
