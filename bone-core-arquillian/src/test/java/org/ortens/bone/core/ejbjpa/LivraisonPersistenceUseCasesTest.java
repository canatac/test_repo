package org.ortens.bone.core.ejbjpa;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
	
	public static Logger _logger = Logger.getLogger(LivraisonPersistenceUseCasesTest.class.getName());
	
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(BaseEntity.class, Demand.class, Livraison.class, Changement.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] LIVRAISON_TITLES = {
        "Document",
        "Software"
    };
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    UserTransaction utx;
    @Inject
    Changement changement;
    @Inject
    Changement changement2;
    @Inject
    Changement changement3;
    @Inject
    Changement changement4;
    @Inject
    Demand demand;
    @Inject
    Demand demand2;
    @Inject
    Demand demand3;
    @Inject
    Demand demand4;
    @Inject
    Demand demand5;
    @Inject
    Demand demand6;
 
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

        // when
        _logger.info("CLEAN DATA - Selecting (using JPQL)...");
        List<Livraison> livraisons = em.createQuery(fetchingAllLivraisonsInJpql, Livraison.class).getResultList();

        // then
        _logger.info("Found " + livraisons.size() + " livraisons (using JPQL):");
              
        for (Livraison livraison : livraisons) {
        	if (livraison != null){
        		em.remove(livraison);
        		_logger.info("em.remove(livraison)");
        		}
        }
        utx.commit();
        
    }

    private void insertData() throws Exception {
    	/*
    	 *	livraison
    	 * 		|__changement
    	 * 				|__demand
    	 *		|__changement2
    	 *				|__demand2
    	 *				|__demand3
    	 *	livraison2
    	 *		|__changement3
    	 *				|__demand4
    	 *				|__demand5
    	 *		|__changement4
    	 *				|__demand6
    	 */
    	
    	
    	demand.setDescription("TestDemand");
    	Set<Demand> demands = new HashSet<Demand>();
    	demands.add(demand);
    	
    	demand2.setDescription("TestDemand2");
    	Set<Demand> demands2 = new HashSet<Demand>();
    	demands2.add(demand2);
    	
    	demand3.setDescription("TestDemand3");
    	Set<Demand> demands3 = new HashSet<Demand>();
    	demands3.add(demand3);
    	
    	demand4.setDescription("TestDemand4");
    	Set<Demand> demands4 = new HashSet<Demand>();
    	demands4.add(demand4);
    	
    	demand5.setDescription("TestDemand5");
    	Set<Demand> demands5 = new HashSet<Demand>();
    	demands5.add(demand5);
    	
    	demand6.setDescription("TestDemand6");
    	Set<Demand> demands6 = new HashSet<Demand>();
    	demands6.add(demand6);
    	
    	//
    	
    	changement.setDescription("TestChangement");
    	changement.getDemandes().addAll(demands);
    	Set<Changement> changements = new HashSet<Changement>();
    	changements.add(changement);

    	changement2.setDescription("TestChangement2");
    	changement2.getDemandes().addAll(demands2);
    	changement2.getDemandes().addAll(demands3);
    	Set<Changement> changements2 = new HashSet<Changement>();
    	changements2.add(changement2);
    	
    	changement3.setDescription("TestChangement3");
    	changement3.getDemandes().addAll(demands4);
    	changement3.getDemandes().addAll(demands5);
    	Set<Changement> changements3 = new HashSet<Changement>();
    	changements3.add(changement3);
    	
    	changement4.setDescription("TestChangement4");
    	changement4.getDemandes().addAll(demands6);
    	Set<Changement> changements4 = new HashSet<Changement>();
    	changements4.add(changement4);
  	
        utx.begin();
        em.joinTransaction();
        _logger.info("Inserting records...");

        em.persist(demand);
        em.persist(demand2);
        em.persist(demand3);
        em.persist(demand4);
        em.persist(demand5);
        em.persist(demand6);
        
        em.persist(changement);
        em.persist(changement2);
        em.persist(changement3);
        em.persist(changement4);

        Livraison livraison = new Livraison();
        Livraison livraison2 = new Livraison(); 
        
        livraison.setDescription(LIVRAISON_TITLES[0]);
        livraison.getChangements().add(changement);
        livraison.getChangements().add(changement2);

        livraison2.setDescription(LIVRAISON_TITLES[1]);
        livraison2.getChangements().add(changement3);
        livraison2.getChangements().add(changement4);
            
        
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
        // given
        String fetchingAllLivraisonsInJpql = "select l from Livraison l order by l.id";

        // when
        _logger.info("Selecting (using JPQL)...");
        List<Livraison> livraisons = em.createQuery(fetchingAllLivraisonsInJpql, Livraison.class).getResultList();
        
        // then
        _logger.info("Found " + livraisons.size() + " livraisons (using JPQL):");
        
        assertContainsAllLivraisons(livraisons);
    }
    
    private static void assertContainsAllLivraisons(Collection<Livraison> retrievedLivraisons) {
        Assert.assertEquals(LIVRAISON_TITLES.length, retrievedLivraisons.size());
        
        final Set<String> retrievedLivraisonTitles = new HashSet<String>();
        for (Livraison livraison : retrievedLivraisons) {
            _logger.info("* " + livraison);
            Assert.assertEquals(2,livraison.getChangements().size());
            
            Iterator<Changement> it = livraison.getChangements().iterator();
            
            while (it.hasNext()){
            	_logger.info("YES un changement !");
            	_logger.info(it.next().getDescription());
            }
            retrievedLivraisonTitles.add(livraison.getDescription());
        }
        Assert.assertTrue(retrievedLivraisonTitles.containsAll(Arrays.asList(LIVRAISON_TITLES)));
        
    }
    
    @Test
    public void shouldFindAllLivraisonsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Livraison> criteria = builder.createQuery(Livraison.class);
        		
        Root<Livraison> livraison = criteria.from(Livraison.class);
        criteria.select(livraison);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(changement.get(Changement_.id)));
        criteria.orderBy(builder.asc(livraison.get("id")));
        // No WHERE clause, which implies select all

        // when
        _logger.info("Selecting (using Criteria)...");
        List<Livraison> livraisons = em.createQuery(criteria).getResultList();

        // then
        _logger.info("Found " + livraisons.size() + " livraisons (using Criteria):");
        assertContainsAllLivraisons(livraisons);
    }
}