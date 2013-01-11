package org.ortens.bone.core.ejbjpa;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ortens.bone.core.model.BaseEntity;

public abstract class BaseEntityDao {
	public static Logger _logger = Logger
			.getLogger(BaseEntityDao.class.getName());

	@PersistenceContext
	EntityManager em;
	
	public List<BaseEntity> getList(BaseEntity entity) {
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

	
}
