package org.ortens.bone.core.ejbjpa.usecases;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ortens.bone.core.ejbjpa.ChangementDao;
import org.ortens.bone.core.ejbjpa.DemandDao;
import org.ortens.bone.core.ejbjpa.LivraisonDao;
import org.ortens.bone.core.model.BaseEntity;
import org.ortens.bone.core.model.Changement;
import org.ortens.bone.core.model.Demand;
import org.ortens.bone.core.model.Livraison;

@RunWith(Arquillian.class)
public class DemandPersistenceUseCasesTest {
	public static Logger _logger = Logger
			.getLogger(DemandPersistenceUseCasesTest.class.getName());

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(BaseEntity.class, Demand.class, Livraison.class,
						Changement.class, LivraisonDao.class, DemandDao.class,
						ChangementDao.class)
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
	protected static final String[] DEMAND_TITLES = { "TestDemand2", "TestDemand3", 
		"TestDemand", "TestDemand6","TestDemand5", "TestDemand4" };

	
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
	Changement changement, changement2, changement3, changement4;
	@Inject
	Demand demand, demand2, demand3, demand4, demand5, demand6;
	@Inject
	Livraison livraison;

	@Before
	public void preparePersistenceTest() throws Exception {
		_logger.setLevel(Level.INFO);
		clearData();
		insertData();
		starttransaction();
	}

	@After
	public void committransaction() throws Exception {
		utx.commit();
	}

	@Test
	public void getListTest(){
		_logger.info("==============>>>>>>>>>> INTO TEST : getListTest()");
		List<Demand> demands = demandDao.getList();
		
		assertContainsAllDemands(demands);
		_logger.info("==============>>>>>>>>>> OUT OF TEST : getListTest()");
	}
	
	private static void assertContainsAllDemands(
			Collection<Demand> retrievedDemands) {
		Assert.assertEquals(DEMAND_TITLES.length, retrievedDemands.size());

		final Set<String> retrievedDemandTitles = new HashSet<String>();
		for (Demand demand : retrievedDemands) {
			_logger.info("* " + demand);
			_logger.info("---------> demand.getDescription() : "
					+ demand.getDescription());
			for (Changement changement : demand.getTravaux()) {
				_logger.info("		|_____________changement : "
						+ changement.getDescription());
				for (Livraison livraison : changement.getLivrables()) {
					_logger.info("			|_____________livraison : "
							+ livraison.getDescription());
				}
			}
			retrievedDemandTitles.add(demand.getDescription());
		}
		Assert.assertTrue(retrievedDemandTitles.containsAll(Arrays
				.asList(DEMAND_TITLES)));
	}
	
	
	private void clearData() throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
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

}
