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
public class DemandPersistenceTest {
	
	public static Logger _logger = Logger.getLogger(DemandPersistenceTest.class.getName());
	
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(BaseEntity.class, Demand.class, Livraison.class, Changement.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] DEMAND_TITLES = {
        "Evolution",
        "Correction",
        "Support"
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
        
        String fetchingAllDemandsInJpql = "select d from Demand d order by d.id";

        // when
        _logger.info("CLEAN DATA - Selecting (using JPQL)...");
        List<Demand> demands = em.createQuery(fetchingAllDemandsInJpql, Demand.class).getResultList();

        // then
        _logger.info("Found " + demands.size() + " demands (using JPQL):");
              
        for (Demand demand : demands) {
        	if (demand != null){
        		em.remove(demand);
        		_logger.info("em.remove(demand)");
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

        for (String title : DEMAND_TITLES) {

            Demand demand = new Demand();
            
            em.persist(changement);
            
            demand.setDescription(title);
            demand.getTravaux().add(changement);
            
            em.persist(demand);
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
    public void shouldFindAllDemandsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllDemandsInJpql = "select d from Demand d order by d.id";

        // when
        _logger.info("Selecting (using JPQL)...");
        List<Demand> demands = em.createQuery(fetchingAllDemandsInJpql, Demand.class).getResultList();
        
        // then
        _logger.info("Found " + demands.size() + " demands (using JPQL):");
        
        assertContainsAllDemands(demands);
    }
    
    private static void assertContainsAllDemands(Collection<Demand> retrievedDemands) {
        Assert.assertEquals(DEMAND_TITLES.length, retrievedDemands.size());
        final Set<String> retrievedDemandTitles = new HashSet<String>();
        for (Demand demand : retrievedDemands) {
            _logger.info("* " + demand);
            Iterator<Changement> it = demand.getTravaux().iterator();
            while (it.hasNext()){
            	_logger.info("YES un changement !");
            	_logger.info(it.next().getDescription());
            }
            retrievedDemandTitles.add(demand.getDescription());
        }
        Assert.assertTrue(retrievedDemandTitles.containsAll(Arrays.asList(DEMAND_TITLES)));
    }
    
    @Test
    public void shouldFindAllDemandsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Demand> criteria = builder.createQuery(Demand.class);
        		
        Root<Demand> demand = criteria.from(Demand.class);
        criteria.select(demand);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(changement.get(Changement_.id)));
        criteria.orderBy(builder.asc(demand.get("id")));
        // No WHERE clause, which implies select all

        // when
        _logger.info("Selecting (using Criteria)...");
        List<Demand> demands = em.createQuery(criteria).getResultList();

        // then
        _logger.info("Found " + demands.size() + " demands (using Criteria):");
        assertContainsAllDemands(demands);
    }
}