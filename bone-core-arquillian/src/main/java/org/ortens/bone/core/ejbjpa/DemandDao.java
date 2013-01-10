package org.ortens.bone.core.ejbjpa;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ortens.bone.core.model.Changement;
import org.ortens.bone.core.model.Demand;
import org.ortens.bone.core.model.Livraison;

@Stateless
public class DemandDao {
	public static Logger _logger = Logger
			.getLogger(DemandDao.class.getName());
	
	@PersistenceContext
	protected EntityManager em;
	
	public void move(Demand demand, List<Changement> changements) {
		for (Changement changement : demand.getTravaux()) {
			_logger.info("					|__ description  : "
					+ changement.getDescription());
			for (Changement changementToAddTo : changements) {
				if ("TestChangement4"
						.equals(changementToAddTo.getDescription())) {
					demand.getTravaux().remove(changement);
					demand.getTravaux().add(changementToAddTo);
				}
			}
		}
	}

	public void updateDescription(String description, String descriptionOLD) {
		Query q = em.createQuery("UPDATE Demand d SET d.description = :description WHERE d.description = :descriptionOLD");
		q.setParameter("description", description);
		q.setParameter("descriptionOLD", descriptionOLD);
		q.executeUpdate();
	}

	public void moveWithoutChildren(List<Changement> changements,
			Demand anotherDemand) {
		for (Changement changement : anotherDemand.getTravaux()) {
			_logger.info("					|__ description  : "
					+ changement.getDescription());
			for (Changement changementToAddTo : changements) {
				_logger.info(" 1.2 - IN THE LOOP : changementToAddTo.getDescription() : "
						+ changementToAddTo.getDescription());
				if ("TestChangement3"
						.equals(changementToAddTo.getDescription())) {
					anotherDemand.getTravaux().remove(changement);
					anotherDemand.getTravaux().add(changementToAddTo);
				}
			}
		}
		
	}

	public void delete(Demand demand) {
		Iterator<Changement> it = demand.getTravaux().iterator();
		Changement change = null;

		while (it.hasNext()) {
			_logger.info("==============>>>>>>>>>> ----- INTO REMOVE LOOP");
			change = it.next();
			_logger.info("==============>>>>>>>>>> ----- change.getDescription() : "
					+ change.getDescription());
			_logger.info("==============>>>>>>>>>> ----- BEFORE REMOVE : change.getDemandes().size() : "
					+ change.getDemandes().size());
			change.getDemandes().remove(demand);
			demand.getTravaux().remove(change);
			em.persist(change);
			em.persist(demand);
			em.flush();
			_logger.info("==============>>>>>>>>>> ----- AFTER REMOVE : change.getDemandes().size() : "
					+ change.getDemandes().size());
		}
		_logger.info("==============>>>>>>>>>> ----- OUT OF REMOVE LOOP");

	}

	public void addOneDemand() {
		// 1 - Demand Creation
		Demand demandX = new Demand();
		demandX.setDescription("testDemandX");
		// 2 - Demand linking to Change : TestChangement4
		String query = "select c from Changement c where c.description = 'TestChangement4'";
		Changement changement = em.createQuery(query, Changement.class)
				.getSingleResult();
		demandX.getTravaux().add(changement);
		changement.getDemandes().add(demandX);
		em.flush();
	}

	public void deleteDemandsFromOneLivraison(String lDescription) {
		String queryLivraison = "select l from Livraison l where l.description = '"+lDescription+"'";
		Livraison livraison = em.createQuery(queryLivraison, Livraison.class).getSingleResult();
		
		Iterator<Changement> it = livraison.getChangements().iterator();
		Changement change = null;
		Demand demand = null;
		while (it.hasNext()){
			change = it.next();
			Iterator<Demand> it2 = change.getDemandes().iterator();
			while(it2.hasNext()){
				demand = it2.next();
				demand.getTravaux().remove(change);
				it2.remove();				
				em.flush();
				em.persist(demand);
			}
			em.persist(change);
		}
	}
	
	public List<Demand> getList() {
		// given
		String fetchingAllDemandsInJpql = "select d from Demand d order by d.id";

		// when
		_logger.info("Selecting (using JPQL)...");
		List<Demand> demands = em.createQuery(
				fetchingAllDemandsInJpql, Demand.class).getResultList();

		// then
		_logger.info("Found " + demands.size() + " demands (using JPQL):");
		
		return demands;
	}

}
