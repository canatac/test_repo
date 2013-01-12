package org.ortens.bone.core.ejbjpa;

import java.util.Iterator;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ortens.bone.core.model.GenericEntity;
import org.ortens.bone.core.model.Changement;
import org.ortens.bone.core.model.Demand;
import org.ortens.bone.core.model.Livraison;

@Stateless
//public class ChangementDao extends GenericEntityDaoImpl{
public class ChangementDao extends GenericEntityDaoImpl{   
	public static Logger _logger = Logger.getLogger(ChangementDao.class
			.getName());

	@PersistenceContext
	protected EntityManager em;

	@Inject
	Demand demand;


	public void move(GenericEntity changement, GenericEntity livraison,
			GenericEntity livraisonNEW) {
		Iterator<Changement> it = ((Livraison)livraison).getChangements().iterator();
		while (it.hasNext()) {
			if (changement.getDescription().equals(it.next().getDescription())) {
				it.remove();
				_logger.info("					|__ changement  "
						+ changement.getDescription() + "1 : REMOVED !");
			}
		}

		((Livraison) livraisonNEW).getChangements().add((Changement) changement);
		em.flush();
		em.persist(livraison);
		em.persist(livraisonNEW);
	}
	
	public void addDemands(Changement changement) {
		Demand demandX = new Demand();
		Demand demandY = new Demand();
		Demand demandZ = new Demand();
		demandX.setDescription("TestDemandX");
		demandY.setDescription("TestDemandY");
		demandZ.setDescription("TestDemandZ");

		demandX.getTravaux().add(changement);
		demandY.getTravaux().add(changement);
		demandZ.getTravaux().add(changement);
		em.flush();
		em.persist(demandX);
		em.persist(demandY);
		em.persist(demandZ);

		changement.getDemandes().add(demandX);
		changement.getDemandes().add(demandY);
		changement.getDemandes().add(demandZ);
		em.flush();
		em.persist(changement);
	}

	public void copy(Changement change, Livraison livraison) {
		Changement changeCopy = new Changement();
		Demand demandCopy = new Demand();

		changeCopy.setDescription(change.getDescription());
		changeCopy.getDemandes().addAll(change.getDemandes());
		changeCopy.getLivrables().add(livraison);

		Iterator<Demand> it = change.getDemandes().iterator();
		while (it.hasNext()) {
			demand = it.next();
			demandCopy.setDescription(demand.getDescription());
			demandCopy.getTravaux().addAll(demand.getTravaux());
		}
		em.persist(demandCopy);
		em.persist(changeCopy);

		livraison.getChangements().add(changeCopy);
		em.persist(livraison);
	}

	
	public GenericEntity update(GenericEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean delete(GenericEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean create(GenericEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public GenericEntity get(String description) {
		// TODO Auto-generated method stub
		return null;
	}
}
