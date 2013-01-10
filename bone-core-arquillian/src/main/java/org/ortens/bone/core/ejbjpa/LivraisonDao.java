package org.ortens.bone.core.ejbjpa;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ortens.bone.core.model.Changement;
import org.ortens.bone.core.model.Livraison;

@Stateless
public class LivraisonDao {
	public static Logger _logger = Logger
			.getLogger(LivraisonDao.class.getName());
	
	@PersistenceContext
	protected EntityManager em;
	
	public void move(Changement changement, Livraison livraison,
			Livraison livraisonNEW) {
		Iterator<Changement> it = livraison.getChangements().iterator();
		Changement change = null;
		while (it.hasNext()) {
			change = it.next();
			_logger.info("change.getDescription() : " + change.getDescription());
			if ("Correction".equals(change.getDescription())) {
				_logger.info("change treated: Correction");
				it.remove();
				_logger.info("					|__ changement  : REMOVED !");
			}
		}
		livraisonNEW.getChangements().add(changement);
		em.merge(livraison);
		em.persist(livraison);
	}

	public List<Livraison> getList() {
		// given
		String fetchingAllLivraisonsInJpql = "select l from Livraison l order by l.id";

		// when
		_logger.info("Selecting (using JPQL)...");
		List<Livraison> livraisons = em.createQuery(
				fetchingAllLivraisonsInJpql, Livraison.class).getResultList();

		// then
		_logger.info("Found " + livraisons.size() + " livraisons (using JPQL):");
		return livraisons;

	}

}
