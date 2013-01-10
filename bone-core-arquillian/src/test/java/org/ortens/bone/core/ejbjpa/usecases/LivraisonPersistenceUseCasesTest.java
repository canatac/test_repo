package org.ortens.bone.core.ejbjpa.usecases;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.ortens.bone.core.ejbjpa.ChangementDao;
import org.ortens.bone.core.ejbjpa.DemandDao;
import org.ortens.bone.core.ejbjpa.LivraisonDao;
import org.ortens.bone.core.model.BaseEntity;
import org.ortens.bone.core.model.Changement;
import org.ortens.bone.core.model.Demand;
import org.ortens.bone.core.model.Livraison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class LivraisonPersistenceUseCasesTest{

	public static Logger _logger = Logger
			.getLogger(LivraisonPersistenceUseCasesTest.class.getName());
	
	
	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(BaseEntity.class, Demand.class, Livraison.class,
						Changement.class, LivraisonDao.class, DemandDao.class, ChangementDao.class)
				.addAsResource("test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("jbossas-ds.xml"); // arquillian-jbossas-managed
														// profile. Can stay
														// without perturbing
														// arquillian-glassfish-embedded
														// test
	}

	protected static final String[] LIVRAISON_TITLES = { "Document", "Software" };
	@PersistenceContext
	protected EntityManager em;
	@Inject
	protected UserTransaction utx;
	@Inject
	DemandDao demandDao;
	@Inject
	LivraisonDao livraisonDao;
	@Inject
	ChangementDao changementDao;
	@Inject
	Changement changement,changement2,changement3,changement4;
	@Inject
	Demand demand,demand2,demand3,demand4,demand5,demand6;
	@Inject
	Livraison livraison;
	
	
	
	@Before
	public void preparePersistenceTest() throws Exception {
		_logger.setLevel(Level.INFO);
		clearData();
		insertData();
		starttransaction();
	}

	@Test
	public void shouldFindAllLivraisonsUsingJpqlQuery() throws Exception {
		_logger.info("==============>>>>>>>>>> INTO TEST : shouldFindAllLivraisonsUsingJpqlQuery()");
		
		List<Livraison> livraisons = livraisonDao.getList();

		assertContainsAllLivraisons(livraisons);
		_logger.info("==============>>>>>>>>>> OUT OF TEST : shouldFindAllLivraisonsUsingJpqlQuery()");
	}

	@Test
	public void shouldFindAllLivraisonsUsingCriteriaApi() throws Exception {
		_logger.info("==============>>>>>>>>>> INTO TEST : shouldFindAllLivraisonsUsingCriteriaApi()");
		// given
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Livraison> criteria = builder
				.createQuery(Livraison.class);

		Root<Livraison> livraison = criteria.from(Livraison.class);
		criteria.select(livraison);
		// TIP: If you don't want to use the JPA 2 Metamodel,
		// you can change the get() method call to get("id")
		// criteria.orderBy(builder.asc(changement.get(Changement_.id)));
		criteria.orderBy(builder.asc(livraison.get("id")));
		// No WHERE clause, which implies select all

		// when
		_logger.info("Selecting (using Criteria)...");
		List<Livraison> livraisons = em.createQuery(criteria).getResultList();

		// then
		_logger.info("Found " + livraisons.size()
				+ " livraisons (using Criteria):");
		assertContainsAllLivraisons(livraisons);

		_logger.info("==============>>>>>>>>>> OUT OF TEST : shouldFindAllLivraisonsUsingCriteriaApi()");
	}

	
	/**
	 * 	// 2.Move a change with the demands
		// |__2.1 - To another Livraison
		//
		// BEFORE :
		// ---------
		// ---------> livraison : Document
		// 				|_____________changement : Correction
		// 					|_____________demande : TestDemand2
		// 					|_____________demande : TestDemand3
		// 				|_____________changement : Evolution
		// 					|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// 				|_____________changement : TestChangement4
		// 					|_____________demande : TestDemand6
		// 				|_____________changement : TestChangement3
		// 					|_____________demande : TestDemand5
		// 					|_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// 				|_____________changement : Evolution
		// 					|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// 				|_____________changement : TestChangement4
		// 					|_____________demande : TestDemand6
		// 				|_____________changement : TestChangement3
		// 					|_____________demande : TestDemand5
		// 					|_____________demande : TestDemand4
		// 				|_____________changement : Correction
		// 					|_____________demande : TestDemand2
		// 					|_____________demande : TestDemand3
	 * 
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws SystemException
	 * @throws NotSupportedException
	 */
	@Test
	public void moveAChangeWithDemandsToAnotherLivraison()
			throws SecurityException, IllegalStateException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException,
			SystemException, NotSupportedException {
		_logger.info("==============>>>>>>>>>> INTO TEST : moveAChangeWithDemandsToAnotherLivraison()");
		

		// ----->> INIT
		String fetchingChangementsInJpql = "select c from Changement c where c.description = 'Correction'";
		Changement changement = em.createQuery(fetchingChangementsInJpql,
				Changement.class).getSingleResult();

		String fetchingLivraisonInJpql = "select l from Livraison l where l.description = 'Document'";
		String fetchingLivraisonInJpqlNEW = "select l from Livraison l where l.description = 'Software'";
		Livraison livraison = em.createQuery(fetchingLivraisonInJpql,
				Livraison.class).getSingleResult();
		Livraison livraisonNEW = em.createQuery(fetchingLivraisonInJpqlNEW,
				Livraison.class).getSingleResult();

		_logger.info("livraison.getChangements().size() : "
				+ livraison.getChangements().size());
		_logger.info("livraison.getChangements().toString() : "
				+ livraison.getChangements().toString());

		// ----->> OPERATION
		
		livraisonDao.move(changement, livraison, livraisonNEW);

		// ==>
		
		utx.commit();
		em.clear();
		
		// ----->> VERIFICATION
		verifyTreeMove();

		_logger.info("==============>>>>>>>>>> OUT OF TEST : moveAChangeWithDemandsToAnotherLivraison()");

	}

	/**
	 * 
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws SystemException
	 * @throws NotSupportedException
	 */
	@Test
	public void moveADemandToOtherChangementSameLivraison() throws SecurityException, IllegalStateException,
			RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException {
		_logger.info("==============>>>>>>>>>> INTO TEST : moveADemandToOtherChangementSameLivraison()");

		// -------------------->> INIT
		String fetchingADemandInJpql = "select d from Demand d where d.description = 'TestDemand5'";
		String fetchingChangementsInJpql = "select c from Changement c";

		_logger.info("Selecting (using JPQL)...");
		
		Demand demand = em.createQuery(fetchingADemandInJpql, Demand.class)
				.getSingleResult();
		List<Changement> changements = em.createQuery(
				fetchingChangementsInJpql, Changement.class).getResultList();

		_logger.info("Found demand (using JPQL) : " + demand.getDescription());
		_logger.info("			|__ Nb of linked changements : "
				+ demand.getTravaux().size());
		
		// -------------------->> OPERATION
		demandDao.move(demand, changements);
		
		utx.commit();
		// -------------------->> VERIFICATION
		utx.begin();
		em.joinTransaction();
		Demand demandAFTER_MOVE = em.createQuery(fetchingADemandInJpql,
				Demand.class).getSingleResult();
		List<Changement> changementsAFTER_MOVE = em.createQuery(
				fetchingChangementsInJpql, Changement.class).getResultList();
		_logger.info("AFTER MOVE - demand (using JPQL) : "
				+ demandAFTER_MOVE.getDescription());
		_logger.info("			|__ Nb of linked changements : "
				+ demandAFTER_MOVE.getTravaux().size());
		for (Changement changement : demandAFTER_MOVE.getTravaux()) {
			_logger.info("					|__ description  : "
					+ changement.getDescription());
		}
		for (Changement changementAFTER_MOVE : changementsAFTER_MOVE) {
			if ("TestChangement4".equals(changementAFTER_MOVE.getDescription())) {
				_logger.info("			|__ Nb of linked demandes : "
						+ changementAFTER_MOVE.getDemandes().size());
				_logger.info("AFTER MOVE - demand (using JPQL) : ");
				for (Demand demandAfterMoveInChangement : changementAFTER_MOVE
						.getDemandes()) {
					_logger.info("			|__ description : "
							+ demandAfterMoveInChangement.getDescription());
				}
			}
		}
		_logger.info("==============>>>>>>>>>> OUT OF TEST : moveADemandToOtherChangementSameLivraison()");

	}

	/**
	 * @throws SystemException 
	 * @throws HeuristicRollbackException 
	 * @throws HeuristicMixedException 
	 * @throws RollbackException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws NotSupportedException 
	 * 
	 */
	@Test
	public void moveADemandToOtherChangementOtherLivraison() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException{
		_logger.info("==============>>>>>>>>>> INTO TEST : moveADemandToOtherChangementOtherLivraison()");
		String fetchingChangementsInJpql = "select c from Changement c";

		_logger.info("Selecting (using JPQL)...");
		List<Changement> changements = em.createQuery(
				fetchingChangementsInJpql, Changement.class).getResultList();
		String fetchingAnotherDemandInJpql = "select d from Demand d where d.description = 'TestDemand2'";

		_logger.info("Selecting (using JPQL)...");
		Demand anotherDemand = em.createQuery(fetchingAnotherDemandInJpql,
				Demand.class).getSingleResult();

		_logger.info("Found demand (using JPQL) : "
				+ anotherDemand.getDescription());
		_logger.info("			|__ Nb of linked changements : "
				+ anotherDemand.getTravaux().size());
		
		demandDao.moveWithoutChildren(changements, anotherDemand);
		
		utx.commit();
		// -------------------->> VERIFICATION
		utx.begin();
		em.joinTransaction();
		Demand demandAfterMove2 = em.createQuery(fetchingAnotherDemandInJpql,
				Demand.class).getSingleResult();
		List<Changement> changementsAfterMove2 = em.createQuery(
				fetchingChangementsInJpql, Changement.class).getResultList();
		_logger.info("AFTER MOVE - demand (using JPQL) : "
				+ demandAfterMove2.getDescription());
		_logger.info("			|__ Nb of linked changements : "
				+ demandAfterMove2.getTravaux().size());
		for (Changement changement : demandAfterMove2.getTravaux()) {
			_logger.info("					|__ description  : "
					+ changement.getDescription());
		}
		for (Changement changementAfterMove : changementsAfterMove2) {
			if ("TestChangement3".equals(changementAfterMove.getDescription())) {
				_logger.info("			|__ Nb of linked demandes : "
						+ changementAfterMove.getDemandes().size());
				_logger.info("AFTER MOVE - demand (using JPQL) : ");
				for (Demand demandAfterMoveInChangement : changementAfterMove
						.getDemandes()) {
					_logger.info("			|__ description : "
							+ demandAfterMoveInChangement.getDescription());
				}
			}
		}
		_logger.info("==============>>>>>>>>>> OUT OF TEST : moveADemandToOtherChangementOtherLivraison()");
	}

	/**
	 * //
		// BEFORE :
		// ---------
		// ---------> livraison : Document
		// 				|_____________changement : Correction
		// 						|_____________demande : TestDemand2
		// 						|_____________demande : TestDemand3
		// 				|_____________changement : Evolution
		// 						|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// 				|_____________changement : TestChangement4
		// 						|_____________demande : TestDemand6
		// 				|_____________changement : TestChangement3
		// 						|_____________demande : TestDemand5
		// 						|_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// 				|_____________changement : Correction
		// 						|_____________demande : Correction1
		// 						|_____________demande : TestDemand3
		// 				|_____________changement : Evolution
		// 						|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// 				|_____________changement : TestChangement4
		// 						|_____________demande : TestDemand6
		// 				|_____________changement : TestChangement3
		// 						|_____________demande : TestDemand5
		// 						|_____________demande : TestDemand4
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws SystemException
	 * @throws NotSupportedException
	 */
	@Test
	public void updateOneItem() throws SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException {
		_logger.info("==============>>>>>>>>>> INTO TEST : updateOneItem()");
		

		String description = "Correction1";
		String descriptionOLD = "TestDemand2";
		
		demandDao.updateDescription(description,descriptionOLD);
		
		// -------------------->> VERIFICATION

		String fetchingChangementInJpql = "select c from Changement c where c.description = 'Correction'";
		Changement changement2 = em.createQuery(fetchingChangementInJpql,
				Changement.class).getSingleResult();
		Iterator<Demand> it2 = changement2.getDemandes().iterator();
		Demand demande2 = null;
		_logger.info("changement.getDemandes() : " + changement2.getDemandes());

		while (it2.hasNext()) {
			demande2 = it2.next();
			_logger.info("demande2.getDescription() : "
					+ demande2.getDescription());
		}

		_logger.info("==============>>>>>>>>>> OUT OF TEST : updateOneItem()");
	}

	/**
	 * 	//
		// BEFORE :
		// ---------
		// ---------> livraison : Document
		// 				|_____________changement : Correction
		// 					|_____________demande : TestDemand2
		// 					|_____________demande : TestDemand3
		// 				|_____________changement : Evolution
		// 					|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// 				|_____________changement : TestChangement4
		// 					|_____________demande : TestDemand6
		// 				|_____________changement : TestChangement3
		// 					|_____________demande : TestDemand5
		//					|_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// 				|_____________changement : Correction
		// 					|_____________XXXXXXXXXXXXXXXXXXXXX
		// 					|_____________demande : TestDemand3
		// 				|_____________changement : Evolution
		// 					|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// 				|_____________changement : TestChangement4
		// 					|_____________demande : TestDemand6
		// 				|_____________changement : TestChangement3
		// 					|_____________demande : TestDemand5
		// 					|_____________demande : TestDemand4
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws SystemException
	 * @throws NotSupportedException
	 */
	@Test
	public void deleteOneItem() throws SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException {

		_logger.info("==============>>>>>>>>>> INTO TEST : deleteOneItem()");
		String description = "TestDemand2";

		String fetchingADemandInJpql = "select d from Demand d where d.description = '"
				+ description + "'";

		Demand demand = em.createQuery(fetchingADemandInJpql, Demand.class)
				.getSingleResult();

		demandDao.delete(demand);
		
		utx.commit();

		//
		// -------------------->> VERIFICATION
		utx.begin();
		em.joinTransaction();

		String fetchingAOldDemandInJpql = "select d from Demand d where d.description = '"
				+ description + "'";

		Demand demandOLD = em.createQuery(fetchingAOldDemandInJpql,
				Demand.class).getSingleResult();
		Changement changeOLD = null;
		if (demandOLD != null) {
			_logger.info("==============>>>>>>>>>> ----- STILL HERE : demand.getDescription() : "
					+ demandOLD.getDescription());
			Iterator<Changement> itOLD = demandOLD.getTravaux().iterator();
			_logger.info("==============>>>>>>>>>> ----- SEARCHING CHANGE");
			while (itOLD.hasNext()) {
				_logger.info("==============>>>>>>>>>> ----- CHANGE FOUND");
				changeOLD = itOLD.next();
				_logger.info("==============>>>>>>>>>> ----- CHANGE : "
						+ changeOLD.getDescription());
			}
		}

		//
		_logger.info("==============>>>>>>>>>> ----- DELETING DEMAND........");
		Query q = em
				.createQuery("DELETE Demand d WHERE d.description = :description");
		q.setParameter("description", description);
		q.executeUpdate();

		utx.commit();
		_logger.info("==============>>>>>>>>>> ----- DELETING DEMAND........DONE");

		utx.begin();
		em.joinTransaction();

		String fetchingAOldDemandInJpql2 = "select d from Demand d where d.description = '"
				+ description + "'";
		Demand demandOLD2 = null;
		try {
			demandOLD2 = em
					.createQuery(fetchingAOldDemandInJpql2, Demand.class)
					.getSingleResult();
			Changement changeOLD2 = null;
			if (demandOLD2 != null) {
				_logger.info("==============>>>>>>>>>> ----- STILL HERE : demand.getDescription() : "
						+ demandOLD2.getDescription());
				Iterator<Changement> itOLD2 = demandOLD2.getTravaux()
						.iterator();
				_logger.info("==============>>>>>>>>>> ----- SEARCHING CHANGE");
				while (itOLD2.hasNext()) {
					_logger.info("==============>>>>>>>>>> ----- CHANGE FOUND");
					changeOLD2 = itOLD2.next();
					_logger.info("==============>>>>>>>>>> ----- CHANGE : "
							+ changeOLD2.getDescription());
				}
			} else {
				_logger.info("==============>>>>>>>>>> ----- DEMAND DELETED !");
			}

		} catch (NoResultException e) {
			_logger.info("==============>>>>>>>>>> ----- NoResultException ! DEMAND DELETED !");
		}

		String fetchingChangementInJpql = "select c from Changement c where c.description = 'Correction'";
		Changement changement = em.createQuery(fetchingChangementInJpql,
				Changement.class).getSingleResult();
		Iterator<Demand> it2 = changement.getDemandes().iterator();
		Demand demande = null;
		_logger.info("changement.getDemandes() : " + changement.getDemandes());

		while (it2.hasNext()) {
			demande = it2.next();
			_logger.info("demande.getDescription() : "
					+ demande.getDescription());
		}

		_logger.info("==============>>>>>>>>>> OUT OF TEST : deleteOneItem()");
	}


	/**
	 *	//
		// BEFORE :
		// ---------
		// ---------> livraison : Document
		// 				|_____________changement : Correction
		// 						|_____________demande : TestDemand2
		// 						|_____________demande : TestDemand3
		// 				|_____________changement : Evolution
		// 						|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// 				|_____________changement : TestChangement4
		// 						|_____________demande : TestDemand6
		// 				|_____________changement : TestChangement3
		// 						|_____________demande : TestDemand5
		// 						|_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// 				|_____________changement : Correction
		// 						|_____________demande : TestDemand2
		// 						|_____________demande : TestDemand3
		// 				|_____________changement : Evolution
		// 						|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// 				|_____________changement : TestChangement4
		// 						|_____________demande : TestDemand6
		// 						|_____________demande : TestDemandX
		// 				|_____________changement : TestChangement3
		// 						|_____________demande : TestDemand5
		// 						|_____________demande : TestDemand4
	 */
	@Test
	public void addOneItem() {
		_logger.info("==============>>>>>>>>>> INTO TEST : addOneItem()");
		
		// -------------------->> OPERATION
		demandDao.addOneDemand();

		// -------------------->> VERIFICATION
		String fetchingChangementInJpql = "select c from Changement c where c.description = 'TestChangement4'";
		Changement changement2 = em.createQuery(fetchingChangementInJpql,
				Changement.class).getSingleResult();
		Iterator<Demand> it2 = changement2.getDemandes().iterator();
		Demand demande = null;
		_logger.info("changement.getDemandes() : " + changement2.getDemandes());

		while (it2.hasNext()) {
			demande = it2.next();
			_logger.info("demande.getDescription() : "
					+ demande.getDescription());
		}

		_logger.info("==============>>>>>>>>>> OUT OF TEST : addOneItem()");
	}

	@Test
	/**
	 * //
		// BEFORE :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// 					|_____________demande : TestDemand2
		// 					|_____________demande : TestDemand3
		// |_____________changement : Evolution
		// 					|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// 					|_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// 					|_____________demande : TestDemand5
		// 					|_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : TestChangement3
		// 					|_____________demande : TestDemand5
		// 					|_____________demande : TestDemand4
		// |_____________changement : Evolution
		// 					|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : Correction
		// 					|_____________demande : TestDemand2
		// 					|_____________demande : TestDemand3
		// |_____________changement : TestChangement4
		// 					|_____________demande : TestDemand6
	 * 
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws SystemException
	 * @throws NotSupportedException
	 */
	public void moveSeveralItems() throws SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException {

		_logger.info("==============>>>>>>>>>> INTO TEST : moveSeveralItems()");
		// Move TestChangement3 from Software to Document
		
		String sChange = "TestChangement3";		
		String sLivraison = "Software";
		String sChange2 = "Correction";
		String sLivraison2 = "Document";
		
		String fetchingChangementsInJpql = "select c from Changement c where c.description = '"
				+ sChange + "'";
		String fetchingLivraisonInJpql = "select l from Livraison l where l.description = '"
				+sLivraison+"'";
		String fetchingChangementsInJpql2 = "select c from Changement c where c.description = '"
				+ sChange2 + "'";
		String fetchingLivraisonInJpqlNEW = "select l from Livraison l where l.description = '"
				+sLivraison2+"'";
		
		Changement changement = em.createQuery(fetchingChangementsInJpql,
				Changement.class).getSingleResult();
		Livraison livraison = em.createQuery(fetchingLivraisonInJpql,
				Livraison.class).getSingleResult();
		Changement changement2 = em.createQuery(fetchingChangementsInJpql2,
				Changement.class).getSingleResult();
		Livraison livraisonNEW = em.createQuery(fetchingLivraisonInJpqlNEW,
				Livraison.class).getSingleResult();
		
		_logger.info("livraison : "+sLivraison+" getChangements().size() : "
				+ livraison.getChangements().size());
		_logger.info("livraison : "+sLivraison+" getChangements().toString() : "
				+ livraison.getChangements().toString());
		_logger.info("livraison : "+sLivraison2+" .getChangements().size() : "
				+ livraisonNEW.getChangements().size());
		_logger.info("livraison : "+sLivraison2+" getChangements().toString() : "
				+ livraisonNEW.getChangements().toString());
		
		
		// ----> OPERATION
		changementDao.move(changement,livraison,livraisonNEW);
		changementDao.move(changement2,livraisonNEW,livraison);
		
		// ==>
		utx.commit();
		em.clear();

		// ----> VERIFICATION
		
		utx.begin();
		em.joinTransaction();
		String sChangeTO_CHECK = "TestChangement3";
		String sChangeTO_CHECK2 = "Correction";
		String fetchingChangementsInJpqlTO_CHECK = "select c from Changement c where c.description = '"
				+ sChangeTO_CHECK + "'";
		String fetchingChangementsInJpqlTO_CHECK2 = "select c from Changement c where c.description = '"
				+ sChangeTO_CHECK2 + "'";
		Changement changementTO_CHECK = em.createQuery(
				fetchingChangementsInJpqlTO_CHECK, Changement.class)
				.getSingleResult();
		Changement changementTO_CHECK2 = em.createQuery(
				fetchingChangementsInJpqlTO_CHECK2, Changement.class)
				.getSingleResult();

		String fetchingLivraisonInJpqlTO_CHECK = "select l from Livraison l where l.description = 'Software'";
		String fetchingLivraisonInJpqlTO_CHECKNEW = "select l from Livraison l where l.description = 'Document'";
		Livraison livraisonTO_CHECK = em.createQuery(
				fetchingLivraisonInJpqlTO_CHECK, Livraison.class)
				.getSingleResult();
		Livraison livraisonTO_CHECKNEW = em.createQuery(
				fetchingLivraisonInJpqlTO_CHECKNEW, Livraison.class)
				.getSingleResult();

		for (Livraison l1 : changementTO_CHECK.getLivrables()) {
			_logger.info("l1.getDescription() in TestChangement3 : " + l1.getDescription());
		}
		for (Livraison l2 : changementTO_CHECK2.getLivrables()) {
			_logger.info("l2.getDescription() in Correction : " + l2.getDescription());
		}
		for (Changement c1 : livraisonTO_CHECK.getChangements()) {
			_logger.info("c1.getDescription() in Software : " + c1.getDescription());
		}
		for (Changement c2 : livraisonTO_CHECKNEW.getChangements()) {
			_logger.info("c2.getDescription() in Document : " + c2.getDescription());
		}
		_logger.info("==============>>>>>>>>>> OUT OF TEST : moveSeveralItems()");
	}


	 /**
	  * //
			// BEFORE :
			// ---------
			// ---------> livraison : Document
			// |_____________changement : Correction
			// 					|_____________demande : TestDemand2
			// 					|_____________demande : TestDemand3
			// |_____________changement : Evolution
			// 					|_____________demande : TestDemand
			//
			// ---------> livraison : Software
			// |_____________changement : TestChangement4
			// 					|_____________demande : TestDemand6
			// |_____________changement : TestChangement3
			// 					|_____________demande : TestDemand5
			// 					|_____________demande : TestDemand4
			//
			// AFTER :
			// ---------
			// ---------> livraison : Document
			// |_____________changement : Correction
			// 					|_____________demande : TDemand2
			// 					|_____________demande : TDemand3
			// |_____________changement : Evolution
			// 					|_____________demande : TDemand
			//
			// ---------> livraison : Software
			// |_____________changement : TestChangement4
			// 					|_____________demande : TDemand6
			// |_____________changement : TestChangement3
			// 					|_____________demande : TDemand5
			// 					|_____________demande : TDemand4
	  * @throws SecurityException
	  * @throws IllegalStateException
	  * @throws RollbackException
	  * @throws HeuristicMixedException
	  * @throws HeuristicRollbackException
	  * @throws SystemException
	  * @throws NotSupportedException
	  */
	 @Test
	public void updateSeveralItems() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
			
		_logger.info("==============>>>>>>>>>> INTO TEST : updateSeveralItems()");
		// Load every demands
		String query = "select d from Demand d";
		List<Demand> demands = em.createQuery(query,Demand.class).getResultList();
		
		Iterator<Demand> it = demands.iterator();
		String descriptionOLD = "";
		String descriptionNEW = "";
		Demand demand = null;
		while(it.hasNext()){
			demand = it.next();
			descriptionOLD = demand.getDescription();
			descriptionNEW = descriptionOLD.replace("Test", "T");
			_logger.info("descriptionNEW : "+descriptionNEW);
			demand.setDescription(descriptionNEW);
			em.flush();
			em.persist(demand);
		}
		utx.commit();
		// ----> VERIFICATION
		utx.begin();
		em.joinTransaction();
		
		List<Demand> demandsTOCHECK = em.createQuery(query, Demand.class).getResultList();
		for(Demand demandTOCHECK : demandsTOCHECK){
			_logger.info("demandTOCHECK.getDescription() : "+demandTOCHECK.getDescription()); 
		}
		
		_logger.info("==============>>>>>>>>>> OUT OF TEST : updateSeveralItems()");
	}


	 /**
	  * // DELETE ALL THE DEMANDS LINKED TO A LIVRAISON
			// =================================================
			// BEFORE :
			// ---------
			// ---------> livraison : Document
			// |_____________changement : Correction
			// 					|_____________demande : TestDemand2
			// 					|_____________demande : TestDemand3
			// |_____________changement : Evolution
			// 					|_____________demande : TestDemand
			//
			// ---------> livraison : Software
			// |_____________changement : TestChangement4
			// 					|_____________demande : TestDemand6
			// |_____________changement : TestChangement3
			// 					|_____________demande : TestDemand5
			// 					|_____________demande : TestDemand4
			//
			// AFTER :
			// ---------
			// ---------> livraison : Document
			// |_____________changement : Correction
			// |_____________changement : Evolution
			//
			// ---------> livraison : Software
			// |_____________changement : TestChangement4
			// 					|_____________demande : TestDemand6
			// |_____________changement : TestChangement3
			// 					|_____________demande : TestDemand5
			// 					|_____________demande : TestDemand4
	  * @throws SecurityException
	  * @throws IllegalStateException
	  * @throws RollbackException
	  * @throws HeuristicMixedException
	  * @throws HeuristicRollbackException
	  * @throws SystemException
	  * @throws NotSupportedException
	  */
	 @Test
	public void deleteSeveralItems() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
			
		_logger.info("==============>>>>>>>>>> INTO TEST : deleteSeveralItems()");
		
		String lDescription = "Document";
		
		demandDao.deleteDemandsFromOneLivraison(lDescription);
		
		utx.commit();
		
		// ----> VERIFICATION
		
		utx.begin();
		em.joinTransaction();
		String queryLivraison = "select l from Livraison l where l.description = '"+lDescription+"'";
		Livraison livraisonTOCHECK = em.createQuery(queryLivraison, Livraison.class).getSingleResult();
		Iterator<Changement> it3 = livraisonTOCHECK.getChangements().iterator();
		Changement changeTOCHECK = null;
		while(it3.hasNext()){
			changeTOCHECK = it3.next();
			_logger.info("changeTOCHECK.getDescription() : "+changeTOCHECK.getDescription());
			Iterator<Demand> it4 = changeTOCHECK.getDemandes().iterator();
			if(it4.hasNext()){
				_logger.info("			|___IL RESTE DES DEMANDES - REVOYEZ VOTRE TEST!");
			}else{
				_logger.info("			|___IL NE RESTE AUCUNE DEMANDE ASSOCIEE A CE TEST -> GREAT !");
			}
		}
		
		_logger.info("==============>>>>>>>>>> OUT OF TEST : deleteSeveralItems()");
	}

	 /**
	  * // BEFORE :
			// ---------
			// ---------> livraison : Document
			// |_____________changement : Correction
			// 					|_____________demande : TestDemand2
			// 					|_____________demande : TestDemand3
			// |_____________changement : Evolution
			// 					|_____________demande : TestDemand
			//
			// ---------> livraison : Software
			// |_____________changement : TestChangement4
			// 					|_____________demande : TestDemand6
			// |_____________changement : TestChangement3
			// 					|_____________demande : TestDemand5
			// 					|_____________demande : TestDemand4
			// BEFORE :
			// ---------
			// ---------> livraison : Document
			// |_____________changement : Correction
			// 					|_____________demande : TestDemand2
			// 					|_____________demande : TestDemand3
			// |_____________changement : Evolution
			// 					|_____________demande : TestDemand
			//
			// ---------> livraison : Software
			// |_____________changement : TestChangement4
			// 					|_____________demande : TestDemand6
			// |_____________changement : TestChangement3
			// 					|_____________demande : TestDemand5
			// 					|_____________demande : TestDemand4
			// 					|_____________demande : TestDemandX
			// 					|_____________demande : TestDemandY
			// 					|_____________demande : TestDemandZ
	  * @throws SecurityException
	  * @throws IllegalStateException
	  * @throws RollbackException
	  * @throws HeuristicMixedException
	  * @throws HeuristicRollbackException
	  * @throws SystemException
	  * @throws NotSupportedException
	  */
	 @Test
	public void addSeveralItems() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
			
		 _logger.info("==============>>>>>>>>>> INTO TEST : addSeveralItems()");
		 
		 String cDescription = "TestChangement3";
		 
		 String query = "select c from Changement c where c.description = '"+cDescription+"'";
		 Changement changement = em.createQuery(query, Changement.class).getSingleResult();
		 changementDao.addDemands(changement);

		 utx.commit();
		 
		 // ----> VERIFICATION
		 utx.begin();
		 em.joinTransaction();
		 Changement changementTOCHECK = em.createQuery(query, Changement.class).getSingleResult();
		 
		 for (Demand demand : changementTOCHECK.getDemandes()){
			 _logger.info("demand.getDescription() in TestChangement3 : "+demand.getDescription());
		 }
		 
		 _logger.info("==============>>>>>>>>>> OUT OF TEST : addSeveralItems()");
	}

	 /**
	  * // BEFORE :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// 					|_____________demande : TestDemand2
		// 					|_____________demande : TestDemand3
		// |_____________changement : Evolution
		// 					|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// 					|_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// 					|_____________demande : TestDemand5
		// 					|_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// 					|_____________demande : TestDemand2
		// 					|_____________demande : TestDemand3
		// |_____________changement : Evolution
		// 					|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// 					|_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// 					|_____________demande : TestDemand5
		// 					|_____________demande : TestDemand4
		// |_____________changement : Evolution
		// 					|_____________demande : TestDemand
	  * @throws SecurityException
	  * @throws IllegalStateException
	  * @throws RollbackException
	  * @throws HeuristicMixedException
	  * @throws HeuristicRollbackException
	  * @throws SystemException
	  * @throws NotSupportedException
	  */
	 @Test
	public void copyOneItem() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
		_logger.info("==============>>>>>>>>>> INTO TEST : copyOneItem()");
		
		// ----> INIT
		String queryC = "select c from Changement c where c.description = 'Evolution'";
		Changement change = em.createQuery(queryC, Changement.class).getSingleResult();
		String queryL = "select l from Livraison l where l.description = 'Software'";
		Livraison livraison = em.createQuery(queryL,Livraison.class).getSingleResult();
		
		// ----> OPERATION
		changementDao.copy(change, livraison);
		
		utx.commit();
		
		// ----> VERIFICATION
		
		utx.begin();
		em.joinTransaction();
		
		List<Changement> changements = em.createQuery(queryC, Changement.class).getResultList();
		Livraison livraisonTOCHECK = em.createQuery(queryL, Livraison.class).getSingleResult();
		
		_logger.info("livraisonTOCHECK.getDescription() : " + livraisonTOCHECK.getDescription());
		
		for (Changement changeTOCHECK : livraisonTOCHECK.getChangements()){
			_logger.info("		|__changeTOCHECK : "+ changeTOCHECK.getDescription());
		}
		
		for(Changement changement : changements){
			for (Livraison livraison2 : changement.getLivrables()){
				_logger.info("changement.getDescription() : "+changement.getDescription());
				_logger.info("		|__livraison2.getDescription() : "+livraison2.getDescription());
			}
		}
		
		_logger.info("==============>>>>>>>>>> OUT OF TEST : copyOneItem()");
	}
	/**
	 * @throws NotSupportedException
	 * @throws SystemException
	 */
	private void verifyTreeMove() throws NotSupportedException, SystemException {
		utx.begin();
		em.joinTransaction();
		String fetchingChangementsInJpqlAFTER_MOVE = "select c from Changement c where c.description = 'Correction'";
		String fetchingLivraisonsInJpqlAFTER_MOVE_Software = "select l from Livraison l where l.description = 'Software'";
		String fetchingLivraisonsInJpqlAFTER_MOVE_Document = "select l from Livraison l where l.description = 'Document'";
		Changement changementsAFTER_MOVE = em.createQuery(
				fetchingChangementsInJpqlAFTER_MOVE, Changement.class)
				.getSingleResult();
		Livraison livraisonsAFTER_MOVE_Software = em.createQuery(
				fetchingLivraisonsInJpqlAFTER_MOVE_Software, Livraison.class)
				.getSingleResult();
		Livraison livraisonsAFTER_MOVE_Document = em.createQuery(
				fetchingLivraisonsInJpqlAFTER_MOVE_Document, Livraison.class)
				.getSingleResult();

		if (changementsAFTER_MOVE != null) {
			for (Livraison livraisonTOCHECK : changementsAFTER_MOVE
					.getLivrables()) {
				_logger.info("livraison.getDescription() in Changement CORRECTION : "
						+ livraisonTOCHECK.getDescription());
			}
		}
		if (livraisonsAFTER_MOVE_Software != null) {
			for (Changement changementAFTER_MOVE_Software : livraisonsAFTER_MOVE_Software
					.getChangements()) {
				_logger.info("changementAFTER_MOVE_Software.getDescription() in Livraison SOFTWARE : "
						+ changementAFTER_MOVE_Software.getDescription());
				if ("Correction".equals(changementAFTER_MOVE_Software
						.getDescription())) {
					for (Demand demande : changementAFTER_MOVE_Software
							.getDemandes()) {
						_logger.info("CORRECTION in SOFTWARE : demande.getDescription() : "
								+ demande.getDescription());
					}
				}
			}
		}
		if (livraisonsAFTER_MOVE_Document != null) {
			for (Changement changementAFTER_MOVE_Document : livraisonsAFTER_MOVE_Document
					.getChangements()) {
				_logger.info("changementAFTER_MOVE_Document.getDescription() in Livraison DOCUMENT : "
						+ changementAFTER_MOVE_Document.getDescription());
			}
		}
	}
	private static void assertContainsAllLivraisons(
			Collection<Livraison> retrievedLivraisons) {
		Assert.assertEquals(LIVRAISON_TITLES.length, retrievedLivraisons.size());

		final Set<String> retrievedLivraisonTitles = new HashSet<String>();
		for (Livraison livraison : retrievedLivraisons) {
			_logger.info("* " + livraison);
			_logger.info("---------> livraison.getDescription() : "
					+ livraison.getDescription());
			for (Changement changement : livraison.getChangements()) {
				_logger.info("		|_____________changement : "
						+ changement.getDescription());
				// _logger.info("		|_____________changement.getDemandes().size() : "+changement.getDemandes().size());
				for (Demand demand : changement.getDemandes()) {
					_logger.info("			|_____________demande : "
							+ demand.getDescription());
				}
			}
			retrievedLivraisonTitles.add(livraison.getDescription());
		}
		Assert.assertTrue(retrievedLivraisonTitles.containsAll(Arrays
				.asList(LIVRAISON_TITLES)));
	}
	
	private void clearData() throws Exception {
		utx.begin();
		em.joinTransaction();
		_logger.info("Dumping old records...");
	
		String fetchingAllLivraisonsInJpql = "select l from Livraison l order by l.id";
	
		_logger.info("CLEAN DATA - Selecting (using JPQL)...");
		List<Livraison> livraisons = em.createQuery(
				fetchingAllLivraisonsInJpql, Livraison.class).getResultList();
	
		_logger.info("Found " + livraisons.size() + " livraisons (using JPQL):");
	
		for (Livraison livraison : livraisons) {
			if (livraison != null) {
				em.remove(livraison);
				_logger.info("em.remove(livraison)");
			}
		}
	
		String fetchingAllChangementsInJpql = "select c from Changement c order by c.id";
	
		_logger.info("CLEAN DATA - Selecting (using JPQL)...");
		List<Changement> changements = em.createQuery(
				fetchingAllChangementsInJpql, Changement.class).getResultList();
	
		_logger.info("Found " + changements.size()
				+ " changements (using JPQL):");
	
		for (Changement changement : changements) {
			if (changement != null) {
				em.remove(changement);
				_logger.info("em.remove(changement)");
			}
		}
	
		String fetchingAllDemandsInJpql = "select d from Demand d order by d.id";
	
		_logger.info("CLEAN DATA - Selecting (using JPQL)...");
		List<Demand> demandes = em.createQuery(fetchingAllDemandsInJpql,
				Demand.class).getResultList();
	
		_logger.info("Found " + demandes.size() + " demandes (using JPQL):");
	
		for (Demand demande : demandes) {
			if (demande != null) {
				em.remove(demande);
				_logger.info("em.remove(demande)");
			}
		}
	
		utx.commit();
	}

	/**
	 * 	// ---------> livraison : Document
		// 				|_____________changement : Correction
		// 					|_____________demande : TestDemand2
		// 					|_____________demande : TestDemand3
		// 				|_____________changement : Evolution
		// 					|_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// 				|_____________changement : TestChangement4
		// 					|_____________demande : TestDemand6
		// 				|_____________changement : TestChangement3
		// 					|_____________demande : TestDemand5
		// 					|_____________demande : TestDemand4
	 * 
	 */
	private void insertData() throws Exception {
		
		Set<Demand> demands = new HashSet<Demand>();
		Set<Demand> demands2 = new HashSet<Demand>();
		Set<Demand> demands3 = new HashSet<Demand>();
		Set<Demand> demands4 = new HashSet<Demand>();
		Set<Demand> demands5 = new HashSet<Demand>();
		Set<Demand> demands6 = new HashSet<Demand>();
		Set<Changement> changements = new HashSet<Changement>();
		Set<Changement> changements2 = new HashSet<Changement>();
		Livraison livraison = new Livraison();
		Livraison livraison2 = new Livraison();
	
		// DEMAND
	
		demand.setDescription("TestDemand");
		demand2.setDescription("TestDemand2");
		demand3.setDescription("TestDemand3");
		demand4.setDescription("TestDemand4");
		demand5.setDescription("TestDemand5");
		demand6.setDescription("TestDemand6");
	
		utx.begin();
		em.joinTransaction();
		_logger.info("Inserting records...demand");
	
		em.persist(demand);
		em.persist(demand2);
		em.persist(demand3);
		em.persist(demand4);
		em.persist(demand5);
		em.persist(demand6);
	
		demands.add(demand);
		demands2.add(demand2);
		demands3.add(demand3);
		demands4.add(demand4);
		demands5.add(demand5);
		demands6.add(demand6);
	
		// CHANGEMENT
	
		changement.setDescription("Evolution");
		changement2.setDescription("Correction");
		changement3.setDescription("TestChangement3");
		changement4.setDescription("TestChangement4");
	
		_logger.info("Getting and inserting demands to changement - 1");
		changement.getDemandes().addAll(demands);
		changement2.getDemandes().addAll(demands3);
		changement3.getDemandes().addAll(demands4);
		changement3.getDemandes().addAll(demands5);
		changement4.getDemandes().addAll(demands6);
	
		_logger.info("Getting and inserting changement to demand - 2");
		demand.getTravaux().add(changement);
		demand2.getTravaux().add(changement2);
		demand3.getTravaux().add(changement2);
		demand4.getTravaux().add(changement3);
		demand5.getTravaux().add(changement3);
		demand6.getTravaux().add(changement4);
	
		em.persist(changement);
		em.persist(changement2);
		em.persist(changement3);
		em.persist(changement4);
		em.persist(demand);
		em.persist(demand2);
		em.persist(demand3);
		em.persist(demand4);
		em.persist(demand5);
		em.persist(demand6);
	
		changements.add(changement);
		changements.add(changement2);
		changements2.add(changement3);
		changements2.add(changement4);
	
		livraison.setDescription(LIVRAISON_TITLES[0]);
		livraison.getChangements().addAll(changements);
	
		livraison2.setDescription(LIVRAISON_TITLES[1]);
		livraison2.getChangements().addAll(changements2);
	
		_logger.info("Inserting records...livraison...with changements & demands");
		em.persist(livraison);
		em.persist(livraison2);
	
		utx.commit();
		// clear the persistence context (first-level cache)
		em.clear();
	}

	private void starttransaction() throws Exception {
		utx.begin();
		em.joinTransaction();
	}

	@After
	public void committransaction() throws Exception {
		utx.commit();
	}

}