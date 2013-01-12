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
import org.ortens.bone.core.model.GenericEntity;
import org.ortens.bone.core.model.Changement;
import org.ortens.bone.core.model.Demand;
import org.ortens.bone.core.model.Livraison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class LivraisonPersistenceTest {
	
	public static Logger _logger = Logger.getLogger(LivraisonPersistenceTest.class.getName());
	
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(GenericEntity.class, Demand.class, Livraison.class, Changement.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] LIVRAISON_TITLES = {
        "Document",
        "Software",
        "Hardware"
    };
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    UserTransaction utx;
    @Inject
    Changement changement;
 
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
    	
    	
    	changement.setDescription("TestChangement");
    	Set<Changement> changements = new HashSet<Changement>();
    	changements.add(changement);
    	
        utx.begin();
        em.joinTransaction();
        _logger.info("Inserting records...");

        for (String title : LIVRAISON_TITLES) {

            Livraison livraison = new Livraison();
            
            em.persist(changement);
            
            livraison.setDescription(title);
            livraison.getChangements().add(changement);
            
            em.persist(livraison);
            em.persist(changement);
        }
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