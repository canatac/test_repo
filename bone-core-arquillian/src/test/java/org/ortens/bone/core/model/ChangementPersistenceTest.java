package org.ortens.bone.core.model;

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
import org.junit.Assert;
import org.ortens.bone.core.model.Changement;
import org.ortens.bone.core.model.Changement_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class ChangementPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Changement.class, Demand.class, Livraison.class,BaseEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] CHANGEMENT_TITLES = {
        "Document Change",
        "Software evolution",
        "Budget evolution"
    };
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    UserTransaction utx;
    @Inject
    Demand demand;
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
        System.out.println("Dumping old records...");
        //em.createQuery("delete from Changement").executeUpdate();
        
        
        String fetchingAllChangementsInJpql = "select g from Changement g order by g.id";
        String fetchingAllLivraisonsInJpql = "select l from Livraison l order by l.id";
        String fetchingAllDemandsInJpql = "select d from Demand d order by d.id";

        // when
        System.out.println("CLEAN DATA - Selecting (using JPQL)...");
        List<Changement> changements = em.createQuery(fetchingAllChangementsInJpql, Changement.class).getResultList();
        List<Livraison> livraisons = em.createQuery(fetchingAllLivraisonsInJpql, Livraison.class).getResultList();
        List<Demand> demands = em.createQuery(fetchingAllDemandsInJpql, Demand.class).getResultList();

        // then
        System.out.println("Found " + changements.size() + " changements (using JPQL):");
        System.out.println("Found " + livraisons.size() + " livraisons (using JPQL):");
        System.out.println("Found " + demands.size() + " demands (using JPQL):");
        
        for (Changement changement : changements) {
            System.out.println("* " + changement);
            Iterator<Demand> it = changement.getDemandes().iterator();
            while (it.hasNext()){
            	System.out.println("YES une demande ! => remove");
            	Demand demand = it.next();
            	changement.getDemandes().remove(demand);
            }
            Iterator<Livraison> it2 = changement.getLivrables().iterator();
            while (it2.hasNext()){
            	System.out.println("YES une livraison ! => remove");
            	Livraison livraison = it2.next();
            	changement.getLivrables().remove(livraison);
            }
            em.merge(changement);
        }
        
//        for (Livraison livraison : livraisons) {
//            System.out.println("* " + livraison);
//            Iterator<Changement> it3 = livraison.getChangements().iterator();
//            while (it3.hasNext()){
//            	System.out.println("YES un changement ! => remove");
//            	it3.remove();
//            }
//            em.persist(livraison);
//        }
//        for (Demand demand : demands) {
//            System.out.println("* " + demand);
//            Iterator<Changement> it4 = demand.getTravaux().iterator();
//            while (it4.hasNext()){
//            	System.out.println("YES un changement ! => remove");
//            	it4.remove();
//            }
//            em.persist(demand);
//        }
        
        utx.commit();
        
        utx.begin();
        em.joinTransaction();
        em.createQuery("delete from Livraison").executeUpdate();
        em.createQuery("delete from Demand").executeUpdate();
        em.createQuery("delete from Changement").executeUpdate();
        utx.commit();
        
        
    }

    private void insertData() throws Exception {
    	demand.setDescription("toto");
    	Set<Demand> demandes = new HashSet<Demand>();
    	demandes.add(demand);
    	livraison.setDescription("toto");
    	Set<Livraison> livraisons = new HashSet<Livraison>();
    	livraisons.add(livraison);
    	
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : CHANGEMENT_TITLES) {
            Changement changement = new Changement();
            
            em.persist(livraison);
            em.persist(demand);
            //em.persist(changement);
            
            changement.setDescription(title);
            changement.getDemandes().add(demand);
            changement.getLivrables().add(livraison);
            demand.getTravaux().add(changement);
            livraison.getChangements().add(changement);
            
            em.persist(livraison);
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
    public void shouldFindAllChangementsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllChangementsInJpql = "select g from Changement g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Changement> changements = em.createQuery(fetchingAllChangementsInJpql, Changement.class).getResultList();

        // then
        System.out.println("Found " + changements.size() + " changements (using JPQL):");
        
        assertContainsAllChangements(changements);
    }
    
    private static void assertContainsAllChangements(Collection<Changement> retrievedChangements) {
        Assert.assertEquals(CHANGEMENT_TITLES.length, retrievedChangements.size());
        final Set<String> retrievedChangementTitles = new HashSet<String>();
        for (Changement changement : retrievedChangements) {
            System.out.println("* " + changement);
            Iterator<Demand> it = changement.getDemandes().iterator();
            while (it.hasNext()){
            	System.out.println("YES une demande !");
            	System.out.println(it.next());
            }
            Iterator<Livraison> it2 = changement.getLivrables().iterator();
            while (it2.hasNext()){
            	System.out.println("YES une livraison !");
            	System.out.println(it2.next());
            }
            retrievedChangementTitles.add(changement.getDescription());
        }
        Assert.assertTrue(retrievedChangementTitles.containsAll(Arrays.asList(CHANGEMENT_TITLES)));
    }
    
    @Test
    public void shouldFindAllChangementsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Changement> criteria = builder.createQuery(Changement.class);
        		
        Root<Changement> changement = criteria.from(Changement.class);
        criteria.select(changement);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(changement.get(Changement_.id)));
        criteria.orderBy(builder.asc(changement.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Changement> changements = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + changements.size() + " changements (using Criteria):");
        assertContainsAllChangements(changements);
    }
}