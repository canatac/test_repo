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
import java.util.Set;
import org.junit.Assert;
import org.ortens.bone.core.model.GenericEntity;
import org.ortens.bone.core.model.Project;
import org.ortens.bone.core.model.Project_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class ProjectPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Project.class, GenericEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] PROJECT_TITLES = {
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
        em.createQuery("delete from Project").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : PROJECT_TITLES) {
            Project project = new Project();
            project.setDescription(title);
            em.persist(project);
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
    public void shouldFindAllProjectsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllProjectsInJpql = "select g from Project g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Project> projects = em.createQuery(fetchingAllProjectsInJpql, Project.class).getResultList();

        // then
        System.out.println("Found " + projects.size() + " projects (using JPQL):");
        assertContainsAllProjects(projects);
    }
    
    private static void assertContainsAllProjects(Collection<Project> retrievedProjects) {
        Assert.assertEquals(PROJECT_TITLES.length, retrievedProjects.size());
        final Set<String> retrievedProjectTitles = new HashSet<String>();
        for (Project project : retrievedProjects) {
            System.out.println("* " + project);
            retrievedProjectTitles.add(project.getDescription());
        }
        Assert.assertTrue(retrievedProjectTitles.containsAll(Arrays.asList(PROJECT_TITLES)));
    }
    
    @Test
    public void shouldFindAllProjectsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Project> criteria = builder.createQuery(Project.class);
        		
        Root<Project> project = criteria.from(Project.class);
        criteria.select(project);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(project.get(Project_.id)));
        criteria.orderBy(builder.asc(project.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Project> projects = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + projects.size() + " projects (using Criteria):");
        assertContainsAllProjects(projects);
    }
}