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
import org.ortens.bone.core.model.BaseEntity;
import org.ortens.bone.core.model.Person;
import org.ortens.bone.core.model.Person_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class PersonPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Person.class, BaseEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] PERSON_TITLES = {
        "Director",
        "PMO",
        "CTO"
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
        em.createQuery("delete from Person").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : PERSON_TITLES) {
            Person person = new Person();
            person.setDescription(title);
            em.persist(person);
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
    public void shouldFindAllPersonsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllPersonsInJpql = "select g from Person g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Person> persons = em.createQuery(fetchingAllPersonsInJpql, Person.class).getResultList();

        // then
        System.out.println("Found " + persons.size() + " persons (using JPQL):");
        assertContainsAllPersons(persons);
    }
    
    private static void assertContainsAllPersons(Collection<Person> retrievedPersons) {
        Assert.assertEquals(PERSON_TITLES.length, retrievedPersons.size());
        final Set<String> retrievedPersonTitles = new HashSet<String>();
        for (Person person : retrievedPersons) {
            System.out.println("* " + person);
            retrievedPersonTitles.add(person.getDescription());
        }
        Assert.assertTrue(retrievedPersonTitles.containsAll(Arrays.asList(PERSON_TITLES)));
    }
    
    @Test
    public void shouldFindAllPersonsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        		
        Root<Person> person = criteria.from(Person.class);
        criteria.select(person);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(person.get(Person_.id)));
        criteria.orderBy(builder.asc(person.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Person> persons = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + persons.size() + " persons (using Criteria):");
        assertContainsAllPersons(persons);
    }
}