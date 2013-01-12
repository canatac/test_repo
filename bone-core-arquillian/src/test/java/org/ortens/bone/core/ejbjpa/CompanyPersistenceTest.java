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
import org.ortens.bone.core.model.Company;
import org.ortens.bone.core.model.Company_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(Arquillian.class)
public class CompanyPersistenceTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Company.class, GenericEntity.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("jbossas-ds.xml"); //arquillian-jbossas-managed profile. Can stay without perturbing arquillian-glassfish-embedded test 
    }
 
    private static final String[] COMPANY_TITLES = {
        "ACME SARL",
        "LOKOUM SAS",
        "CHEAP SA"
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
        em.createQuery("delete from Company").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : COMPANY_TITLES) {
            Company company = new Company();
            company.setDescription(title);
            em.persist(company);
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
    public void shouldFindAllCompaniesUsingJpqlQuery() throws Exception {
        // given
        String fetchingAllCompaniesInJpql = "select g from Company g order by g.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Company> companies = em.createQuery(fetchingAllCompaniesInJpql, Company.class).getResultList();

        // then
        System.out.println("Found " + companies.size() + " companies (using JPQL):");
        assertContainsAllCompanies(companies);
    }
    
    private static void assertContainsAllCompanies(Collection<Company> retrievedCompanies) {
        Assert.assertEquals(COMPANY_TITLES.length, retrievedCompanies.size());
        final Set<String> retrievedCompanyTitles = new HashSet<String>();
        for (Company company : retrievedCompanies) {
            System.out.println("* " + company);
            retrievedCompanyTitles.add(company.getDescription());
        }
        Assert.assertTrue(retrievedCompanyTitles.containsAll(Arrays.asList(COMPANY_TITLES)));
    }
    
    @Test
    public void shouldFindAllCompaniesUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Company> criteria = builder.createQuery(Company.class);
        		
        Root<Company> company = criteria.from(Company.class);
        criteria.select(company);
        // TIP: If you don't want to use the JPA 2 Metamodel,
        // you can change the get() method call to get("id")
        //criteria.orderBy(builder.asc(company.get(Company_.id)));
        criteria.orderBy(builder.asc(company.get("id")));
        // No WHERE clause, which implies select all

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Company> companies = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + companies.size() + " companies (using Criteria):");
        assertContainsAllCompanies(companies);
    }
}