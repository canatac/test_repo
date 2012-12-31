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
import org.ortens.bone.core.model.Activity;
import org.ortens.bone.core.model.Activity_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class ActivityPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Activity.class, BaseEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] ACTIVITY_TITLES = {
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
        startTransaction();
    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from Activity").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : ACTIVITY_TITLES) {
            Activity activity = new Activity();
            activity.setDescription(title);
            em.persist(activity);
        }
        utx.commit();
        // clear the persistence context (first-level cache)
        em.clear();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }
    
    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }
    @Test
    public void shouldFindAllActivitiesUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllActivitiesInJpql = "select g from Activity g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Activity> activities = em.createQuery(fetchingAllActivitiesInJpql, Activity.class).getResultList();

        // then
        System.out.println("Found " + activities.size() + " activities (using JPQL):");
        assertContainsAllActivities(activities);
    }
    
    private static void assertContainsAllActivities(Collection<Activity> retrievedActivities) {
        Assert.assertEquals(ACTIVITY_TITLES.length, retrievedActivities.size());
        final Set<String> retrievedActivityTitles = new HashSet<String>();
        for (Activity activity : retrievedActivities) {
            System.out.println("* " + activity);
            retrievedActivityTitles.add(activity.getDescription());
        }
        Assert.assertTrue(retrievedActivityTitles.containsAll(Arrays.asList(ACTIVITY_TITLES)));
    }
    
    @Test
    public void shouldFindAllActivitiesUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Activity> criteria = builder.createQuery(Activity.class);
        		
        Root<Activity> activity = criteria.from(Activity.class);
        criteria.select(activity);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(activity.get(Activity_.id)));
        criteria.orderBy(builder.asc(activity.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Activity> activities = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + activities.size() + " activities (using Criteria):");
        assertContainsAllActivities(activities);
    }
}