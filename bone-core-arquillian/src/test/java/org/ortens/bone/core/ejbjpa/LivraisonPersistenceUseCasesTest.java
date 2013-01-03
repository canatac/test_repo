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
//        utx.commit();
    	
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
    	
//    	utx.begin();
//        em.joinTransaction();
        //_logger.info("Inserting records...changement");
        
//        em.persist(changement);
//    	em.persist(changement2);
//    	em.persist(changement3);
//    	em.persist(changement4);
//    	utx.commit();
//    	
//    	utx.begin();
//        em.joinTransaction();
        _logger.info("Getting and inserting demands to changement - 1");
    	changement.getDemandes().addAll(demands);
    	_logger.info("		|_____________changement.getDemandes().size() : "+changement.getDemandes().size());
    	changement2.getDemandes().addAll(demands3);
    	_logger.info("		|_____________changement2.getDemandes().size() : "+changement2.getDemandes().size());
    	changement3.getDemandes().addAll(demands4);
    	_logger.info("		|_____________changement3.getDemandes().size() : "+changement3.getDemandes().size());
    	changement3.getDemandes().addAll(demands5);
    	_logger.info("		|_____________changement3.getDemandes().size() : "+changement3.getDemandes().size());
    	changement4.getDemandes().addAll(demands6);
    	_logger.info("		|_____________changement4.getDemandes().size() : "+changement4.getDemandes().size());
    	
    	 _logger.info("Getting and inserting changement to demand - 2");
     	demand.getTravaux().add(changement);
     	_logger.info("		|_____________demand.getTravaux().size() : "+demand.getTravaux().size());
     	demand2.getTravaux().add(changement2);
     	_logger.info("		|_____________demand2.getTravaux().size() : "+demand2.getTravaux().size());
     	demand3.getTravaux().add(changement2);
     	_logger.info("		|_____________demand3.getTravaux().size() : "+demand3.getTravaux().size());
     	demand4.getTravaux().add(changement3);
     	_logger.info("		|_____________demand4.getTravaux().size() : "+demand4.getTravaux().size());
     	demand5.getTravaux().add(changement3);
     	_logger.info("		|_____________demand5.getTravaux().size() : "+demand5.getTravaux().size());
     	demand6.getTravaux().add(changement4);
     	_logger.info("		|_____________demand6.getTravaux().size() : "+demand6.getTravaux().size());
    	
//    	em.flush();
//    	em.refresh(changement);
//    	em.refresh(changement2);
//    	em.refresh(changement3);
//    	em.refresh(changement4);
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
    	//    	utx.commit();
    	
    	
    	changements.add(changement);
    	changements.add(changement2);
    	changements2.add(changement3); 
    	changements2.add(changement4);

    	livraison.setDescription(LIVRAISON_TITLES[0]);
        livraison.getChangements().addAll(changements);

        livraison2.setDescription(LIVRAISON_TITLES[1]);
        livraison2.getChangements().addAll(changements2);
    	
//    	utx.begin();
//        em.joinTransaction();
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
            _logger.info("---------> livraison.getDescription() : "+livraison.getDescription());
            for (Changement changement : livraison.getChangements()){
            	_logger.info("		|_____________changement : "+changement.getDescription());
            	_logger.info("		|_____________changement.getDemandes().size() : "+changement.getDemandes().size());
            	for (Demand demand : changement.getDemandes()){
            		_logger.info("			|_____________demande : "+demand.getDescription());
            	}
            }
            retrievedLivraisonTitles.add(livraison.getDescription());
        }
        Assert.assertTrue(retrievedLivraisonTitles.containsAll(Arrays.asList(LIVRAISON_TITLES)));
//            Iterator<Changement> it = livraison.getChangements().iterator();
            
//            while (it.hasNext()){
//            	_logger.info("		|_____________changement : "+it.next().getDescription());
//            	//
//            	Iterator<Demand> itDemand = it.next().getDemandes().iterator();
//            	while (itDemand.hasNext()){
//            		_logger.info("		|_____________changement : "+it.next().getDescription());
//            	}
//            	
//            	
     
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