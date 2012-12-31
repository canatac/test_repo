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
import java.util.Set;
import org.junit.Assert;
import org.ortens.bone.core.model.CheckPoint;
import org.ortens.bone.core.model.CheckPoint_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class CheckPointPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(CheckPoint.class, BaseEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] CHECKPOINT_TITLES = {
        "Debt Recovery",
        "Project Planning",
        "Budget Planning"
    };
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    UserTransaction utx;
 
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
        em.createQuery("delete from CheckPoint").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : CHECKPOINT_TITLES) {
            CheckPoint checkPoint = new CheckPoint();
            checkPoint.setDescription(title);
            em.persist(checkPoint);
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
    public void shouldFindAllCheckPointsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllCheckPointsInJpql = "select g from CheckPoint g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<CheckPoint> checkPoints = em.createQuery(fetchingAllCheckPointsInJpql, CheckPoint.class).getResultList();

        // then
        System.out.println("Found " + checkPoints.size() + " checkPoints (using JPQL):");
        assertContainsAllCheckPoints(checkPoints);
    }
    
    private static void assertContainsAllCheckPoints(Collection<CheckPoint> retrievedCheckPoints) {
        Assert.assertEquals(CHECKPOINT_TITLES.length, retrievedCheckPoints.size());
        final Set<String> retrievedCheckPointTitles = new HashSet<String>();
        for (CheckPoint checkPoint : retrievedCheckPoints) {
            System.out.println("* " + checkPoint);
            retrievedCheckPointTitles.add(checkPoint.getDescription());
        }
        Assert.assertTrue(retrievedCheckPointTitles.containsAll(Arrays.asList(CHECKPOINT_TITLES)));
    }
    
    @Test
    public void shouldFindAllCheckPointsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CheckPoint> criteria = builder.createQuery(CheckPoint.class);
        		
        Root<CheckPoint> checkPoint = criteria.from(CheckPoint.class);
        criteria.select(checkPoint);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(checkPoint.get(CheckPoint_.id)));
        criteria.orderBy(builder.asc(checkPoint.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<CheckPoint> checkPoints = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + checkPoints.size() + " checkPoints (using Criteria):");
        assertContainsAllCheckPoints(checkPoints);
    }
}