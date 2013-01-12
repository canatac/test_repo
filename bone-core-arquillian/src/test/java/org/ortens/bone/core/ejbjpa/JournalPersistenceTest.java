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
import org.ortens.bone.core.model.Journal;
import org.ortens.bone.core.model.Journal_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class JournalPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Journal.class, GenericEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] JOURNAL_TITLES = {
        "DAILY",
        "MONTHLY",
        "SEMESTRIAL",
        "ANNUAL"
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
        em.createQuery("delete from Journal").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : JOURNAL_TITLES) {
            Journal journal = new Journal();
            journal.setDescription(title);
            em.persist(journal);
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
    public void shouldFindAllJournalsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllJournalsInJpql = "select g from Journal g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Journal> journals = em.createQuery(fetchingAllJournalsInJpql, Journal.class).getResultList();

        // then
        System.out.println("Found " + journals.size() + " journals (using JPQL):");
        assertContainsAllJournals(journals);
    }
    
    private static void assertContainsAllJournals(Collection<Journal> retrievedJournals) {
        Assert.assertEquals(JOURNAL_TITLES.length, retrievedJournals.size());
        final Set<String> retrievedJournalTitles = new HashSet<String>();
        for (Journal journal : retrievedJournals) {
            System.out.println("* " + journal);
            retrievedJournalTitles.add(journal.getDescription());
        }
        Assert.assertTrue(retrievedJournalTitles.containsAll(Arrays.asList(JOURNAL_TITLES)));
    }
    
    @Test
    public void shouldFindAllJournalsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Journal> criteria = builder.createQuery(Journal.class);
        		
        Root<Journal> journal = criteria.from(Journal.class);
        criteria.select(journal);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(journal.get(Journal_.id)));
        criteria.orderBy(builder.asc(journal.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Journal> journals = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + journals.size() + " journals (using Criteria):");
        assertContainsAllJournals(journals);
    }
}