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
import org.ortens.bone.core.model.Solution;
import org.ortens.bone.core.model.Solution_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class SolutionPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Solution.class, BaseEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] SOLUTION_TITLES = {
        "BUG",
        "SYSTEM CRASH",
        "BUILD FAILURE",
        "ABSENCE"
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
        em.createQuery("delete from Solution").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : SOLUTION_TITLES) {
            Solution solution = new Solution();
            solution.setDescription(title);
            em.persist(solution);
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
    public void shouldFindAllSolutionsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllSolutionsInJpql = "select g from Solution g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Solution> solutions = em.createQuery(fetchingAllSolutionsInJpql, Solution.class).getResultList();

        // then
        System.out.println("Found " + solutions.size() + " solutions (using JPQL):");
        assertContainsAllSolutions(solutions);
    }
    
    private static void assertContainsAllSolutions(Collection<Solution> retrievedSolutions) {
        Assert.assertEquals(SOLUTION_TITLES.length, retrievedSolutions.size());
        final Set<String> retrievedSolutionTitles = new HashSet<String>();
        for (Solution solution : retrievedSolutions) {
            System.out.println("* " + solution);
            retrievedSolutionTitles.add(solution.getDescription());
        }
        Assert.assertTrue(retrievedSolutionTitles.containsAll(Arrays.asList(SOLUTION_TITLES)));
    }
    
    @Test
    public void shouldFindAllSolutionsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Solution> criteria = builder.createQuery(Solution.class);
        		
        Root<Solution> solution = criteria.from(Solution.class);
        criteria.select(solution);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(solution.get(Solution_.id)));
        criteria.orderBy(builder.asc(solution.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Solution> solutions = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + solutions.size() + " solutions (using Criteria):");
        assertContainsAllSolutions(solutions);
    }
}