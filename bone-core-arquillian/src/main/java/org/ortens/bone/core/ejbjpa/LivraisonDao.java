package org.ortens.bone.core.ejbjpa;


import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ortens.bone.core.model.BaseEntity;


@Stateless
public class LivraisonDao extends BaseEntityDao{
	public static Logger _logger = Logger
			.getLogger(LivraisonDao.class.getName());
	
	@PersistenceContext
	protected EntityManager em;

//	@Override
//	public void move(BaseEntity changement, BaseEntity livraison,
//			BaseEntity livraisonNEW) {
//		Iterator<Changement> it = ((Livraison) livraison).getChangements().iterator();
//		Changement change = null;
//		while (it.hasNext()) {
//			change = it.next();
//			_logger.info("change.getDescription() : " + change.getDescription());
//			if ("Correction".equals(change.getDescription())) {
//				_logger.info("change treated: Correction");
//				it.remove();
//				_logger.info("					|__ changement  : REMOVED !");
//			}
//		}
//		((Livraison) livraisonNEW).getChangements().add((Changement) changement);
//		em.flush();
//		em.persist(livraison);
//		em.persist(livraisonNEW);
//	}

	@Override
	public BaseEntity update(BaseEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(BaseEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean create(BaseEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseEntity get(String description) {
		// TODO Auto-generated method stub
		return null;
	}
}
