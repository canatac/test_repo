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
import org.ortens.bone.core.model.Document;
import org.ortens.bone.core.model.Document_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class DocumentPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Document.class, BaseEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] DOCUMENT_TITLES = {
        "CONTRACT",
        "REPORT",
        "ARTICLE"
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
        em.createQuery("delete from Document").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : DOCUMENT_TITLES) {
            Document document = new Document();
            document.setDescription(title);
            em.persist(document);
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
    public void shouldFindAllDocumentsUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllDocumentsInJpql = "select g from Document g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Document> documents = em.createQuery(fetchingAllDocumentsInJpql, Document.class).getResultList();

        // then
        System.out.println("Found " + documents.size() + " documents (using JPQL):");
        assertContainsAllDocuments(documents);
    }
    
    private static void assertContainsAllDocuments(Collection<Document> retrievedDocuments) {
        Assert.assertEquals(DOCUMENT_TITLES.length, retrievedDocuments.size());
        final Set<String> retrievedDocumentTitles = new HashSet<String>();
        for (Document document : retrievedDocuments) {
            System.out.println("* " + document);
            retrievedDocumentTitles.add(document.getDescription());
        }
        Assert.assertTrue(retrievedDocumentTitles.containsAll(Arrays.asList(DOCUMENT_TITLES)));
    }
    
    @Test
    public void shouldFindAllDocumentsUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Document> criteria = builder.createQuery(Document.class);
        		
        Root<Document> document = criteria.from(Document.class);
        criteria.select(document);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(document.get(Document_.id)));
        criteria.orderBy(builder.asc(document.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Document> documents = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + documents.size() + " documents (using Criteria):");
        assertContainsAllDocuments(documents);
    }
}