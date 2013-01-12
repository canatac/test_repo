package org.ortens.bone.core.ejbjpa;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
import org.ortens.bone.core.model.ActionPlan;
import org.ortens.bone.core.model.GenericEntity;

@RunWith(Arquillian.class)
public class ActionPlanPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(ActionPlan.class, GenericEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] ACTIONPLAN_TITLES = {
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
        em.createQuery("delete from ActionPlan").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : ACTIONPLAN_TITLES) {
            ActionPlan actionPlan = new ActionPlan();
            actionPlan.setDescription(title);
            em.persist(actionPlan);
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
    public void shouldFindAllActionPlansUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllActionPlansInJpql = "select g from ActionPlan g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<ActionPlan> actionPlans = em.createQuery(fetchingAllActionPlansInJpql, ActionPlan.class).getResultList();

        // then
        System.out.println("Found " + actionPlans.size() + " actionPlans (using JPQL):");
        assertContainsAllActionPlans(actionPlans);
    }
    
    private static void assertContainsAllActionPlans(Collection<ActionPlan> retrievedActionPlans) {
        Assert.assertEquals(ACTIONPLAN_TITLES.length, retrievedActionPlans.size());
        final Set<String> retrievedActionPlanTitles = new HashSet<String>();
        for (ActionPlan actionPlan : retrievedActionPlans) {
            System.out.println("* " + actionPlan);
            retrievedActionPlanTitles.add(actionPlan.getDescription());
        }
        Assert.assertTrue(retrievedActionPlanTitles.containsAll(Arrays.asList(ACTIONPLAN_TITLES)));
    }
    
    @Test
    public void shouldFindAllActionPlansUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ActionPlan> criteria = builder.createQuery(ActionPlan.class);
        		
        Root<ActionPlan> actionPlan = criteria.from(ActionPlan.class);
        criteria.select(actionPlan);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(actionPlan.get(ActionPlan_.id)));
        criteria.orderBy(builder.asc(actionPlan.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<ActionPlan> actionPlans = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + actionPlans.size() + " actionPlans (using Criteria):");
        assertContainsAllActionPlans(actionPlans);
    }
}