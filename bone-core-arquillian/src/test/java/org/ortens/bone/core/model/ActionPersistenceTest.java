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
import org.ortens.bone.core.model.Action;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class ActionPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Action.class, GenericEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] ACTION_TITLES = {
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
        em.createQuery("delete from Action").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : ACTION_TITLES) {
            Action action = new Action();
            action.setDescription(title);
            em.persist(action);
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
    public void shouldFindAllActionsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllActionsInJpql = "select g from Action g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Action> actions = em.createQuery(fetchingAllActionsInJpql, Action.class).getResultList();

        // then
        System.out.println("Found " + actions.size() + " actions (using JPQL):");
        assertContainsAllActions(actions);
    }
    
    private static void assertContainsAllActions(Collection<Action> retrievedActions) {
        Assert.assertEquals(ACTION_TITLES.length, retrievedActions.size());
        final Set<String> retrievedActionTitles = new HashSet<String>();
        for (Action action : retrievedActions) {
            System.out.println("* " + action);
            retrievedActionTitles.add(action.getDescription());
        }
        Assert.assertTrue(retrievedActionTitles.containsAll(Arrays.asList(ACTION_TITLES)));
    }
    
    @Test
    public void shouldFindAllActionsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Action> criteria = builder.createQuery(Action.class);
        		
        Root<Action> action = criteria.from(Action.class);
        criteria.select(action);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(action.get(Action_.id)));
        criteria.orderBy(builder.asc(action.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Action> actions = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + actions.size() + " actions (using Criteria):");
        assertContainsAllActions(actions);
    }
}