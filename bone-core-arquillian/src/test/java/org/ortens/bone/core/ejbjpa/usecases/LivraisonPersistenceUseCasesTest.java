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
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Assert;
import org.ortens.bone.core.model.BaseEntity;
import org.ortens.bone.core.model.Changement;
import org.ortens.bone.core.model.Demand;
import org.ortens.bone.core.model.Livraison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class LivraisonPersistenceUseCasesTest {

	public static Logger _logger = Logger
			.getLogger(LivraisonPersistenceUseCasesTest.class.getName());

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(BaseEntity.class, Demand.class, Livraison.class,
						Changement.class)
				.addAsResource("test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("jbossas-ds.xml"); // arquillian-jbossas-managed
														// profile. Can stay
														// without perturbing
														// arquillian-glassfish-embedded
														// test
	}

	private static final String[] LIVRAISON_TITLES = { "Document", "Software" };

	@PersistenceContext
	EntityManager em;

	@Inject
	UserTransaction utx;
	@Inject
	Changement changement, changement2, changement3, changement4;

	@Inject
	Demand demand, demand2, demand3, demand4, demand5, demand6;

	@Inject
	Livraison livraison;

	@Before
	public void preparePersistenceTest() throws Exception {
		clearData();
		insertData();
		starttransaction();
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

	private void insertData() throws Exception {
		// ---------> livraison : Document
		// |_____________changement : Correction
		// |_____________demande : TestDemand2
		// |_____________demande : TestDemand3
		// |_____________changement : Evolution
		// |_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// |_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// |_____________demande : TestDemand5
		// |_____________demande : TestDemand4

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

		// _logger.info("		|_____________changement.getDemandes().size() : "+changement.getDemandes().size());
		// _logger.info("		|_____________changement2.getDemandes().size() : "+changement2.getDemandes().size());
		// _logger.info("		|_____________changement3.getDemandes().size() : "+changement3.getDemandes().size());
		// _logger.info("		|_____________changement4.getDemandes().size() : "+changement4.getDemandes().size());

		_logger.info("Getting and inserting changement to demand - 2");
		demand.getTravaux().add(changement);
		demand2.getTravaux().add(changement2);
		demand3.getTravaux().add(changement2);
		demand4.getTravaux().add(changement3);
		demand5.getTravaux().add(changement3);
		demand6.getTravaux().add(changement4);

		// _logger.info("		|_____________demand.getTravaux().size() : "+demand.getTravaux().size());
		// _logger.info("		|_____________demand2.getTravaux().size() : "+demand2.getTravaux().size());
		// _logger.info("		|_____________demand3.getTravaux().size() : "+demand3.getTravaux().size());
		// _logger.info("		|_____________demand4.getTravaux().size() : "+demand4.getTravaux().size());
		// _logger.info("		|_____________demand5.getTravaux().size() : "+demand5.getTravaux().size());
		// _logger.info("		|_____________demand6.getTravaux().size() : "+demand6.getTravaux().size());

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

	@Test
	public void shouldFindAllLivraisonsUsingJpqlQuery() throws Exception {
		_logger.info("==============>>>>>>>>>> INTO TEST : shouldFindAllLivraisonsUsingJpqlQuery()");
		// given
		String fetchingAllLivraisonsInJpql = "select l from Livraison l order by l.id";

		// when
		_logger.info("Selecting (using JPQL)...");
		List<Livraison> livraisons = em.createQuery(
				fetchingAllLivraisonsInJpql, Livraison.class).getResultList();

		// then
		_logger.info("Found " + livraisons.size() + " livraisons (using JPQL):");

		assertContainsAllLivraisons(livraisons);
		_logger.info("==============>>>>>>>>>> OUT OF TEST : shouldFindAllLivraisonsUsingJpqlQuery()");
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

	@Test
	public void moveAChangeWithDemandsToAnotherLivraison()
			throws SecurityException, IllegalStateException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException,
			SystemException, NotSupportedException {
		_logger.info("==============>>>>>>>>>> INTO TEST : moveAChangeWithDemandsToAnotherLivraison()");
		// 2.Move a change with the demands
		// |__2.1 - To another Livraison
		//
		// BEFORE :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// |_____________demande : TestDemand2
		// |_____________demande : TestDemand3
		// |_____________changement : Evolution
		// |_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// |_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// |_____________demande : TestDemand5
		// |_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Evolution
		// |_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// |_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// |_____________demande : TestDemand5
		// |_____________demande : TestDemand4
		// |_____________changement : Correction
		// |_____________demande : TestDemand2
		// |_____________demande : TestDemand3

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

		// ==>
		utx.commit();
		em.clear();
		// -------------------->> VERIFICATION
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

		_logger.info("==============>>>>>>>>>> OUT OF TEST : moveAChangeWithDemandsToAnotherLivraison()");

	}

	@Test
	public void moveADemand() throws SecurityException, IllegalStateException,
			RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException {
		_logger.info("==============>>>>>>>>>> INTO TEST : moveADemand()");
		// 1.Move a demand
		// |__1.1 - To another Changement but same Livraison
		// |
		// |__1.2 - To another Changement and other Livraison
		//

		// 1.1 - Move a demand to another Changement but same Livraison

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

		// 1.2 - Move a demand to another Changement and another Livraison
		String fetchingAnotherDemandInJpql = "select d from Demand d where d.description = 'TestDemand2'";

		_logger.info("Selecting (using JPQL)...");
		Demand anotherDemand = em.createQuery(fetchingAnotherDemandInJpql,
				Demand.class).getSingleResult();

		_logger.info("Found demand (using JPQL) : "
				+ anotherDemand.getDescription());
		_logger.info("			|__ Nb of linked changements : "
				+ anotherDemand.getTravaux().size());
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
		_logger.info("==============>>>>>>>>>> OUT OF TEST : moveADemand()");
	}

	@Test
	public void updateOneItem() throws SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException {
		_logger.info("==============>>>>>>>>>> INTO TEST : updateOneItem()");
		//
		// BEFORE :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// |_____________demande : TestDemand2
		// |_____________demande : TestDemand3
		// |_____________changement : Evolution
		// |_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// |_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// |_____________demande : TestDemand5
		// |_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// |_____________demande : Correction1
		// |_____________demande : TestDemand3
		// |_____________changement : Evolution
		// |_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// |_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// |_____________demande : TestDemand5
		// |_____________demande : TestDemand4

		String description = "Correction1";
		String descriptionOLD = "TestDemand2";
		Query q = em
				.createQuery("UPDATE Demand d SET d.description = :description WHERE d.description = :descriptionOLD");
		q.setParameter("description", description);
		q.setParameter("descriptionOLD", descriptionOLD);
		q.executeUpdate();

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

	@Test
	public void deleteOneItem() throws SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException {
		//
		// BEFORE :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// |_____________demande : TestDemand2
		// |_____________demande : TestDemand3
		// |_____________changement : Evolution
		// |_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// |_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// |_____________demande : TestDemand5
		// |_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// |_____________XXXXXXXXXXXXXXXXXXXXX
		// |_____________demande : TestDemand3
		// |_____________changement : Evolution
		// |_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// |_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// |_____________demande : TestDemand5
		// |_____________demande : TestDemand4
		_logger.info("==============>>>>>>>>>> INTO TEST : deleteOneItem()");
		String description = "TestDemand2";

		String fetchingADemandInJpql = "select d from Demand d where d.description = '"
				+ description + "'";

		Demand demand = em.createQuery(fetchingADemandInJpql, Demand.class)
				.getSingleResult();

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

	@Test
	public void addOneItem() {
		//
		// BEFORE :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// |_____________demande : TestDemand2
		// |_____________demande : TestDemand3
		// |_____________changement : Evolution
		// |_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// |_____________demande : TestDemand6
		// |_____________changement : TestChangement3
		// |_____________demande : TestDemand5
		// |_____________demande : TestDemand4
		// AFTER :
		// ---------
		// ---------> livraison : Document
		// |_____________changement : Correction
		// |_____________demande : TestDemand2
		// |_____________demande : TestDemand3
		// |_____________changement : Evolution
		// |_____________demande : TestDemand
		//
		// ---------> livraison : Software
		// |_____________changement : TestChangement4
		// |_____________demande : TestDemand6
		// |_____________demande : TestDemandX
		// |_____________changement : TestChangement3
		// |_____________demande : TestDemand5
		// |_____________demande : TestDemand4
		_logger.info("==============>>>>>>>>>> INTO TEST : addOneItem()");
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

		//

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
	public void moveSeveralItems() throws SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException {
		//
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

		_logger.info("==============>>>>>>>>>> INTO TEST : moveSeveralItems()");
		// Move TestChangement3
		String sChange = "TestChangement3";
		String sChange2 = "Correction";
		String fetchingChangementsInJpql = "select c from Changement c where c.description = '"
				+ sChange + "'";
		String fetchingChangementsInJpql2 = "select c from Changement c where c.description = '"
				+ sChange2 + "'";
		Changement changement = em.createQuery(fetchingChangementsInJpql,
				Changement.class).getSingleResult();
		Changement changement2 = em.createQuery(fetchingChangementsInJpql2,
				Changement.class).getSingleResult();

		String fetchingLivraisonInJpql = "select l from Livraison l where l.description = 'Software'";
		String fetchingLivraisonInJpqlNEW = "select l from Livraison l where l.description = 'Document'";
		Livraison livraison = em.createQuery(fetchingLivraisonInJpql,
				Livraison.class).getSingleResult();
		Livraison livraisonNEW = em.createQuery(fetchingLivraisonInJpqlNEW,
				Livraison.class).getSingleResult();

		_logger.info("livraison.getChangements().size() : "
				+ livraison.getChangements().size());
		_logger.info("livraison.getChangements().toString() : "
				+ livraison.getChangements().toString());

		_logger.info("livraisonNEW.getChangements().size() : "
				+ livraisonNEW.getChangements().size());
		_logger.info("livraisonNEW.getChangements().toString() : "
				+ livraisonNEW.getChangements().toString());

		Iterator<Changement> it = livraison.getChangements().iterator();
		Iterator<Changement> it2 = livraisonNEW.getChangements().iterator();

		while (it.hasNext()) {
			if (sChange.equals(it.next().getDescription())) {
				it.remove();
				_logger.info("					|__ changement  1 : REMOVED !");
			}
		}
		em.flush();

		// Move Correction
		while (it2.hasNext()) {
			if (sChange2.equals(it2.next().getDescription())) {
				it2.remove();
				_logger.info("					|__ changement  2 : REMOVED !");
			}
		}

		livraison.getChangements().add(changement2);
		livraisonNEW.getChangements().add(changement);

		em.merge(livraison);
		em.merge(livraisonNEW);
		
		em.persist(livraison);
		em.persist(livraisonNEW);

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

	 @Test
	public void updateSeveralItems() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
			//
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

	 @Test
	public void deleteSeveralItems() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
			// DELETE ALL THE DEMANDS LINKED TO A LIVRAISON
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
		_logger.info("==============>>>>>>>>>> INTO TEST : deleteSeveralItems()");
		
		String queryLivraison = "select l from Livraison l where l.description = 'Document'";
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
		
		utx.commit();
		
		// ----> VERIFICATION
		
		utx.begin();
		em.joinTransaction();
		
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

	 @Test
	public void addSeveralItems() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
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
		 _logger.info("==============>>>>>>>>>> INTO TEST : addSeveralItems()");
		 String query = "select c from Changement c where c.description = 'TestChangement3'";
		 Changement changement = em.createQuery(query, Changement.class).getSingleResult();
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

	// @Test
	public void cloneOneItem() {
	}

	// @Test
	public void cloneSeveralItems() {
	}
}