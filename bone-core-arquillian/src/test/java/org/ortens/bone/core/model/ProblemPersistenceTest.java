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
import org.ortens.bone.core.model.Problem;
import org.ortens.bone.core.model.Problem_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class ProblemPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Problem.class, BaseEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] PROBLEM_TITLES = {
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
        em.createQuery("delete from Problem").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : PROBLEM_TITLES) {
            Problem problem = new Problem();
            problem.setDescription(title);
            em.persist(problem);
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
    public void shouldFindAllProblemsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllProblemsInJpql = "select g from Problem g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Problem> problems = em.createQuery(fetchingAllProblemsInJpql, Problem.class).getResultList();

        // then
        System.out.println("Found " + problems.size() + " problems (using JPQL):");
        assertContainsAllProblems(problems);
    }
    
    private static void assertContainsAllProblems(Collection<Problem> retrievedProblems) {
        Assert.assertEquals(PROBLEM_TITLES.length, retrievedProblems.size());
        final Set<String> retrievedProblemTitles = new HashSet<String>();
        for (Problem problem : retrievedProblems) {
            System.out.println("* " + problem);
            retrievedProblemTitles.add(problem.getDescription());
        }
        Assert.assertTrue(retrievedProblemTitles.containsAll(Arrays.asList(PROBLEM_TITLES)));
    }
    
    @Test
    public void shouldFindAllProblemsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Problem> criteria = builder.createQuery(Problem.class);
        		
        Root<Problem> problem = criteria.from(Problem.class);
        criteria.select(problem);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(problem.get(Problem_.id)));
        criteria.orderBy(builder.asc(problem.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Problem> problems = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + problems.size() + " problems (using Criteria):");
        assertContainsAllProblems(problems);
    }
}